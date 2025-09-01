package com.flightreservation.flight.flight_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightreservation.flight.flight_service.dto.requestDTO.FlightCreateRequest;
import com.flightreservation.flight.flight_service.dto.requestDTO.FlightUpdateRequest;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightResponse;
import com.flightreservation.flight.flight_service.dto.responseDTO.FlightSearchCriteria;
import com.flightreservation.flight.flight_service.entities.Flight;

@Mapper(componentModel = "spring")
public interface FlightMapper {

   // FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);
    // DTO → Entity
    Flight toEntity(FlightCreateRequest flightCreateRequest);

    Flight toEntity(FlightUpdateRequest flightUpdateRequest);
    
    // Flight → FlightSearchResponse

    @Mapping(source = "departureAirport.name", target = "departureAirportName")
    @Mapping(source = "arrivalAirport.name", target = "arrivalAirportName")
    @Mapping(source = "airline.name", target = "airlineName")
    FlightSearchCriteria toSearchResponse (Flight flight);

    // Entity → DTO
    @Mapping(source = "departureAirport.id", target = "departureAirportId")
    @Mapping(source = "arrivalAirport.id", target = "arrivalAirportId")
    @Mapping(source = "airline.id", target = "airlineId")
    FlightResponse toResponse(Flight flight);
}
