package org.example.busticketpro.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private Long seatId;
    private Long tripId;
    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;
    private Long userId;           // <-- Phải có dòng này
}