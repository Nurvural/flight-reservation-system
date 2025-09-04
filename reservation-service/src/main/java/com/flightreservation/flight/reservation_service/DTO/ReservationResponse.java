package com.flightreservation.flight.reservation_service.DTO;

import lombok.*;
import java.time.LocalDateTime;

import com.flightreservation.flight.reservation_service.entities.Reservation;
import com.flightreservation.flight.reservation_service.entities.ReservationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReservationResponse {
    private Long id;
    private String passengerName;
    private String passengerEmail;
    private Long flightId;
    private Long userId;   
    private double flightPrice;
    private LocalDateTime reservationDate;
    private String status;
    private String specialRequests;
    private String seatNumber;
    private Integer baggageCount;
}
