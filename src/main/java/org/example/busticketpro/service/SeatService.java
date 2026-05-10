package org.example.busticketpro.service;

import lombok.RequiredArgsConstructor;
import org.example.busticketpro.dto.SeatDto;
import org.example.busticketpro.dto.SeatMapDto;
import org.example.busticketpro.entity.Seat;
import org.example.busticketpro.entity.SeatStatus;
import org.example.busticketpro.entity.TicketStatus;
import org.example.busticketpro.repository.SeatRepository;
import org.example.busticketpro.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public SeatMapDto getAvailableSeats(Long tripId) {
        List<Seat> seats = seatRepository.findByTripId(tripId);

        List<SeatDto> seatDtos = seats.stream()
                .map(seat -> {
                    SeatDto dto = new SeatDto(
                            seat.getId(),
                            seat.getSeatNumber(),
                            "",
                            false
                    );

                    // Check if seat has a valid ticket (PENDING or PAID)
                    boolean hasActiveTicket = ticketRepository.existsBySeatId(seat.getId()) &&
                            ticketRepository.findByTripId(tripId).stream()
                                    .filter(t -> t.getSeat() != null && t.getSeat().getId().equals(seat.getId()))
                                    .anyMatch(t -> t.getStatus() == TicketStatus.PENDING || t.getStatus() == TicketStatus.PAID);

                    // Check if seat is locked (temporary reservation)
                    boolean isLocked = seat.getLockedUntil() != null &&
                            seat.getLockedUntil().isAfter(LocalDateTime.now());

                    // Determine final status
                    if (hasActiveTicket || seat.getStatus() == SeatStatus.BOOKED) {
                        dto.setStatus("BOOKED");
                        dto.setAvailable(false);
                    } else if (seat.getStatus() == SeatStatus.PENDING || isLocked) {
                        dto.setStatus("PENDING");
                        dto.setAvailable(false);
                    } else {
                        dto.setStatus("AVAILABLE");
                        dto.setAvailable(true);
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        int availableCount = (int) seatDtos.stream()
                .filter(SeatDto::isAvailable)
                .count();

        SeatMapDto seatMapDto = new SeatMapDto();
        seatMapDto.setTripId(tripId);
        seatMapDto.setSeats(seatDtos);
        seatMapDto.setTotalSeats(seats.size());
        seatMapDto.setAvailableSeats(availableCount);

        return seatMapDto;
    }

    @Transactional
    public void lockSeat(Long seatId, int lockMinutes) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setLockedUntil(LocalDateTime.now().plusMinutes(lockMinutes));
        seat.setStatus(SeatStatus.PENDING);
        seatRepository.save(seat);
    }

    @Transactional
    public void unlockSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setLockedUntil(null);
        seat.setStatus(SeatStatus.AVAILABLE);
        seatRepository.save(seat);
    }
}
