package com.flightreservation.flight.reservation_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.flight.reservation_service.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	List<Reservation> findAllByDeletedFalse(Sort sort);
	Optional<Reservation> findByIdAndDeletedFalse(Long id);

}
