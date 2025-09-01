package com.flightreservation.flight.flight_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.flight_service.entities.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight>  {
	
	Optional<Flight> findByIdAndDeletedFalse(Long id);
	List<Flight> findAllByDeletedFalse(Sort sort);

}
