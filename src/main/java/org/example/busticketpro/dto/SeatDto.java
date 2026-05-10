// dto/SeatDto.java
package org.example.busticketpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatDto {
    private Long id;
    private String seatNumber;
    private String status; // AVAILABLE, PENDING, BOOKED
    private boolean isAvailable;
}