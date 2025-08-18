package com.flightreservation.flight.reservation_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.client.FlightClient;
import com.flightreservation.flight.reservation_service.entities.Reservation;
import com.flightreservation.flight.reservation_service.exception.BusinessException;
import com.flightreservation.flight.reservation_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.reservation_service.mapper.ReservationMapper;
import com.flightreservation.flight.reservation_service.repositories.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationMapper mapper;
	private final FlightClient flightClient;

	public ReservationResponse createReservation(ReservationCreateRequest request) {
		// Flight servise sadece ID kontrolü için çağrı
		try {
			flightClient.checkFlightExists(request.getFlightId()); // Eğer flight yoksa FeignException.NotFound fırlatır
		} catch (feign.FeignException.NotFound ex) {
			throw new ResourceNotFoundException("Flight not found with id: " + request.getFlightId());
		} catch (feign.FeignException ex) {
			throw new BusinessException("Error calling Flight service: " + ex.getMessage());
		}

		// DTO'dan Reservation entity oluştur
		Reservation reservation = mapper.toEntity(request);
		reservation.setReservationDate(LocalDateTime.now());
		// Reservation'ı kaydet
		Reservation saved = reservationRepository.save(reservation);
		// DTO'ya çevir ve geri döndür
		return mapper.toDTO(saved);
	}

	public ReservationResponse updateReservation(Long id, ReservationCreateRequest request) {
		// Mevcut reservation kontrolü
		Reservation existing = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

		// Flight servise sadece ID kontrolü için çağrı
		flightClient.checkFlightExists(request.getFlightId()); // Eğer flight yoksa FeignException.NotFound fırlatır

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

		// DTO'ya çevirip döndür
		return mapper.toDTO(updated);
	}

	public List<ReservationResponse> getAllReservations() {
		return reservationRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	public ReservationResponse getReservationById(Long id) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found" + id));
		return mapper.toDTO(reservation);
	}

	public void deleteReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found" + id));
		reservationRepository.delete(reservation);
	}
}
