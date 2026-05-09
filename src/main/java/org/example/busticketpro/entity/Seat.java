package org.example.busticketpro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ với Trip
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    // Thông tin ghế
    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "floor")
    private Integer floor;

    // Trạng thái ghế
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    // Lock ghế (giữ chỗ)
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    // CHỐNG TRÙNG GHẾ (RẤT QUAN TRỌNG)
    @Version
    private Long version;
}