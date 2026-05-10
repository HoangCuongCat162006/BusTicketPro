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

import java.math.BigDecimal;
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
        // Retrieve seat with optimistic locking (version field)
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        // Check if seat is available
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatAlreadyBookedException("Seat is not available. Current status: " + seat.getStatus());
        }

        // Check if seat is locked by another user
        if (seat.getLockedUntil() != null && seat.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new SeatAlreadyBookedException("Seat is temporarily reserved by another user");
        }

        // Retrieve trip
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // Create ticket with PENDING status
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
                .build();

        // Update seat status to PENDING
        seat.setStatus(SeatStatus.PENDING);
        seat.setLockedUntil(LocalDateTime.now().plusMinutes(15)); // 15-minute reservation

        // Save both entities in the same transaction
        // If any operation fails, both will rollback
        Ticket savedTicket = ticketRepository.save(ticket);
        seatRepository.save(seat);

        return savedTicket;
    }

    private String generateTicketCode() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
