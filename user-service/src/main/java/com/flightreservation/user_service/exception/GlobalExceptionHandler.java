package com.flightreservation.user_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

// @RestControllerAdvice -> tüm controller'lardan çıkan exception'ları yakalar
// @Slf4j -> Lombok ile loglama için logger (log.info, log.warn, log.error)
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Yardımcı method: ApiError nesnesi oluşturur.
    // req null olursa path/method null dönebilir — güvenlik için null-check yapıyoruz.
    private ApiError body(HttpServletRequest req, HttpStatus status, String message, String code) {
        String traceId = org.slf4j.MDC.get("traceId"); // CorrelationIdFilter ile set edilen id
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code(code)
                .message(message)
                .path(req != null ? req.getRequestURI() : null)
                .method(req != null ? req.getMethod() : null)
                .traceId(traceId)
                .build();
    }

    // --- 404: endpoint bulunamadı ---
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> noHandler(NoHandlerFoundException ex, HttpServletRequest req) {
        // Log: uyarı (WARNING) çünkü bu, yanlış URL/route kullanımıdır
        log.warn("[404] {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        ApiError err = body(req, HttpStatus.NOT_FOUND, "Endpoint not found", "ENDPOINT_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // --- 404: kaynak bulunamadı (domain) ---
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> notFound(ResourceNotFoundException ex, HttpServletRequest req) {
        // Log: uyarı — bu genelde kullanıcı hatası (yanlış id vs.)
        log.warn("[404] {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        ApiError err = body(req, HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // --- 400: iş kuralı ihlali ---
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> business(BusinessException ex, HttpServletRequest req) {
        // Log: uyarı — domain kuralı çiğnendi
        log.warn("[400] {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        ApiError err = body(req, HttpStatus.BAD_REQUEST, ex.getMessage(), "BUSINESS_RULE_VIOLATION");
        return ResponseEntity.badRequest().body(err);
    }

    // --- 400: @Valid body hataları ---
    // MethodArgumentNotValidException Spring tarafından otomatik atılır (DTO üzerindeki anotasyonlara uyulmazsa)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        // Alan bazlı hataları "field: mesaj" formatında birleştiriyoruz
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        // Log: kısa özet uyarı
        log.warn("[400] {} {} -> {}", req.getMethod(), req.getRequestURI(), msg);
        ApiError err = body(req, HttpStatus.BAD_REQUEST, msg, "VALIDATION_ERROR");
        return ResponseEntity.badRequest().body(err);
    }

    // --- 400: @Validated parametre (query/path) hataları ---
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> constraint(ConstraintViolationException ex, HttpServletRequest req) {
        String msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        log.warn("[400] {} {} -> {}", req.getMethod(), req.getRequestURI(), msg);
        ApiError err = body(req, HttpStatus.BAD_REQUEST, msg, "CONSTRAINT_VIOLATION");
        return ResponseEntity.badRequest().body(err);
    }

    // --- 400: tip/parsing hataları (parametre tipi yanlış veya body parse edilemedi) ---
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ApiError> badRequest(Exception ex, HttpServletRequest req) {
        log.warn("[400] {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        ApiError err = body(req, HttpStatus.BAD_REQUEST, "Bad request", "BAD_REQUEST");
        return ResponseEntity.badRequest().body(err);
    }

    // --- 405: HTTP method desteklenmiyor ---
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> methodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("[405] {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        ApiError err = body(req, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", "METHOD_NOT_ALLOWED");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
    }

    // --- 409: DB unique/constraint ihlali ---
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        // DB hatasının spesifik mesajını almak faydalı olabilir
        String dbMsg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        log.warn("[409] {} {} -> {}", req.getMethod(), req.getRequestURI(), dbMsg);
        ApiError err = body(req, HttpStatus.CONFLICT, "Data integrity violation", "DATA_INTEGRITY_VIOLATION");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    // --- 500: beklenmeyen tüm hatalar ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest req) {
        // Burada stack trace dahil tam log atıyoruz — prod'da bu kritik
        log.error("[500] {} {} -> {}", req != null ? req.getMethod() : "N/A",
                req != null ? req.getRequestURI() : "N/A", ex.getMessage(), ex);
        ApiError err = body(req, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", "UNEXPECTED_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
