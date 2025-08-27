package com.flightreservation.user_service.DTO.customer;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flightreservation.user_service.entities.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    private String passportNumber;
    private LocalDate dateOfBirth;
    private String nationality;
    private Role role;
    private Integer loyaltyPoints;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
