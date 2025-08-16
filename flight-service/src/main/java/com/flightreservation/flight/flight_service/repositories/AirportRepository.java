package com.flightreservation.flight.flight_service.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.flight_service.entities.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
	boolean existsByCode(String code);
}
