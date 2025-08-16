package com.flightreservation.flight.flight_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.flight_service.entities.Airline;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long>{
	boolean existsByCode(String code);
}
