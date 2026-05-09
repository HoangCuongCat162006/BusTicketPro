package org.example.busticketpro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder  // ✅ Thêm Lombok
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)  // ✅
    private String licensePlate;

    @Column(name = "total_seats", nullable = false)                   // ✅
    private int totalSeats;

    @Column(name = "bus_type")                                         // ✅
    private String busType;
}