package com.flightreservation.flight.reservation_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;

import com.flightreservation.flight.reservation_service.DTO.UserResponse;

import reactor.core.publisher.Mono;

@Component
public class UserWebClient {

	/* private final WebClient webClient;
	 
	   public UserWebClient(WebClient.Builder webClientBuilder) {
	    	this.webClient = webClientBuilder.baseUrl("http://USER-SERVICE/api/users").build();
	    }
	   
	    public Mono<UserResponse> getUserByEmail(String email) {
	        return webClient.get()
	                .uri(uriBuilder -> uriBuilder
	                        .path("/by-email")
	                        .queryParam("email", email)
	                        .build())
	                .header("X-User-Email", email) // Gateway üzerinden header geçiyor
	                .retrieve()
	                .bodyToMono(UserResponse.class);
	    }*/
}
