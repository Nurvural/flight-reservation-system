package com.flightreservation.flight.flight_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.flight_service.entities.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>{

}
