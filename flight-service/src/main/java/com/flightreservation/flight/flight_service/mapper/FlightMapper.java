package com.flightreservation.flight.flight_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.flightreservation.flight.flight_service.dto.requestDTO.FlightCreateRequest;
import com.flightreservation.flight.flight_service.dto.requestDTO.FlightUpdateRequest;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightResponse;
import com.flightreservation.flight.flight_service.entities.Flight;

@Mapper(componentModel = "spring")
public interface FlightMapper {

   // FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);
    // DTO → Entity
    Flight toEntity(FlightCreateRequest flightCreateRequest);

    Flight toEntity(FlightUpdateRequest flightUpdateRequest);

    // Entity → DTO
    @Mapping(source = "departureAirport.id", target = "departureAirportId")
    @Mapping(source = "arrivalAirport.id", target = "arrivalAirportId")
    @Mapping(source = "airline.id", target = "airlineId")
    FlightResponse toResponse(Flight flight);
}
