package com.flightreservation.user_service.DTO.admin;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCreateRequest {

    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotBlank(message = "Soyisim boş olamaz")
    private String surname;

    @Email(message = "Geçerli bir email giriniz")
    @NotBlank(message = "Email boş olamaz")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
    private String password;

    @NotBlank(message = "Telefon numarası boş olamaz")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Geçerli bir telefon numarası giriniz")
    private String phoneNumber;

    @NotBlank(message = "Departman boş olamaz")
    private String department;

    @NotBlank(message = "Pozisyon boş olamaz")
    private String position;
}
