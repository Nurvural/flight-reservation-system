package com.flightreservation.flight.flight_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightreservation.flight.flight_service.dto.AirportDTO;
import com.flightreservation.flight.flight_service.entities.Airport;

@Mapper(componentModel = "spring")
public interface AirportMapper {
	
    @Mapping(source = "city.id", target = "cityId")
    AirportDTO toDTO(Airport airport);

    @Mapping(source = "cityId", target = "city.id")
    Airport toEntity(AirportDTO airportDTO);

    List<AirportDTO> toDTOList(List<Airport> airports);
}