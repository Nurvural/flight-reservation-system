package com.flightreservation.flight.flight_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.flight_service.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
	   boolean existsByNameAndCountryId(String name, Long countryId);
}
