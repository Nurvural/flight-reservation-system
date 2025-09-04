package com.flightreservation.flight.reservation_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;

import com.flightreservation.flight.reservation_service.DTO.UserResponse;

import reactor.core.publisher.Mono;

@Component
public class UserWebClient {

	 private final WebClient webClient;
	 
	   public UserWebClient(WebClient.Builder webClientBuilder) {
	    	this.webClient = webClientBuilder.baseUrl("http://USER-SERVICE/api/users").build();
	    }
	   
	    public Mono<UserResponse> getUserById(Long userId, String token) {
	        return webClient.get()
	                .uri("/{id}", userId)
	                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
	                .retrieve()
	                .bodyToMono(UserResponse.class);
	    }
}
