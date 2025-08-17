package com.flightreservation.flight.reservation_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private String passengerEmail;
    
    private Long flightId;
    //private Long userId;

    private LocalDateTime reservationDate;

    private String status;
    private String specialRequests;
    private String seatNumber;
    private Integer baggageCount;
}
