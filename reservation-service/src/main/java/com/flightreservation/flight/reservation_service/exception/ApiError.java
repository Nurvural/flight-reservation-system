package com.flightreservation.flight.reservation_service.exception;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class ApiError {

	LocalDateTime timestamp; // Hata oluştuğu anın zamanı
	int status; // HTTP status kodu (404, 400, 500 vb.)
	String error; // HTTP reason phrase (Not Found, Bad Request vb.)
	String code; // domain kodu: FLIGHT_NOT_FOUND, DUPLICATE_AIRPORT_CODE...
	String message; // kullanıcıya gösterilecek mesaj
	String path; // /api/flights/1
	String method; // GET/POST...
	String traceId; // Correlation/trace id — log korelasyonu için MDC'den çekilir
}
