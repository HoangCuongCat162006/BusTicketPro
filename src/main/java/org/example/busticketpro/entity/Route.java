package org.example.busticketpro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "routes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "departure_id")
    private Location departure;

    @ManyToOne
    @JoinColumn(name = "arrival_id")
    private Location arrival;

    private BigDecimal distance;
    private Integer duration;     // phút
}