package com.flightreservation.flight.flight_service.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.requestDTO.FlightCreateRequest;
import com.flightreservation.flight.flight_service.dto.requestDTO.FlightUpdateRequest;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightResponse;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightSearchCriteria;
import com.flightreservation.flight.flight_service.entities.Flight;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.mapper.FlightMapper;
import com.flightreservation.flight.flight_service.repositories.AirlineRepository;
import com.flightreservation.flight.flight_service.repositories.AirportRepository;
import com.flightreservation.flight.flight_service.repositories.FlightRepository;
import com.flightreservation.flight.flight_service.specifications.FlightSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FlightService {

	private final FlightRepository flightRepository;
	private final AirportRepository airportRepository;
	private final AirlineRepository airlineRepository;
	private final FlightMapper flightMapper;

	public FlightService(FlightRepository flightRepository, AirportRepository airportRepository,
			AirlineRepository airlineRepository, FlightMapper flightMapper) {
		this.flightRepository = flightRepository;
		this.airportRepository = airportRepository;
		this.airlineRepository = airlineRepository;
		this.flightMapper = flightMapper;
	}

	// Yeni uçuş oluşturma
	public FlightResponse createFlight(FlightCreateRequest flightCreateRequest) {
		log.info("Creating flight: {}", flightCreateRequest);

		// İş kuralı kontrolü: boş koltuk sayısı toplam koltuktan büyük olamaz
		if (flightCreateRequest.getAvailableSeats() != null
				&& flightCreateRequest.getAvailableSeats() > flightCreateRequest.getTotalSeats()) {
			log.warn("Available seats {} exceed total seats {}", flightCreateRequest.getAvailableSeats(),
					flightCreateRequest.getTotalSeats());
			throw new BusinessException("Available seats cannot exceed total seats");
		}

		Flight flight = flightMapper.toEntity(flightCreateRequest);

		flight.setDepartureAirport(
				airportRepository.findById(flightCreateRequest.getDepartureAirportId()).orElseThrow(() -> {
					log.error("Departure airport not found with id: {}", flightCreateRequest.getDepartureAirportId());
					return new ResourceNotFoundException("Departure airport not found");
				}));

		flight.setArrivalAirport(
				airportRepository.findById(flightCreateRequest.getArrivalAirportId()).orElseThrow(() -> {
					log.error("Arrival airport not found with id: {}", flightCreateRequest.getArrivalAirportId());
					return new ResourceNotFoundException("Arrival airport not found");
				}));

		flight.setAirline(airlineRepository.findById(flightCreateRequest.getAirlineId()).orElseThrow(() -> {
			log.error("Airline not found with id: {}", flightCreateRequest.getAirlineId());
			return new ResourceNotFoundException("Airline not found");
		}));

		Flight savedFlight = flightRepository.save(flight);
		log.info("Flight created with id: {}", savedFlight.getId());

		return flightMapper.toResponse(savedFlight);
	}

	// Mevcut uçuşu güncelleme
	public FlightResponse updateFlight(Long id, FlightUpdateRequest flightUpdateRequest) {
		log.info("Updating flight with id: {}", id);

		Flight existingFlight = flightRepository.findById(id).orElseThrow(() -> {
			log.error("Flight not found with id: {}", id);
			return new ResourceNotFoundException("Flight not found with id: " + id);
		});

		if (flightUpdateRequest.getAvailableSeats() != null
				&& flightUpdateRequest.getAvailableSeats() > flightUpdateRequest.getTotalSeats()) {
			log.warn("Available seats {} exceed total seats {}", flightUpdateRequest.getAvailableSeats(),
					flightUpdateRequest.getTotalSeats());
			throw new BusinessException("Available seats cannot exceed total seats");
		}

		Flight updatedFlight = flightMapper.toEntity(flightUpdateRequest);
		updatedFlight.setId(existingFlight.getId()); // Mevcut ID’yi koru

		updatedFlight.setDepartureAirport(
				airportRepository.findById(flightUpdateRequest.getDepartureAirportId()).orElseThrow(() -> {
					log.error("Departure airport not found with id: {}", flightUpdateRequest.getDepartureAirportId());
					return new ResourceNotFoundException("Departure airport not found");
				}));

		updatedFlight.setArrivalAirport(
				airportRepository.findById(flightUpdateRequest.getArrivalAirportId()).orElseThrow(() -> {
					log.error("Arrival airport not found with id: {}", flightUpdateRequest.getArrivalAirportId());
					return new ResourceNotFoundException("Arrival airport not found");
				}));

		updatedFlight.setAirline(airlineRepository.findById(flightUpdateRequest.getAirlineId()).orElseThrow(() -> {
			log.error("Airline not found with id: {}", flightUpdateRequest.getAirlineId());
			return new ResourceNotFoundException("Airline not found");
		}));

		Flight savedFlight = flightRepository.save(updatedFlight);
		log.info("Flight updated with id: {}", savedFlight.getId());
		return flightMapper.toResponse(savedFlight);
	}

	public List<FlightResponse> searchFlights(FlightSearchCriteria criteria) {
		// Specification oluştur
		var spec = FlightSpecification.getFlightsByCriteria(criteria);

		// Repository’den filtrelenmiş uçuşları al ve DTO’ya çevir
		return flightRepository.findAll(spec).stream().map(flightMapper::toResponse).collect(Collectors.toList());
	}

	public List<FlightResponse> findAllByDeletedFalse(Sort sort) {
		log.info("Fetching all flights");
		List<FlightResponse> flights = flightRepository.findAllByDeletedFalse(sort).stream()
				.map(flightMapper::toResponse).collect(Collectors.toList());
		log.info("Total flights found: {}", flights.size());
		return flights;
	}


	public FlightResponse getActiveFlightById(Long id) {
		log.info("Fetching flight with id: {}", id);
		Flight flight = flightRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
			log.error("Flight not found with id: {}", id);
			return new ResourceNotFoundException("Flight not found with id: " + id);
		});
		log.info("Flight fetched: {}", flight.getId());
		return flightMapper.toResponse(flight);
	}

	public void deleteFlight(Long id) {
		log.info("Soft deleting flight with id: {}", id);
		Flight flight = flightRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
			log.error("Flight not found with id: {}", id);
			return new ResourceNotFoundException("Flight not found with id: " + id);
		});
		flight.setDeleted(true);
		flightRepository.save(flight);
		log.info("Flight soft deleted with id: {}", id);
	}


}
