package com.flightreservation.flight.flight_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDTO {
    private Long id;
    private String name;
    private String code;
}
