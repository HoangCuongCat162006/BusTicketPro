package org.example.busticketpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long tripId;
    private Long seatId;
    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;
}
