package com.flightreservation.flight.flight_service.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.AirlineDTO;
import com.flightreservation.flight.flight_service.entities.Airline;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.mapper.AirlineMapper;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.repositories.AirlineRepository;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    public AirlineService(AirlineRepository airlineRepository, AirlineMapper airlineMapper) {
        this.airlineRepository = airlineRepository;
        this.airlineMapper = airlineMapper;
    }

    public AirlineDTO createAirline(AirlineDTO dto) {
        if (airlineRepository.existsByCode(dto.getCode())) {
            throw new BusinessException("Airline code already exists: " + dto.getCode());
        }

        Airline airline = airlineMapper.toEntity(dto);
        return airlineMapper.toDTO(airlineRepository.save(airline));
    }

    public AirlineDTO updateAirline(Long id, AirlineDTO dto) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));

        airline.setName(dto.getName());
        airline.setCode(dto.getCode());
        return airlineMapper.toDTO(airlineRepository.save(airline));
    }

    public List<AirlineDTO> getAllAirlines() {
        List<Airline> airlines = airlineRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return airlineMapper.toDTOList(airlines);
    }


    public AirlineDTO getAirlineById(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
        return airlineMapper.toDTO(airline);
    }

    public void deleteAirline(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
        airlineRepository.delete(airline);
    }
}
