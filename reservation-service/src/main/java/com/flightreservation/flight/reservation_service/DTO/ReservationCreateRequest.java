package com.flightreservation.flight.reservation_service.DTO;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateRequest {
    private String passengerName;
    private String passengerEmail;
    private Long flightId;
    private LocalDateTime reservationDate;
    private String status;
    private String specialRequests;
    private String seatNumber;
    private Integer baggageCount;
}
