package com.flightreservation.flight.flight_service.dto.requestDTO;

import lombok.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightUpdateRequest {
    
    @NotBlank(message = "Flight number boş olamaz")
    private String flightNumber;

    @NotNull(message = "Departure airport id boş olamaz")
    private Long departureAirportId;

    @NotNull(message = "Arrival airport id boş olamaz")
    private Long arrivalAirportId;

    @NotNull(message = "Departure time boş olamaz")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time boş olamaz")
    private LocalDateTime arrivalTime;

    @Positive(message = "Price sıfırdan büyük olmalı")
    private double price;

    @NotBlank(message = "Seat class boş olamaz")
    private String seatClass;

    @Positive(message = "Total seats sıfırdan büyük olmalı")
    private Integer totalSeats;

    @PositiveOrZero(message = "Available seats sıfır veya pozitif olmalı")
    private Integer availableSeats;

    @NotNull(message = "Airline id boş olamaz")
    private Long airlineId;
}
