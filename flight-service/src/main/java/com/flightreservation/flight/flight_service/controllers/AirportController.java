package com.flightreservation.flight.flight_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightreservation.flight.flight_service.dto.AirportDTO;
import com.flightreservation.flight.flight_service.services.AirportService;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    // Yeni havaalanı ekleme
    @PostMapping
    public ResponseEntity<AirportDTO> createAirport(@RequestBody AirportDTO dto) {
        return ResponseEntity.ok(airportService.createAirport(dto));
    }

    // Havaalanı güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<AirportDTO> updateAirport(@PathVariable Long id, @RequestBody AirportDTO dto) {
        return ResponseEntity.ok(airportService.updateAirport(id, dto));
    }

    // Tüm havaalanlarını listeleme
    @GetMapping
    public ResponseEntity<List<AirportDTO>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    // ID ile havaalanı bulma
    @GetMapping("/{id}")
    public ResponseEntity<AirportDTO> getAirportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }

    // Havaalanı silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
