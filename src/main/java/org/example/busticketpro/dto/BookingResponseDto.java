// src/main/java/org/example/busticketpro/dto/BookingResponseDto.java
package org.example.busticketpro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {

    private Long ticketId;
    private String ticketCode;
    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;
    private BigDecimal totalPrice;
    private String status;

    // Thông tin chuyến xe
    private Long tripId;
    private String departurePoint;
    private String destination;
    private LocalDateTime departureTime;
    private String licensePlate;        // Biển số xe
    private String busType;

    // Thông tin ghế
    private String seatNumber;
    private Integer floor;

    private LocalDateTime bookedAt;
    private LocalDateTime lockedUntil;   // thời gian giữ chỗ
}