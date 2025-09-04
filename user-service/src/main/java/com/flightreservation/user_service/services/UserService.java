package com.flightreservation.user_service.services;

import com.flightreservation.user_service.DTO.UserResponse;
import com.flightreservation.user_service.entities.Admin;
import com.flightreservation.user_service.entities.Customer;
import com.flightreservation.user_service.entities.User;
import com.flightreservation.user_service.exception.ResourceNotFoundException;
import com.flightreservation.user_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		return mapToUserResponse(user);
	}

	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
	}

	public void deleteUser(Long id) {
		log.info("Soft deleting user with id: {}", id);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		user.setDeleted(true);
		userRepository.save(user);
		log.info("User soft deleted with id: {}", id);
	}

	private UserResponse mapToUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setName(user.getName());
		response.setSurname(user.getSurname());
		response.setEmail(user.getEmail());
		response.setPhoneNumber(user.getPhoneNumber());
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());
		response.setRole(user.getRole());

		if (user instanceof Customer customer) {
			response.setPassportNumber(customer.getPassportNumber());
			response.setDateOfBirth(customer.getDateOfBirth());
			response.setNationality(customer.getNationality());
			response.setLoyaltyPoints(customer.getLoyaltyPoints());
		} else if (user instanceof Admin admin) {
			response.setDepartment(admin.getDepartment());
			response.setPosition(admin.getPosition());
		}
		return response;
	}

}
