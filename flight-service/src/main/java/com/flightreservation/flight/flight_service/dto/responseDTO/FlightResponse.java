package com.flightreservation.flight.flight_service.dto.responseDTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class FlightResponse {
	
    private Long id;

    private String flightNumber;

    private Long departureAirportId;

    private Long arrivalAirportId;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private double price;

    private String seatClass;

    private Integer totalSeats;

    private Integer availableSeats;

    private Long airlineId;
}
