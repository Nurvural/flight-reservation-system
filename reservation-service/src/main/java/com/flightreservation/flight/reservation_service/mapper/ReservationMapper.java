package com.flightreservation.flight.reservation_service.mapper;



import org.mapstruct.Mapper;

import com.flightreservation.flight.reservation_service.DTO.ReservationCreateRequest;
import com.flightreservation.flight.reservation_service.DTO.ReservationResponse;
import com.flightreservation.flight.reservation_service.entities.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
	//ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

	Reservation toEntity(ReservationCreateRequest request);

	ReservationResponse toDTO(Reservation reservation);
}
