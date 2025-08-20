package com.flightreservation.flight.flight_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.flight.flight_service.dto.CountryDTO;
import com.flightreservation.flight.flight_service.entities.Country;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.mapper.CountryMapper;
import com.flightreservation.flight.flight_service.repositories.CountryRepository;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }
   /* private CountryDTO  mapToResponse(Country country) {
        return CountryDTO .builder()
                .id(country.getId())
                .name(country.getName())
                .code(country.getCode())
                .build();
    }*/

    public CountryDTO createCountry(CountryDTO countryDTO) {
        if (countryRepository.existsByCode(countryDTO.getCode())) {
            throw new BusinessException("Country code already exists: " + countryDTO.getCode());
        }
        Country country = countryMapper.toEntity(countryDTO); // DTO → Entity
        Country saved = countryRepository.save(country);
        return countryMapper.toDTO(saved); // Entity → DTO
    }
    
    public CountryDTO updateCountry(Long id, CountryDTO countryDTO) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        country.setName(countryDTO.getName());
        country.setCode(countryDTO.getCode());
        return countryMapper.toDTO(countryRepository.save(country));
    }

    public List<CountryDTO > getAllCountries() {
        return countryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(countryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        return countryMapper.toDTO(country);
    }

    public void deleteCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        countryRepository.delete(country);
    }
}
