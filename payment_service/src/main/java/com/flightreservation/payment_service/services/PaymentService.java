package com.flightreservation.payment_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flightreservation.payment_service.client.ReservationWebClient;
import com.flightreservation.payment_service.dto.PaymentCreateRequest;
import com.flightreservation.payment_service.dto.PaymentResponse;
import com.flightreservation.payment_service.entities.Payment;
import com.flightreservation.payment_service.exception.ResourceNotFoundException;
import com.flightreservation.payment_service.mapper.PaymentMapper;
import com.flightreservation.payment_service.repositories.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ReservationWebClient reservationClient;
    
    public Mono<PaymentResponse> createPayment(PaymentCreateRequest request) {
        return reservationClient.getReservationById(request.getReservationId())
                .doOnNext(reservation -> log.info("Reservation found: {}", reservation.getId()))
                .map(reservation -> {
                    // Payment oluştur
                    Payment payment = paymentMapper.toEntity(request);
                    payment.setPaymentDate(LocalDateTime.now());
                    payment.setFlightId(reservation.getFlightId()); 
                    payment.setAmount(reservation.getFlightPrice()); // Reservation'dan amount al
                    payment.setTransactionId(UUID.randomUUID().toString());
                    return payment;
                })
                .flatMap(payment -> Mono.fromCallable(() -> paymentRepository.save(payment)))
                .map(paymentMapper::toResponse)
                .doOnSuccess(response -> log.info("Payment created with id: {}", response.getId()))
                .doOnError(ex -> log.error("Error while creating payment: {}", ex.getMessage(), ex));
    }


    public List<PaymentResponse> getAllPayments() {
        log.info("Fetching all payments");
        List<PaymentResponse> payments = paymentRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Total payments fetched: {}", payments.size());
        return payments;
    }

    public PaymentResponse getPaymentById(Long id) {
        log.info("Fetching payment with id: {}", id);
        Payment payment = paymentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        log.info("Payment retrieved: {}", payment.getId());
        return paymentMapper.toResponse(payment);
    }

    public void deletePayment(Long id) {
        log.info("Soft deleting payment with id: {}", id);
        Payment payment = paymentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        payment.setDeleted(true);
        paymentRepository.save(payment);
        log.info("Payment soft deleted with id: {}", id);
    }
}
