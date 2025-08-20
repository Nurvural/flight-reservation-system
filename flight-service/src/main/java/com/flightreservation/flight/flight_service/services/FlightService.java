package com.flightreservation.flight.flight_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.requestDTO.FlightCreateRequest;
import com.flightreservation.flight.flight_service.dto.requestDTO.FlightUpdateRequest;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightResponse;
import com.flightreservation.flight.flight_service.entities.Flight;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.mapper.FlightMapper;
import com.flightreservation.flight.flight_service.repositories.AirlineRepository;
import com.flightreservation.flight.flight_service.repositories.AirportRepository;
import com.flightreservation.flight.flight_service.repositories.FlightRepository;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    private final FlightMapper flightMapper;

    public FlightService(FlightRepository flightRepository,
                         AirportRepository airportRepository,
                         AirlineRepository airlineRepository,
                         FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.airlineRepository = airlineRepository;
        this.flightMapper = flightMapper;
    }
	// Yeni uçuş oluşturma
    public FlightResponse createFlight(FlightCreateRequest flightCreateRequest) {

    	// İş kuralı kontrolü: boş koltuk sayısı toplam koltuktan büyük olamaz
        if (flightCreateRequest.getAvailableSeats() != null &&
            flightCreateRequest.getAvailableSeats() > flightCreateRequest.getTotalSeats()) {
            throw new BusinessException("Available seats cannot exceed total seats");
        }

        Flight flight = flightMapper.toEntity(flightCreateRequest);

        flight.setDepartureAirport(airportRepository.findById(flightCreateRequest.getDepartureAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found")));

        flight.setArrivalAirport(airportRepository.findById(flightCreateRequest.getArrivalAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found")));

        flight.setAirline(airlineRepository.findById(flightCreateRequest.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found")));

        Flight savedFlight = flightRepository.save(flight);

        return flightMapper.toResponse(savedFlight);
    }
	// Mevcut uçuşu güncelleme
    public FlightResponse updateFlight(Long id, FlightUpdateRequest flightUpdateRequest) {

        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        if (flightUpdateRequest.getAvailableSeats() != null &&
            flightUpdateRequest.getAvailableSeats() > flightUpdateRequest.getTotalSeats()) {
            throw new BusinessException("Available seats cannot exceed total seats");
        }

        Flight updatedFlight = flightMapper.toEntity(flightUpdateRequest);
        updatedFlight.setId(existingFlight.getId()); // Mevcut ID’yi koru

        updatedFlight.setDepartureAirport(airportRepository.findById(flightUpdateRequest.getDepartureAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found")));

        updatedFlight.setArrivalAirport(airportRepository.findById(flightUpdateRequest.getArrivalAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found")));

        updatedFlight.setAirline(airlineRepository.findById(flightUpdateRequest.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found")));

        Flight savedFlight = flightRepository.save(updatedFlight);

        return flightMapper.toResponse(savedFlight);
    }

    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(flightMapper::toResponse)
                .collect(Collectors.toList());
    }

    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return flightMapper.toResponse(flight);
    }

    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        flightRepository.delete(flight);
    }
}
