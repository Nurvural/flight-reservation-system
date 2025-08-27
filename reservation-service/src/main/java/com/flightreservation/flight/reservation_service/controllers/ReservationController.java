package com.flightreservation.flight.reservation_service.controllers;

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

import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.services.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
	 private final ReservationService reservationService;

	    @PostMapping
	    public ResponseEntity<ReservationResponse> createReservation(
	            @RequestBody ReservationCreateRequest request) {
	        ReservationResponse response = reservationService.createReservation(request);
	        return ResponseEntity.ok(response);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @RequestBody ReservationCreateRequest request) {
	    	 ReservationResponse response = reservationService.updateReservation(id, request);
	    	    return ResponseEntity.ok(response);
	    }
	    @GetMapping
	    public ResponseEntity<List<ReservationResponse>> getAllReservations() { 
	        return ResponseEntity.ok(reservationService.getAllReservations());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
	        ReservationResponse response = reservationService.getReservationById(id);
	        return ResponseEntity.ok(response);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
	        reservationService.deleteReservation(id);
	        return ResponseEntity.noContent().build();
	    }
}
