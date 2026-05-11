package org.example.busticketpro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketCode;

    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;

    private Long userId;                    // Đang dùng userId

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime bookedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    // === KHÔNG CẦN field User full object ở lúc này ===
}