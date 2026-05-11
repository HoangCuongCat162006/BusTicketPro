package org.example.busticketpro.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.BookingRequest;
import org.example.busticketpro.dto.BookingResponseDto;
import org.example.busticketpro.dto.SeatMapDto;
import org.example.busticketpro.dto.TripResponseDto;
import org.example.busticketpro.entity.*;
import org.example.busticketpro.exception.SeatAlreadyBookedException;
import org.example.busticketpro.repository.LocationRepository;
import org.example.busticketpro.service.BookingService;
import org.example.busticketpro.service.SeatService;
import org.example.busticketpro.service.TripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.busticketpro.entity.Bus;
import org.example.busticketpro.entity.Route;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final SeatService        seatService;
    private final BookingService     bookingService;
    private final TripService        tripService;
    private final LocationRepository locationRepository;

    // ==================== TRANG ĐẶT VÉ ====================
    // Thêm locations vào model để dropdown chọn điểm đi/đến
    @GetMapping
    public String bookingPage(Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        return "user/booking/index";
    }

    // ==================== API TÌM CHUYẾN (MỚI) ====================
    @GetMapping("/api/trips")
    @ResponseBody
    public ResponseEntity<List<TripResponseDto>> searchTrips(
            @RequestParam Long departureId,
            @RequestParam Long arrivalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<TripResponseDto> trips = tripService.searchTrips(departureId, arrivalId, date);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== API LẤY SƠ ĐỒ GHẾ ====================
    @GetMapping("/api/seats")
    @ResponseBody
    public ResponseEntity<SeatMapDto> getAvailableSeats(@RequestParam Long tripId) {
        try {
            SeatMapDto seatMap = seatService.getAvailableSeats(tripId);
            return ResponseEntity.ok(seatMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== API KHÓA GHẾ TẠM THỜI ====================
    @PostMapping("/api/seats/{seatId}/lock")
    @ResponseBody
    public ResponseEntity<?> lockSeat(@PathVariable Long seatId) {
        try {
            seatService.lockSeat(seatId, 15);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== API MỞ KHÓA GHẾ ====================
    @PostMapping("/api/seats/{seatId}/unlock")
    @ResponseBody
    public ResponseEntity<?> unlockSeat(@PathVariable Long seatId) {
        try {
            seatService.unlockSeat(seatId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== API ĐẶT VÉ ====================
    @PostMapping("/api/book-ticket")
    @ResponseBody
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        try {
            Ticket ticket = bookingService.processBooking(request);

            // === SỬA Ở ĐÂY: Chuyển sang DTO thay vì trả Entity ===
            BookingResponseDto response = convertToBookingResponse(ticket);

            return ResponseEntity.ok(response);

        } catch (SeatAlreadyBookedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Đặt vé thất bại: " + e.getMessage());
        }
    }

    // Helper method chuyển Entity sang DTO
    private BookingResponseDto convertToBookingResponse(Ticket ticket) {
        Trip trip = ticket.getTrip();
        Seat seat = ticket.getSeat();
        Bus bus = trip.getBus();           // giả sử Trip có Bus
        Route route = trip.getRoute();     // giả sử Trip có Route

        return BookingResponseDto.builder()
                .ticketId(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .passengerName(ticket.getPassengerName())
                .passengerPhone(ticket.getPassengerPhone())
                .passengerEmail(ticket.getPassengerEmail())
                .totalPrice(ticket.getTotalPrice())
                .status(ticket.getStatus().name())
                .tripId(trip.getId())
                .departurePoint(route.getDeparture().getName())   // điều chỉnh tên field nếu khác
                .destination(route.getArrival().getName())
                .departureTime(trip.getDepartureTime())
                .licensePlate(bus.getLicensePlate())
                .busType(bus.getBusType())
                .seatNumber(seat.getSeatNumber())
                .floor(seat.getFloor())
                .bookedAt(ticket.getBookedAt())
                .lockedUntil(seat.getLockedUntil())
                .build();
    }
}