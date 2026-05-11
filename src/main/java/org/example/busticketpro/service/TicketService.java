package org.example.busticketpro.service;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.BookingRequest;
import org.example.busticketpro.dto.TicketDetailDTO;
import org.example.busticketpro.dto.TicketSummaryDTO;
import org.example.busticketpro.entity.*;
import org.example.busticketpro.exception.SeatAlreadyBookedException;
import org.example.busticketpro.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final TripRepository tripRepository;

    // ====================== CORE-06: ĐẶT VÉ + TRANSACTION ======================
    @Transactional
    public Ticket processBooking(BookingRequest request) {
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến xe"));

        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatAlreadyBookedException("Ghế này đã được đặt hoặc đang giữ chỗ");
        }

        // Giữ ghế tạm thời
        seat.setStatus(SeatStatus.PENDING);
        seat.setLockedUntil(LocalDateTime.now().plusMinutes(15));
        seatRepository.save(seat);

        // Tạo vé
        Ticket ticket = Ticket.builder()
                .ticketCode(generateTicketCode())
                .passengerName(request.getPassengerName())
                .passengerPhone(request.getPassengerPhone())
                .passengerEmail(request.getPassengerEmail())
                .totalPrice(trip.getPrice())
                .status(TicketStatus.PENDING)
                .bookedAt(LocalDateTime.now())
                .trip(trip)
                .seat(seat)
                .user(null) // sẽ set sau nếu cần
                .build();

        return ticketRepository.save(ticket);
    }

    private String generateTicketCode() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ====================== CORE-07 ======================
    @Transactional(readOnly = true)
    public List<TicketSummaryDTO> getTicketsByUser(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets.stream().map(this::convertToSummaryDTO).toList();
    }

    @Transactional(readOnly = true)
    public TicketDetailDTO getTicketDetail(String ticketCode, String phoneNumber) {
        Ticket ticket = ticketRepository.findByTicketCodeAndPassengerPhone(ticketCode, phoneNumber)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé hoặc SĐT không khớp"));
        return convertToDetailDTO(ticket);
    }

    // ====================== CORE-08: STAFF ======================
    @Transactional(readOnly = true)
    public List<TicketDetailDTO> getAllTicketsForStaff() {
        List<Ticket> tickets = ticketRepository.findAllByOrderByStatusAscBookedAtDesc();
        return tickets.stream().map(this::convertToDetailDTO).toList();
    }

    @Transactional
    public void confirmPayment(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể xác nhận vé đang chờ thanh toán");
        }

        ticket.setStatus(TicketStatus.PAID);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void cancelTicketByStaff(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        ticket.setStatus(TicketStatus.CANCELLED);

        if (ticket.getSeat() != null) {
            Seat seat = ticket.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedUntil(null);
            seatRepository.save(seat);
        }

        ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public TicketDetailDTO getTicketDetailById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));
        return convertToDetailDTO(ticket);
    }

    // ====================== CORE-09: HỦY VÉ ======================
    @Transactional
    public void cancelTicketForPassenger(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy vé đang chờ thanh toán");
        }

        if (ticket.getTrip() != null &&
                ticket.getTrip().getDepartureTime().minusHours(12).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Đã quá thời hạn hủy vé (phải hủy trước 12 tiếng)");
        }

        ticket.setStatus(TicketStatus.CANCELLED);

        if (ticket.getSeat() != null) {
            Seat seat = ticket.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedUntil(null);
            seatRepository.save(seat);
        }

        ticketRepository.save(ticket);
    }

    // ====================== CONVERTERS ======================
    private TicketSummaryDTO convertToSummaryDTO(Ticket ticket) {
        TicketSummaryDTO dto = new TicketSummaryDTO();
        dto.setId(ticket.getId());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setTotalPrice(ticket.getTotalPrice());
        dto.setStatus(ticket.getStatus() != null ? ticket.getStatus().name() : null);
        dto.setDepartureTime(ticket.getTrip() != null ? ticket.getTrip().getDepartureTime() : null);

        if (ticket.getSeat() != null) {
            dto.setSeatNumber(ticket.getSeat().getSeatNumber());
        }

        if (ticket.getTrip() != null && ticket.getTrip().getRoute() != null) {
            dto.setDeparturePoint(ticket.getTrip().getRoute().getDeparture().getName());
            dto.setDestination(ticket.getTrip().getRoute().getArrival().getName());
        }
        return dto;
    }

    private TicketDetailDTO convertToDetailDTO(Ticket ticket) {
        TicketDetailDTO dto = new TicketDetailDTO();

        dto.setTicketCode(ticket.getTicketCode());
        dto.setPassengerName(ticket.getPassengerName());
        dto.setPassengerPhone(ticket.getPassengerPhone());
        dto.setPassengerEmail(ticket.getPassengerEmail());
        dto.setTicketStatus(ticket.getStatus() != null ? ticket.getStatus().name() : "UNKNOWN");
        dto.setDepartureTime(ticket.getTrip() != null ? ticket.getTrip().getDepartureTime() : null);

        if (ticket.getSeat() != null) {
            dto.setSeatNumber(ticket.getSeat().getSeatNumber());
            dto.setFloor(ticket.getSeat().getFloor());
        }

        if (ticket.getTrip() != null) {
            if (ticket.getTrip().getRoute() != null) {
                dto.setDeparturePoint(ticket.getTrip().getRoute().getDeparture().getName());
                dto.setDestination(ticket.getTrip().getRoute().getArrival().getName());
            }
            if (ticket.getTrip().getBus() != null) {
                dto.setVehicleLicensePlate(ticket.getTrip().getBus().getLicensePlate());
            }
        }

        return dto;
    }
}