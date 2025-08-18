package com.flightreservation.flight.reservation_service.exception;

//Hata kodlarını tek yerde topladım
public enum ErrorCode {
    RESOURCE_NOT_FOUND,
    BUSINESS_RULE_VIOLATION,
    VALIDATION_ERROR,
    DATA_INTEGRITY_VIOLATION,
    ENDPOINT_NOT_FOUND,
    BAD_REQUEST,
    METHOD_NOT_ALLOWED,
    UNEXPECTED_ERROR
}
