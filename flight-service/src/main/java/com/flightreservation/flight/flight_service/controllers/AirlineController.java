package com.flightreservation.flight.flight_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightreservation.flight.flight_service.dto.AirlineDTO;
import com.flightreservation.flight.flight_service.services.AirlineService;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    // Yeni havayolu ekleme
    @PostMapping
    public ResponseEntity<AirlineDTO> createAirline(@RequestBody AirlineDTO dto) {
        return ResponseEntity.ok(airlineService.createAirline(dto));
    }

    // Havayolu güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<AirlineDTO> updateAirline(@PathVariable Long id, @RequestBody AirlineDTO dto) {
        return ResponseEntity.ok(airlineService.updateAirline(id, dto));
    }

    // Tüm havayollarını listeleme
    @GetMapping
    public ResponseEntity<List<AirlineDTO>> getAllAirlines() {
        return ResponseEntity.ok(airlineService.getAllAirlines());
    }

    // ID ile havayolu bulma
    @GetMapping("/{id}")
    public ResponseEntity<AirlineDTO> getAirlineById(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.getAirlineById(id));
    }

    // Havayolu silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
