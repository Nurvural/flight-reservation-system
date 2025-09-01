package com.flightreservation.flight.reservation_service.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateRequest {
	
    @NotBlank(message = "Yolcu adı boş olamaz")
    @Size(min = 2, max = 50, message = "Yolcu adı 2 ile 50 karakter arasında olmalıdır")
    private String passengerName;

    @NotBlank(message = "E-posta boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String passengerEmail;

    @NotNull(message = "Uçuş ID boş olamaz")
    private Long flightId;

    private String specialRequests;

    @NotBlank(message = "Koltuk numarası boş olamaz")
    private String seatNumber;

    @NotNull(message = "Bagaj sayısı boş olamaz")
    @Min(value = 0, message = "Bagaj sayısı 0'dan küçük olamaz")
    private Integer baggageCount;
}
