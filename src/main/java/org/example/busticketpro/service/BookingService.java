package org.example.busticketpro.service;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.BookingRequest;
import org.example.busticketpro.entity.Seat;
import org.example.busticketpro.entity.SeatStatus;
import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.entity.TicketStatus;
import org.example.busticketpro.entity.Trip;
import org.example.busticketpro.exception.SeatAlreadyBookedException;
import org.example.busticketpro.repository.SeatRepository;
import org.example.busticketpro.repository.TicketRepository;
import org.example.busticketpro.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final TripRepository tripRepository;

    @Transactional(rollbackFor = Exception.class)
    public Ticket processBooking(BookingRequest request) {

        // =====================================================
        // 1. Lấy ghế
        // =====================================================
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy ghế"));

        LocalDateTime now = LocalDateTime.now();

        // =====================================================
        // 2. Nếu ghế đang PENDING nhưng đã hết hạn giữ chỗ
        // => tự động mở lại ghế
        // =====================================================
        if (seat.getStatus() == SeatStatus.PENDING) {

            boolean lockExpired =
                    seat.getLockedUntil() == null ||
                            seat.getLockedUntil().isBefore(now);

            if (lockExpired) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedUntil(null);

                seatRepository.save(seat);
            }
        }

        // =====================================================
        // 3. Kiểm tra ghế còn trống không
        // =====================================================
        // Nếu ghế đang PENDING nhưng lock đã hết hạn
        // thì mở lại
        if (seat.getStatus() == SeatStatus.PENDING &&
                seat.getLockedUntil() != null &&
                seat.getLockedUntil().isBefore(now)) {

            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedUntil(null);
        }

        // =====================================================
        // 4. Lấy chuyến xe
        // =====================================================
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy chuyến xe"));

        // =====================================================
        // 5. Khóa ghế
        // =====================================================
        seat.setStatus(SeatStatus.BOOKED);
        seat.setLockedUntil(null);

        // =====================================================
        // 6. Tạo vé
        // =====================================================
        Ticket ticket = Ticket.builder()
                .ticketCode(generateTicketCode())
                .passengerName(request.getPassengerName())
                .passengerPhone(request.getPassengerPhone())
                .passengerEmail(request.getPassengerEmail())
                .userId(request.getUserId())
                .totalPrice(trip.getPrice())
                .status(TicketStatus.PAID)
                .bookedAt(now)
                .trip(trip)
                .seat(seat)
                .build();

        // =====================================================
        // 7. Lưu dữ liệu
        // =====================================================
        seat.setStatus(SeatStatus.BOOKED);
        seat.setLockedUntil(null);

        ticket.setStatus(TicketStatus.PAID);    

        ticketRepository.save(ticket);
        seatRepository.save(seat);

        return ticket;
    }

    // =====================================================
    // Sinh mã vé
    // =====================================================
    private String generateTicketCode() {
        return "BK" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}