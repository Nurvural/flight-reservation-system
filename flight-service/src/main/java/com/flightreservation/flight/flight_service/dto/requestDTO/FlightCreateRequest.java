package com.flightreservation.flight.flight_service.dto.requestDTO;

import lombok.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightCreateRequest {

	private String flightNumber;
	private Long departureAirportId;
	private Long arrivalAirportId;
	@Future
	private LocalDateTime departureTime;
	@Future
	private LocalDateTime arrivalTime;
	@Positive
	private Double price;
	private String seatClass;
	@Positive
	private Integer totalSeats;
	private Integer availableSeats;
	private Long airlineId;
}
