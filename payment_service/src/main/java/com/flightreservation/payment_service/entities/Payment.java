package com.flightreservation.payment_service.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;
    
    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod  paymentMethod; 
    
    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;
    
    @Column(nullable = false)
    private String status; // Örn: PENDING, COMPLETED, FAILED

    @Column(name = "transaction_id", unique = true)
    private String transactionId;
    
}
