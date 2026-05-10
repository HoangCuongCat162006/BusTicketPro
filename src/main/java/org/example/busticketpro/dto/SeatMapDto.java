package org.example.busticketpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapDto {
    private Long tripId;
    private List<SeatDto> seats;
    private int totalSeats;
    private int availableSeats;
}
