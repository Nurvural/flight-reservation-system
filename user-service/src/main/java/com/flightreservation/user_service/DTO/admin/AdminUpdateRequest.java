package com.flightreservation.user_service.DTO.admin;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUpdateRequest {

    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotBlank(message = "Soyisim boş olamaz")
    private String surname;

    @NotBlank(message = "Telefon numarası boş olamaz")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Geçerli bir telefon numarası giriniz")
    private String phoneNumber;

    @NotBlank(message = "Departman boş olamaz")
    private String department;

    @NotBlank(message = "Pozisyon boş olamaz")
    private String position;
}
