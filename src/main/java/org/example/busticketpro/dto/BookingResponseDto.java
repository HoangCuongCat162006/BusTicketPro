package org.example.busticketpro.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
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
    private Long tripId;
    private String departurePoint;
    private String destination;
    private LocalDateTime departureTime;
    private String licensePlate;
    private String busType;
    private String seatNumber;
    private Integer floor;
    private LocalDateTime bookedAt;
    private LocalDateTime lockedUntil;

    private String errorMessage;     // <-- Thêm dòng này

    // Phương thức tiện ích trả về lỗi
    public static BookingResponseDto error(String message) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setErrorMessage(message);
        return dto;
    }
}