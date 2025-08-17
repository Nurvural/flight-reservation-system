package com.flightreservation.flight.reservation_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "FLIGHT-SERVICE")
public interface FlightClient {

	@GetMapping("/api/flights/{id}")
    void checkFlightExists(@PathVariable("id") Long id);
}
