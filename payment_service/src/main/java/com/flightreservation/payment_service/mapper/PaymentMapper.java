package com.flightreservation.payment_service.mapper;

import org.mapstruct.Mapper;

import com.flightreservation.payment_service.dto.PaymentCreateRequest;
import com.flightreservation.payment_service.dto.PaymentResponse;
import com.flightreservation.payment_service.entities.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {


    Payment toEntity(PaymentCreateRequest request);

    PaymentResponse toResponse(Payment payment);
    
}
