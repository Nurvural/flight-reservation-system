package com.flightreservation.flight.reservation_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passengerName;
    private String passengerEmail;
    private Long flightId;
    private double flightPrice; 
    private Long userId;
    private LocalDateTime reservationDate;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING; // varsayılan enum değeri
    private String specialRequests;
    private String seatNumber;
    private Integer baggageCount;
    
    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

}
