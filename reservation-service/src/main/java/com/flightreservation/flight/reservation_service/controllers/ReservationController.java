package com.flightreservation.flight.reservation_service.controllers;

import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.services.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;

	@PostMapping
	public Mono<ResponseEntity<ReservationResponse>> createReservation(
			 @RequestHeader("X-User-Email") String email,
			@Valid @RequestBody ReservationCreateRequest request) {
	
		return reservationService.createReservation(request, email)
                .map(ResponseEntity::ok);
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<ReservationResponse>> updateReservation(@Valid @PathVariable Long id,
			@RequestBody ReservationCreateRequest request) {
		return reservationService.updateReservation(id, request).map(response -> ResponseEntity.ok(response));
	}

	@GetMapping
	public ResponseEntity<List<ReservationResponse>> getReservations(
			@RequestHeader(value = "X-User-Email", required = false) String email,
			@RequestHeader(value = "X-User-Role", required = false) String role) {

		try {
			List<ReservationResponse> reservations;
			if ("ADMIN".equalsIgnoreCase(role)) {
				reservations = reservationService.getAllReservations();
			} else {
				reservations = reservationService.getReservationsByEmail(email);
			}
			return ResponseEntity.ok(reservations);
		} catch (Exception ex) {
			ex.printStackTrace(); // burası önemli, hatayı gör
			return ResponseEntity.status(500).body(Collections.emptyList());
		}
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