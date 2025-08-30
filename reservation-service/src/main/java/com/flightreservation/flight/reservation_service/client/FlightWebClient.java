package com.flightreservation.flight.reservation_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FlightWebClient {


    private final WebClient webClient;

    public FlightWebClient(WebClient.Builder webClientBuilder) {
    	this.webClient = webClientBuilder.baseUrl("http://FLIGHT-SERVICE/api").build();
    }
    // Flight var mı yok mu kontrol (reactive)
    public Mono<Void> checkFlightExists(Long flightId) {
        return webClient.get()    // HTTP GET request
                .uri("/flights/{id}", flightId)
                .retrieve() // Response'u al
                .toBodilessEntity()    // Body'yi ignore et
                .then();   // Mono<Void> döndür
    }
    // Flight fiyatını al (reactive)
    public Mono<Double> getFlightPrice(Long flightId) {
        return webClient.get()
                .uri("/flights/{id}/price", flightId)
                .retrieve()
                .bodyToMono(Double.class);   // Body'yi Double'a çevir
    }
  
}
