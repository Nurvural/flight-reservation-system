package com.flightreservation.user_service.DTO.admin;

import com.flightreservation.user_service.entities.Role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {

    private Long id;

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Role role;
    private String department;
    private String position;

}
