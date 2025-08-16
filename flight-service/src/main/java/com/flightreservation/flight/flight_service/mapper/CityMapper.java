package com.flightreservation.flight.flight_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flightreservation.flight.flight_service.dto.CityDTO;
import com.flightreservation.flight.flight_service.entities.City;

@Mapper(componentModel = "spring") //mapper inject edildi.
public interface CityMapper {

	
	@Mapping(source = "country.id", target = "countryId")
	CityDTO toDTO(City city);
	
	City toEntity(CityDTO cityDTO);
}
