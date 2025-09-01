package com.flightreservation.payment_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flightreservation.payment_service.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	

    List<Payment> findAllByDeletedFalse(Sort sort);

    Optional<Payment> findByIdAndDeletedFalse(Long id);

}
