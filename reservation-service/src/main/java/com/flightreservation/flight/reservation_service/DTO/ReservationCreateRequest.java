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
	

    private String passengerName;


    private String passengerEmail;

    @NotNull(message = "Uçuş ID boş olamaz")
    private Long flightId;
    
   private Long userId;
    
    private String specialRequests;

    @NotBlank(message = "Koltuk numarası boş olamaz")
    private String seatNumber;

    @NotNull(message = "Bagaj sayısı boş olamaz")
    @Min(value = 0, message = "Bagaj sayısı 0'dan küçük olamaz")
    private Integer baggageCount;
    
    public interface Guest {}
}
