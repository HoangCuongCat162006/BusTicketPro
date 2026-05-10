package org.example.busticketpro.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TripSearchRequest {
    private Long departureLocationId;
    private Long arrivalLocationId;
    private LocalDate departureDate;
}