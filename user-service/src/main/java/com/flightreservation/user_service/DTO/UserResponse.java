package com.flightreservation.user_service.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flightreservation.user_service.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
	
	    private Long id;
	    private String name;
	    private String surname;
	    private String email;
	    private String phoneNumber;

	    // Customer alanları
	    private String passportNumber;
	    private LocalDate dateOfBirth;
	    private String nationality;
	    private Integer loyaltyPoints;

	    // Admin alanları
	    private String department;
	    private String position;

	    private Role role; // CUSTOMER veya ADMIN
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
}