package com.flightreservation.flight.flight_service.services;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flightreservation.flight.flight_service.dto.CityDTO;
import com.flightreservation.flight.flight_service.entities.City;
import com.flightreservation.flight.flight_service.exception.ResourceNotFoundException;
import com.flightreservation.flight.flight_service.mapper.CityMapper;
import com.flightreservation.flight.flight_service.exception.BusinessException;
import com.flightreservation.flight.flight_service.repositories.CityRepository;
import com.flightreservation.flight.flight_service.repositories.CountryRepository;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.cityMapper = cityMapper;
    }

  /*  private CityDTO mapToDTO(City city) {
        return CityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .countryId(city.getCountry().getId())
                .build();
    }*/

    @Transactional
    @CacheEvict(value = {"cities", "city"}, allEntries = true)
    public CityDTO createCity(CityDTO dto) {
        boolean exists = cityRepository.existsByNameAndCountryId(dto.getName(), dto.getCountryId());
        if (exists) {
            throw new BusinessException("City already exists in the given country");
        }

        var country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + dto.getCountryId()));

        City city = City.builder()
                .name(dto.getName())
                .country(country)
                .build();

        City savedCity = cityRepository.save(city);
        return cityMapper.toDTO(savedCity);
    }
    @CacheEvict(value = {"cities", "city"}, allEntries = true)
    public CityDTO updateCity(Long id, CityDTO dto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        city.setName(dto.getName());
        city.setCountry(countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + dto.getCountryId())));
        return cityMapper.toDTO(cityRepository.save(city)); 
    }
    @Cacheable(value = "cities")
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
        	    .stream()
        	    .map(cityMapper::toDTO)
        	    .collect(Collectors.toList());
    }
    public CityDTO getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        return cityMapper.toDTO(city);
    }
    @CacheEvict(value = {"cities", "city"}, allEntries = true)
    public void deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        cityRepository.delete(city);
    }
}
