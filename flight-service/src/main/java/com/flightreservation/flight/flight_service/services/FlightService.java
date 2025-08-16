package com.flightreservation.flight.flight_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.requestDTO.FlightCreateRequest;
import com.flightreservation.flight.flight_service.dto.requestDTO.FlightUpdateRequest;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightResponse;
import com.flightreservation.flight.flight_service.entities.Flight;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.repositories.AirlineRepository;
import com.flightreservation.flight.flight_service.repositories.AirportRepository;
import com.flightreservation.flight.flight_service.repositories.FlightRepository;

@Service
public class FlightService {

	private final FlightRepository flightRepository;
	private final AirportRepository airportRepository;
	private final AirlineRepository airlineRepository;

	public FlightService(FlightRepository flightRepository, AirportRepository airportRepository,
			AirlineRepository airlineRepository) {
		this.flightRepository = flightRepository;
		this.airportRepository = airportRepository;
		this.airlineRepository = airlineRepository;
	}

	// Entity → DTO mapping
	// Flight entity'sini FlightResponse DTO'suna dönüştürür
	private FlightResponse mapToResponse(Flight flight) {
		return FlightResponse.builder().id(flight.getId()).flightNumber(flight.getFlightNumber())
				.departureAirportId(flight.getDepartureAirport().getId())
				.arrivalAirportId(flight.getArrivalAirport().getId()).departureTime(flight.getDepartureTime())
				.arrivalTime(flight.getArrivalTime()).price(flight.getPrice()).seatClass(flight.getSeatClass())
				.totalSeats(flight.getTotalSeats()).availableSeats(flight.getAvailableSeats())
				.airlineId(flight.getAirline().getId()).build();
	}

	// Yeni uçuş oluşturma
	public FlightResponse createFlight(FlightCreateRequest flightCreateRequest) {
		if (flightCreateRequest.getAvailableSeats() != null
				&& flightCreateRequest.getAvailableSeats() > flightCreateRequest.getTotalSeats()) {
			throw new BusinessException("Available seats cannot exceed total seats");
		}
		// Flight entity'si oluşturulur
		Flight flight = Flight.builder().flightNumber(flightCreateRequest.getFlightNumber())
				.departureAirport(airportRepository.findById(flightCreateRequest.getDepartureAirportId())
						.orElseThrow(() -> new ResourceNotFoundException("Departure airport not found")))
				.arrivalAirport(airportRepository.findById(flightCreateRequest.getArrivalAirportId())
						.orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found")))
				.departureTime(flightCreateRequest.getDepartureTime()).arrivalTime(flightCreateRequest.getArrivalTime())
				.price(flightCreateRequest.getPrice()).seatClass(flightCreateRequest.getSeatClass())
				.totalSeats(flightCreateRequest.getTotalSeats())
				.availableSeats(
						flightCreateRequest.getAvailableSeats() != null ? flightCreateRequest.getAvailableSeats()
								: flightCreateRequest.getTotalSeats())
				.airline(airlineRepository.findById(flightCreateRequest.getAirlineId())
						.orElseThrow(() -> new ResourceNotFoundException("Airline not found")))
				.build();

		// Flight entity'si veritabanına kaydedilir
		Flight savedFlight = flightRepository.save(flight);
		// Kaydedilen entity DTO'ya dönüştürülüp döndürülür
		return mapToResponse(savedFlight);
	}

	// Mevcut uçuşu güncelleme
	public FlightResponse updateFlight(Long id, FlightUpdateRequest flightUpdateRequest) {
		Flight flight = flightRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

		// İş kuralı kontrolü
		if (flightUpdateRequest.getAvailableSeats() != null
				&& flightUpdateRequest.getAvailableSeats() > flightUpdateRequest.getTotalSeats()) {
			throw new BusinessException("Available seats cannot exceed total seats");
		}

		flight.setFlightNumber(flightUpdateRequest.getFlightNumber());
		flight.setDepartureAirport(airportRepository.findById(flightUpdateRequest.getDepartureAirportId())
				.orElseThrow(() -> new ResourceNotFoundException("Departure airport not found")));
		flight.setArrivalAirport(airportRepository.findById(flightUpdateRequest.getArrivalAirportId())
				.orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found")));
		flight.setDepartureTime(flightUpdateRequest.getDepartureTime());
		flight.setArrivalTime(flightUpdateRequest.getArrivalTime());
		flight.setPrice(flightUpdateRequest.getPrice());
		flight.setSeatClass(flightUpdateRequest.getSeatClass());
		flight.setTotalSeats(flightUpdateRequest.getTotalSeats());
		flight.setAvailableSeats(
				flightUpdateRequest.getAvailableSeats() != null ? flightUpdateRequest.getAvailableSeats()
						: flightUpdateRequest.getTotalSeats());
		flight.setAirline(airlineRepository.findById(flightUpdateRequest.getAirlineId())
				.orElseThrow(() -> new ResourceNotFoundException("Airline not found")));

		// Güncellenen entity veritabanına kaydedilir
		Flight updatedFlight = flightRepository.save(flight);
		// Güncellenen entity DTO'ya dönüştürülüp döndürülür
		return mapToResponse(updatedFlight);
	}

	public List<FlightResponse> getAllFlights() {
		// findAll ile tüm uçuşlar çekilir, map ile DTO'ya dönüştürülür
		return flightRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	public FlightResponse getFlightById(Long id) {
		Flight flight = flightRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
		return mapToResponse(flight);
	}

	public void deleteFlight(Long id) {
		Flight flight = flightRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
		flightRepository.delete(flight);
	}

}
