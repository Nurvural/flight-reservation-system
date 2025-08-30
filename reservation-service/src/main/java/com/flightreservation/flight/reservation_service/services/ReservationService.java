package com.flightreservation.flight.reservation_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.client.FlightWebClient;
import com.flightreservation.flight.reservation_service.entities.Reservation;
import com.flightreservation.flight.reservation_service.exception.BusinessException;
import com.flightreservation.flight.reservation_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.reservation_service.mapper.ReservationMapper;
import com.flightreservation.flight.reservation_service.repositories.ReservationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationMapper mapper;
	private final FlightWebClient flightClient;

	public Mono<ReservationResponse> createReservation(ReservationCreateRequest request) {
		return flightClient.checkFlightExists(request.getFlightId())
				.then(flightClient.getFlightPrice(request.getFlightId())).flatMap(price -> {
					Reservation reservation = mapper.toEntity(request);
					reservation.setReservationDate(LocalDateTime.now());
					reservation.setFlightPrice(price);
					Reservation saved = reservationRepository.save(reservation); // blocking, ama basit projede kabul
																					// edilebilir
					return Mono.just(mapper.toDTO(saved));
				});
	}

	public Mono<ReservationResponse> updateReservation(Long id, ReservationCreateRequest request) {
		log.info("Updating reservation with id: {}", id);
		// Mevcut reservation kontrolü
		Reservation existing = reservationRepository.findById(id).orElseThrow(() -> {
			log.error("Reservation not found with id: {}", id);
			return new ResourceNotFoundException("Reservation not found with id: " + id);
		});
		return flightClient.checkFlightExists(request.getFlightId())
				.doOnSuccess(unused -> log.info("Flight exists with id: {}", request.getFlightId()))
				.then(Mono.fromCallable(() -> {
					// DTO'daki alanları entity'ye güncelle
					existing.setPassengerName(request.getPassengerName());
					existing.setPassengerEmail(request.getPassengerEmail());
					existing.setFlightId(request.getFlightId());
					existing.setReservationDate(LocalDateTime.now());
					existing.setStatus(request.getStatus());
					existing.setSpecialRequests(request.getSpecialRequests());
					existing.setSeatNumber(request.getSeatNumber());
					existing.setBaggageCount(request.getBaggageCount());

					// Güncellenen reservation'ı kaydet
					Reservation updated = reservationRepository.save(existing);
					log.info("Reservation updated with id: {}", updated.getId());
					// DTO'ya çevirip döndür
					return mapper.toDTO(updated);
				})).onErrorMap(ex -> {
					log.error("Update reservation failed: {}", ex.getMessage());
					if (ex instanceof ResourceNotFoundException) {
						return ex; // ResourceNotFoundException'ı olduğu gibi fırlat
					}
					return new BusinessException("Failed to update reservation: " + ex.getMessage());
				});
	}

	public List<ReservationResponse> getAllReservations() {
		log.info("Fetching all reservations");
		List<ReservationResponse> reservations = reservationRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
				.stream().map(mapper::toDTO).collect(Collectors.toList());
		log.info("Total reservations found: {}", reservations.size());
		return reservations;
	}

	public ReservationResponse getReservationById(Long id) {
		log.info("Fetching reservation with id: {}", id);
		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> {
			log.error("Reservation not found with id: {}", id);
			return new ResourceNotFoundException("Reservation not found with id: " + id);
		});
		log.info("Reservation fetched with id: {}", reservation.getId());
		return mapper.toDTO(reservation);
	}

	public void deleteReservation(Long id) {
		log.info("Deleting reservation with id: {}", id);
		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> {
			log.error("Reservation not found with id: {}", id);
			return new ResourceNotFoundException("Reservation not found with id: " + id);
		});
		reservationRepository.delete(reservation);
		log.info("Reservation deleted with id: {}", id);
	}
}
