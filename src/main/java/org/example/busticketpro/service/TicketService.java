package org.example.busticketpro.service;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.TicketDetailDTO;
import org.example.busticketpro.entity.Seat;
import org.example.busticketpro.entity.SeatStatus;
import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.entity.TicketStatus;
import org.example.busticketpro.exception.SeatAlreadyBookedException;
import org.example.busticketpro.repository.SeatRepository;
import org.example.busticketpro.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public TicketDetailDTO getTicketDetail(String ticketCode, String phoneNumber) {
        // Find ticket by code and phone number
        Ticket ticket = ticketRepository.findByTicketCodeAndPassengerPhone(ticketCode, phoneNumber)
                .orElseThrow(() -> new SeatAlreadyBookedException("Ticket not found or phone number doesn't match"));

        // Map to DTO using JOIN relationships
        TicketDetailDTO dto = new TicketDetailDTO();
        dto.setTicketCode(ticket.getTicketCode());
        dto.setPassengerName(ticket.getPassengerName());
        dto.setPassengerPhone(ticket.getPassengerPhone());
        dto.setPassengerEmail(ticket.getPassengerEmail());
        dto.setTicketStatus(ticket.getStatus().name());

        // Get seat information
        if (ticket.getSeat() != null) {
            dto.setSeatNumber(ticket.getSeat().getSeatNumber());
            dto.setFloor(ticket.getSeat().getFloor());
        }

        // Get trip information
        if (ticket.getTrip() != null) {
            dto.setDepartureTime(ticket.getTrip().getDepartureTime());

            // Get route information
            if (ticket.getTrip().getRoute() != null) {
                dto.setDeparturePoint(
                        ticket.getTrip().getRoute().getDeparture().getName()
                );

                dto.setDestination(
                        ticket.getTrip().getRoute().getArrival().getName()
                );
            }

            // Get bus information
            if (ticket.getTrip().getBus() != null) {
                dto.setVehicleLicensePlate(ticket.getTrip().getBus().getLicensePlate());
                dto.setVehicleType(ticket.getTrip().getBus().getBusType());
            }
        }

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public Ticket confirmPayment(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Only PENDING tickets can be confirmed");
        }

        // Update ticket status to PAID
        ticket.setStatus(TicketStatus.PAID);

        // Update seat status to BOOKED
        if (ticket.getSeat() != null) {
            Seat seat = ticket.getSeat();
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedUntil(null); // Clear any temporary lock
            seatRepository.save(seat);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional(rollbackFor = Exception.class)
    public Ticket cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Only PENDING tickets can be cancelled");
        }

        // Update ticket status to CANCELLED
        ticket.setStatus(TicketStatus.CANCELLED);

        // Release seat back to AVAILABLE
        if (ticket.getSeat() != null) {
            Seat seat = ticket.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedUntil(null); // Clear any temporary lock
            seatRepository.save(seat);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional(rollbackFor = Exception.class)
    public Ticket cancelTicketForPassenger(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Only PENDING tickets can be cancelled by passengers");
        }

        // Check if cancellation is at least 12 hours before departure
        if (ticket.getTrip() != null && ticket.getTrip().getDepartureTime() != null) {
            LocalDateTime departureTime = ticket.getTrip().getDepartureTime();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime cutoffTime = departureTime.minusHours(12);
            
            if (now.isAfter(cutoffTime)) {
                throw new RuntimeException("Tickets can only be cancelled at least 12 hours before departure. " +
                        "Departure time: " + departureTime + ", Current time: " + now);
            }
        }

        // Update ticket status to CANCELLED
        ticket.setStatus(TicketStatus.CANCELLED);

        // Release seat back to AVAILABLE
        if (ticket.getSeat() != null) {
            Seat seat = ticket.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedUntil(null); // Clear any temporary lock
            seatRepository.save(seat);
        }

        return ticketRepository.save(ticket);
    }
}
