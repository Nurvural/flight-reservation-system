package com.flightreservation.payment_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.flightreservation.payment_service.dto.ReservationResponse;

import reactor.core.publisher.Mono;

@Component
public class ReservationWebClient {

    private final WebClient webClient;

    public ReservationWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://RESERVATION-SERVICE/api")
                .build();
    }

    // Reservation var mı yok mu kontrol
    public Mono<Boolean> checkReservationExists(Long reservationId) {
        return webClient.get()
                .uri("/reservations/{id}", reservationId)
                .retrieve()
                .toBodilessEntity()
                .map(entity -> true)
                .onErrorReturn(WebClientResponseException.NotFound.class, false);
    }

    // Reservation detaylarını al
    public Mono<ReservationResponse> getReservationById(Long reservationId) {
        return webClient.get()
                .uri("/reservations/{id}", reservationId)
                .retrieve()
                .bodyToMono(ReservationResponse.class);
                
    }
}