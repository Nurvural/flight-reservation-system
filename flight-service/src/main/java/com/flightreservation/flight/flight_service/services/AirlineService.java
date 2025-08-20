package com.flightreservation.flight.flight_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.AirlineDTO;
import com.flightreservation.flight.flight_service.entities.Airline;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.repositories.AirlineRepository;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    private AirlineDTO mapToDTO(Airline airline) {
        return AirlineDTO.builder()
                .id(airline.getId())
                .name(airline.getName())
                .code(airline.getCode())
                .build();
    }

    public AirlineDTO createAirline(AirlineDTO dto) {
        if (airlineRepository.existsByCode(dto.getCode())) {
            throw new BusinessException("Airline code already exists: " + dto.getCode());
        }

        Airline airline = new Airline();
        airline.setName(dto.getName());
        airline.setCode(dto.getCode());
        return mapToDTO(airlineRepository.save(airline));
    }

    public AirlineDTO updateAirline(Long id, AirlineDTO dto) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));

        airline.setName(dto.getName());
        airline.setCode(dto.getCode());
        return mapToDTO(airlineRepository.save(airline));
    }

    public List<AirlineDTO> getAllAirlines() {
        return airlineRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AirlineDTO getAirlineById(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
        return mapToDTO(airline);
    }

    public void deleteAirline(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
        airlineRepository.delete(airline);
    }
}
