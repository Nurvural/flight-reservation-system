package com.flightreservation.payment_service.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateRequest {

    private Long reservationId;
    private String paymentMethod;


}
