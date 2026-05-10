package org.example.busticketpro.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.BookingRequest;
import org.example.busticketpro.dto.SeatMapDto;
import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.exception.SeatAlreadyBookedException;
import org.example.busticketpro.service.BookingService;
import org.example.busticketpro.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final SeatService seatService;
    private final BookingService bookingService;

    // Tìm chuyến xe
    @GetMapping
    public String bookingPage() {
        return "user/booking/index";
    }

    // API: Lấy danh sách ghế theo chuyến
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

    // API: Khóa ghế tạm thời (temporary reservation)
    @PostMapping("/api/seats/{seatId}/lock")
    @ResponseBody
    public ResponseEntity<?> lockSeat(@PathVariable Long seatId) {
        try {
            seatService.lockSeat(seatId, 15); // 15 minutes timeout
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Mở khóa ghế
    @PostMapping("/api/seats/{seatId}/unlock")
    @ResponseBody
    public ResponseEntity<?> unlockSeat(@PathVariable Long seatId) {
        try {
            seatService.unlockSeat(seatId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // API: Đặt vé (Transaction: Create ticket + Update seat status)
    @PostMapping("/api/book-ticket")
    @ResponseBody
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequest request) {
        try {
            Ticket ticket = bookingService.processBooking(request);
            return ResponseEntity.ok(ticket);
        } catch (SeatAlreadyBookedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }
}