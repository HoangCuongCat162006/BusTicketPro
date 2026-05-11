package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Ticket;
import org.example.busticketpro.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByOrderByStatusAscBookedAtDesc();
    Optional<Ticket> findByIdAndUserId(Long ticketId, Long userId);

    // Tìm theo mã vé
    Optional<Ticket> findByTicketCode(String ticketCode);

    // Vé của user
    List<Ticket> findByUserId(Long userId);


    // Vé theo trạng thái
    List<Ticket> findByStatus(TicketStatus status);

    // Tìm bằng mã vé + SĐT
    Optional<Ticket> findByTicketCodeAndPassengerPhone(
            String ticketCode,
            String passengerPhone
    );

    // Vé theo chuyến
    List<Ticket> findByTripId(Long tripId);

    // Kiểm tra ghế đã có vé chưa
    boolean existsBySeatId(Long seatId);

}