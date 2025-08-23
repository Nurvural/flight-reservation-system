package com.flightreservation.flight.flight_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.flightreservation.flight.flight_service.dto.AirlineDTO;
import com.flightreservation.flight.flight_service.entities.Airline;

@Mapper(componentModel = "spring")
public interface AirlineMapper {

	AirlineDTO toDTO(Airline airline);

	Airline toEntity(AirlineDTO airlineDTO);

	List<AirlineDTO> toDTOList(List<Airline> airlines);

}
