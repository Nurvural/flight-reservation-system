package com.flightreservation.flight.flight_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.AirportDTO;
import com.flightreservation.flight.flight_service.entities.Airport;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.repositories.AirportRepository;
import com.flightreservation.flight.flight_service.repositories.CityRepository;

@Service
public class AirportService {

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    public AirportService(AirportRepository airportRepository, CityRepository cityRepository) {
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
    }

    private AirportDTO mapToDTO(Airport airport) {
        return AirportDTO.builder()
                .id(airport.getId())
                .name(airport.getName())
                .code(airport.getCode())
                .cityId(airport.getCity().getId())
                .build();
    }

    public AirportDTO createAirport(AirportDTO dto) {
        // İş kuralı: aynı kodlu havalimanı tekrar olamaz
        if (airportRepository.existsByCode(dto.getCode())) {
            throw new BusinessException("Airport code already exists: " + dto.getCode());
        }

        Airport airport = new Airport();
        airport.setName(dto.getName());
        airport.setCode(dto.getCode());
        airport.setCity(cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + dto.getCityId())));
        return mapToDTO(airportRepository.save(airport));
    }

    public AirportDTO updateAirport(Long id, AirportDTO dto) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));

        airport.setName(dto.getName());
        airport.setCode(dto.getCode());
        airport.setCity(cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + dto.getCityId())));
        return mapToDTO(airportRepository.save(airport));
    }

    public List<AirportDTO> getAllAirports() {
        return airportRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AirportDTO getAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
        return mapToDTO(airport);
    }

    public void deleteAirport(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
        airportRepository.delete(airport);
    }
}
