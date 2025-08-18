package com.flightreservation.flight.reservation_service.exception;

//İş kuralı ihlalleri (domain hataları) için özel exception.
public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}