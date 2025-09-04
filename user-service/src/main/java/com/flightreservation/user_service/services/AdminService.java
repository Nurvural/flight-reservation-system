package com.flightreservation.user_service.services;

import com.flightreservation.user_service.DTO.admin.AdminCreateRequest;
import com.flightreservation.user_service.DTO.admin.AdminUpdateRequest;
import com.flightreservation.user_service.DTO.admin.AdminResponse;
import com.flightreservation.user_service.entities.Admin;
import com.flightreservation.user_service.exception.BusinessException;
import com.flightreservation.user_service.exception.ResourceNotFoundException;
import com.flightreservation.user_service.mapper.AdminMapper;
import com.flightreservation.user_service.repositories.AdminRepository;
import com.flightreservation.user_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public List<AdminResponse> getAllAdmins() {
        log.info("Fetching all admins");
        List<AdminResponse> admins = adminRepository.findAll()
                .stream()
                .map(adminMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} admins", admins.size());
        return admins;
    }

    public AdminResponse createAdmin(AdminCreateRequest request) {
        log.info("Creating new admin with email: {}", request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Email already in use: {}", request.getEmail());
            throw new BusinessException("Email already in use");
        }

        if (request.getDepartment() == null || request.getDepartment().isBlank() ||
            request.getPosition() == null || request.getPosition().isBlank()) {
            log.warn("Department or position missing for email: {}", request.getEmail());
            throw new BusinessException("Department and position must be provided");
        }

        Admin admin = adminMapper.toEntity(request);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        Admin saved = adminRepository.save(admin);
        log.info("Admin created with id: {}", saved.getId());
      //  verificationService.generateVerificationToken(saved);
       // verificationService.sendVerificationEmail(saved);
        return adminMapper.toResponse(saved);
    }

    public AdminResponse updateAdmin(Long id, AdminUpdateRequest request) {
        log.info("Updating admin with id: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Admin not found with id: {}", id);
                    return new ResourceNotFoundException("Admin not found with id: " + id);
                });

        if (request.getDepartment() == null || request.getDepartment().isBlank() ||
            request.getPosition() == null || request.getPosition().isBlank()) {
            log.warn("Department or position missing for admin id: {}", id);
            throw new BusinessException("Department and position must be provided");
        }

        adminMapper.updateAdminFromRequest(request, admin);
        Admin updated = adminRepository.save(admin);
        log.info("Admin updated with id: {}", updated.getId());
        return adminMapper.toResponse(updated);
    }

  
}
