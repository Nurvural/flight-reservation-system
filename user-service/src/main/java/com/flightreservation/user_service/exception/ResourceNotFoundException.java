package com.flightreservation.user_service.exception;

//Basit bir unchecked exception — bulunamayan kaynaklar için kullan.
public class ResourceNotFoundException extends RuntimeException {
	// Mesajı doğrudan RuntimeException'a veriyoruz
    public ResourceNotFoundException(String message) { super(message); }
}