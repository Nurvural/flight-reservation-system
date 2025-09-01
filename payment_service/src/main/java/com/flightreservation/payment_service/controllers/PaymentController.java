package com.flightreservation.payment_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightreservation.payment_service.dto.PaymentCreateRequest;
import com.flightreservation.payment_service.dto.PaymentResponse;
import com.flightreservation.payment_service.services.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    
    @PostMapping
    public Mono<ResponseEntity<PaymentResponse>> createPayment(@RequestBody @Valid PaymentCreateRequest request) {
        log.info("Received request to create payment for reservation id: {}", request.getReservationId());
        
        return paymentService.createPayment(request)
                .doOnSuccess(response -> log.info("Payment created successfully with id: {}", response.getId()))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        log.info("Received request to fetch all payments");
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        log.info("Received request to fetch payment with id: {}", id);
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.info("Received request to delete payment with id: {}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
