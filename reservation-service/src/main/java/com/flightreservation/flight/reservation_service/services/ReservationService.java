package com.flightreservation.flight.reservation_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.client.FlightWebClient;
import com.flightreservation.flight.reservation_service.client.UserWebClient;
import com.flightreservation.flight.reservation_service.entities.Reservation;
import com.flightreservation.flight.reservation_service.exception.BusinessException;
import com.flightreservation.flight.reservation_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.reservation_service.mapper.ReservationMapper;
import com.flightreservation.flight.reservation_service.repositories.ReservationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationMapper mapper;
	private final FlightWebClient flightClient;
	private final UserWebClient userClient;

	public Mono<ReservationResponse> createReservation(ReservationCreateRequest request, String userEmail) {
	    return flightClient.checkFlightExists(request.getFlightId())
	        .then(flightClient.getFlightPrice(request.getFlightId()))
	        .flatMap(price -> {
	            Reservation reservation = mapper.toEntity(request);
	            reservation.setReservationDate(LocalDateTime.now());
	            reservation.setFlightPrice(price);

	            if (userEmail != null && !userEmail.isBlank()) {
	                // Login olmuş kullanıcı, bilgileri JWT'den alıyoruz
	                reservation.setPassengerEmail(userEmail);
	                // Eğer isim de JWT veya başka bir header’dan geliyorsa ekleyebilirsin
	                // reservation.setPassengerName(userName);
	            } else {
	                // Guest kullanıcı
	                if (request.getPassengerName() == null || request.getPassengerName().isBlank() ||
	                    request.getPassengerEmail() == null || request.getPassengerEmail().isBlank()) {
	                    return Mono.error(new IllegalArgumentException(
	                        "Guest kullanıcı için passengerName ve passengerEmail zorunludur"));
	                }
	                reservation.setPassengerName(request.getPassengerName());
	                reservation.setPassengerEmail(request.getPassengerEmail());
	            }

	            return Mono.fromCallable(() -> reservationRepository.save(reservation))
	                       .subscribeOn(Schedulers.boundedElastic())
	                       .map(mapper::toDTO);
	        })
	        .doOnError(err -> {
	            System.err.println("createReservation error: " + err.getMessage());
	            err.printStackTrace();
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
				.then(flightClient.getFlightPrice(request.getFlightId())) // fiyatı alıyoruz
				.flatMap(price -> Mono.fromCallable(() -> {
					// DTO'daki alanları entity'ye güncelle
					existing.setPassengerName(request.getPassengerName());
					existing.setPassengerEmail(request.getPassengerEmail());
					existing.setFlightId(request.getFlightId());
					existing.setReservationDate(LocalDateTime.now());
					existing.setSpecialRequests(request.getSpecialRequests());
					existing.setSeatNumber(request.getSeatNumber());
					existing.setBaggageCount(request.getBaggageCount());
					existing.setFlightPrice(price);
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
		List<ReservationResponse> reservations = reservationRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.ASC, "id"))
				.stream().map(mapper::toDTO).collect(Collectors.toList());
		log.info("Total reservations found: {}", reservations.size());
		return reservations;
	}
	public List<ReservationResponse> getReservationsByEmail(String email) {
	    return reservationRepository.findByPassengerEmailAndDeletedFalse(email)
	            .stream()
	            .map(mapper::toDTO)
	            .collect(Collectors.toList());
	}


	public ReservationResponse getReservationById(Long id) {
		log.info("Fetching reservation with id: {}", id);
		Reservation reservation = reservationRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
			log.error("Reservation not found with id: {}", id);
			return new ResourceNotFoundException("Reservation not found with id: " + id);
		});
		log.info("Reservation fetched with id: {}", reservation.getId());
		return mapper.toDTO(reservation);
	}

	public void deleteReservation(Long id) {
		log.info("Soft deleting reservation with id: {}", id);
		Reservation reservation = reservationRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
			log.error("Reservation not found with id: {}", id);
			return new ResourceNotFoundException("Reservation not found with id: " + id);
		});
	    reservation.setDeleted(true); // soft delete
	    reservationRepository.save(reservation);

	    log.info("Reservation soft deleted with id: {}", id);
	}
}
