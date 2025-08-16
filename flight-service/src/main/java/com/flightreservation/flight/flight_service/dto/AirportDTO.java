package com.flightreservation.flight.flight_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportDTO {
    private Long id;
    private String name;
    private String code;
    private Long cityId;
}