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
public class TicketSummaryDTO {
    private Long id;
    private String ticketCode;
    private String departurePoint;
    private String destination;
    private LocalDateTime departureTime;
    private String seatNumber;
    private String status;
    private BigDecimal totalPrice;
}