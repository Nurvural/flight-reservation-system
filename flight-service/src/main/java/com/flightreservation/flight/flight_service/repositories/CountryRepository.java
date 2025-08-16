package com.flightreservation.flight.flight_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.flight_service.entities.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>  {
	 boolean existsByCode(String code);
}
