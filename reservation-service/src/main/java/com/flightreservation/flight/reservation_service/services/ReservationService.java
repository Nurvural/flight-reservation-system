package com.flightreservation.flight.reservation_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flightreservation.flight.reservation_service.DTO.FlightResponse;
import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.client.FlightClient;
import com.flightreservation.flight.reservation_service.entities.Reservation;
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
        flightClient.checkFlightExists(request.getFlightId()); // Eğer flight yoksa FeignException.NotFound fırlatır

        // DTO'dan Reservation entity oluştur
        Reservation reservation = mapper.toEntity(request);

        // Reservation'ı kaydet
        Reservation saved = reservationRepository.save(reservation);

        // DTO'ya çevir ve geri döndür
        return mapper.toDTO(saved);
    }


	public List<ReservationResponse> getAllReservations() {
		return reservationRepository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
	}

	public ReservationResponse getReservationById(Long id) {
		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		return mapper.toDTO(reservation);
	}

	public void deleteReservation(Long id) {
		reservationRepository.deleteById(id);
	}
}
