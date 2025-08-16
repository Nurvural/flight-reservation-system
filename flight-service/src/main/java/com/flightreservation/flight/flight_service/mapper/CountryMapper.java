package com.flightreservation.flight.flight_service.mapper;

import org.mapstruct.Mapper;

import com.flightreservation.flight.flight_service.dto.CountryDTO;
import com.flightreservation.flight.flight_service.entities.Country;

@Mapper(componentModel = "spring") //mapper inject edildi.
public interface CountryMapper {

	  // CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class); 
	   //instance: tek bir nesne (singleton) olarak bu mapper’ı kullanmamıı sağlıyor
	   
	   // Entity → DTO dönüşümü
	    CountryDTO toDTO(Country country);

	    // DTO → Entity dönüşümü
	    Country toEntity(CountryDTO countryDTO);
}
