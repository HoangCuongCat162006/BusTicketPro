package org.example.busticketpro.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TripResponseDto {
    private Long tripId;
    private String busPlate;
    private String routeName;
    private LocalDateTime departureTime;
    private double price;
    private int availableSeats;
    private int totalSeats;
}