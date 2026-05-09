package org.example.busticketpro.repository;

import org.example.busticketpro.entity.Payment;
import org.example.busticketpro.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Tìm payment theo ticket
    Payment findByTicketId(Long ticketId);

    // Danh sách payment theo trạng thái
    List<Payment> findByStatus(PaymentStatus status);

    // Tìm theo mã giao dịch
    Payment findByTransactionCode(String transactionCode);
}