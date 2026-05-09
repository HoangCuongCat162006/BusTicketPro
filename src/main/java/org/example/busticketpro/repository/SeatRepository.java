package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Seat;
import org.example.busticketpro.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Danh sách ghế của chuyến
    List<Seat> findByTripId(Long tripId);

    // Tìm ghế theo số ghế
    Optional<Seat> findByTripIdAndSeatNumber(
            Long tripId,
            String seatNumber
    );

    // Danh sách ghế theo trạng thái
    List<Seat> findByTripIdAndStatus(
            Long tripId,
            SeatStatus status
    );

    // Đếm ghế đã đặt
    long countByTripIdAndStatus(
            Long tripId,
            SeatStatus status
    );
}