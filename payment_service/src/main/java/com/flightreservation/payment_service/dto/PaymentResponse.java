package com.flightreservation.payment_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long reservationId;
    private Long flightId;
    private Double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;
}
