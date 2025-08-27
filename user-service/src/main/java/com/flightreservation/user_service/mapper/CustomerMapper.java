package com.flightreservation.user_service.mapper;

import com.flightreservation.user_service.DTO.customer.CustomerCreateRequest;
import com.flightreservation.user_service.DTO.customer.CustomerUpdateRequest;
import com.flightreservation.user_service.DTO.customer.CustomerResponse;
import com.flightreservation.user_service.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Entity → Response DTO
    CustomerResponse toResponse(Customer customer);

    // Create DTO → Entity
    Customer toEntity(CustomerCreateRequest request);

    // Update DTO → Entity güncelle
    void updateCustomerFromRequest(CustomerUpdateRequest request, @MappingTarget Customer customer);
}
