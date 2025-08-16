package com.flightreservation.flight.flight_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightreservation.flight.flight_service.dto.requestDTO.FlightCreateRequest;
import com.flightreservation.flight.flight_service.dto.requestDTO.FlightUpdateRequest;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightResponse;
import com.flightreservation.flight.flight_service.services.FlightService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/flights")
public class FlightController {

	
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        List<FlightResponse> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }
    
    // Tek uçuşu ID ile getir
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        FlightResponse flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }
    
    // Yeni uçuş oluştur
    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody FlightCreateRequest flightCreateRequest ) {
        FlightResponse createdFlight = flightService.createFlight(flightCreateRequest);
        return ResponseEntity.ok(createdFlight);
    }

    // Uçuşu güncelle
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable Long id,
                                                  @Valid @RequestBody FlightUpdateRequest flightUpdateRequest ) {
        FlightResponse updatedFlight = flightService.updateFlight(id, flightUpdateRequest);
        return ResponseEntity.ok(updatedFlight);
    }

    // Uçuş sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

}
