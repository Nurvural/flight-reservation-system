package com.flightreservation.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.flightreservation.user_service.DTO.admin.AdminCreateRequest;
import com.flightreservation.user_service.DTO.admin.AdminResponse;
import com.flightreservation.user_service.DTO.admin.AdminUpdateRequest;
import com.flightreservation.user_service.entities.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminResponse toResponse(Admin admin);

    Admin toEntity(AdminCreateRequest request);

    void updateAdminFromRequest(AdminUpdateRequest request, @MappingTarget Admin admin);
}
