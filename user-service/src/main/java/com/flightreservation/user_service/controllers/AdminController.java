package com.flightreservation.user_service.controllers;

import com.flightreservation.user_service.DTO.admin.AdminCreateRequest;
import com.flightreservation.user_service.DTO.admin.AdminUpdateRequest;
import com.flightreservation.user_service.DTO.admin.AdminResponse;
import com.flightreservation.user_service.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Tüm adminleri getir
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // ID ile admin getir
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        AdminResponse admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }

    // Yeni admin oluştur
    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminCreateRequest request) {
        AdminResponse createdAdmin = adminService.createAdmin(request);
        return ResponseEntity.ok(createdAdmin);
    }

    // Mevcut admini güncelle
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Long id,
                                                     @Valid @RequestBody AdminUpdateRequest request) {
        AdminResponse updatedAdmin = adminService.updateAdmin(id, request);
        return ResponseEntity.ok(updatedAdmin);
    }

    // Admin sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
