package org.example.busticketpro.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticketCode;

    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime bookedAt;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    @JoinColumn(name="seat_id")
    private Seat seat;

    @ManyToOne(optional = true)
    private User user;
}