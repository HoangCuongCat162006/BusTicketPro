package org.example.busticketpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailDTO {
    private String ticketCode;
    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;
    private String vehicleLicensePlate;
    private String vehicleType;
    private String driverName;
    private String departurePoint;
    private String destination;
    private LocalDateTime departureTime;
    private String seatNumber;
    private Integer floor;
    private String ticketStatus;
}
