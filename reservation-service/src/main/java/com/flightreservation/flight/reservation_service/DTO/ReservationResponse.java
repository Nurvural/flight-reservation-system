package com.flightreservation.flight.reservation_service.DTO;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private String passengerName;
    private String passengerEmail;
    private Long flightId;
    private Long userId;
    private LocalDateTime reservationDate;
    private String status;
    private String specialRequests;
    private String seatNumber;
    private Integer baggageCount;
}
