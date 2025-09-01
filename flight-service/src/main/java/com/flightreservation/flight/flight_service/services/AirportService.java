package com.flightreservation.flight.flight_service.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.AirportDTO;
import com.flightreservation.flight.flight_service.entities.Airport;
import com.flightreservation.flight.flight_service.entities.City;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.mapper.AirportMapper;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.repositories.AirportRepository;
import com.flightreservation.flight.flight_service.repositories.CityRepository;

@Service
public class AirportService {

	private final AirportRepository airportRepository;
	private final CityRepository cityRepository;
	private final AirportMapper airportMapper;

	public AirportService(AirportRepository airportRepository, CityRepository cityRepository,
			AirportMapper airportMapper) {
		this.airportRepository = airportRepository;
		this.cityRepository = cityRepository;
		this.airportMapper = airportMapper;
	}

	public AirportDTO createAirport(AirportDTO dto) {
		// İş kuralı: aynı kodlu havalimanı tekrar olamaz
		if (airportRepository.existsByCode(dto.getCode())) {
			throw new BusinessException("Airport code already exists: " + dto.getCode());
		}

		City city = cityRepository.findById(dto.getCityId())
				.orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + dto.getCityId()));

		Airport airport = airportMapper.toEntity(dto);
		airport.setCity(city);

		return airportMapper.toDTO(airportRepository.save(airport));
	}

	public AirportDTO updateAirport(Long id, AirportDTO dto) {
		Airport airport = airportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));

		City city = cityRepository.findById(dto.getCityId())
				.orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + dto.getCityId()));

		airport.setName(dto.getName());
		airport.setCode(dto.getCode());
		airport.setCity(city);

		return airportMapper.toDTO(airportRepository.save(airport));
	}

	public List<AirportDTO> getAllAirports() {
		return airportMapper.toDTOList(airportRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
	}

	public AirportDTO getAirportById(Long id) {
		Airport airport = airportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
		return airportMapper.toDTO(airport);
	}

	public void deleteAirport(Long id) {
		Airport airport = airportRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
		airportRepository.delete(airport);
	}
}
