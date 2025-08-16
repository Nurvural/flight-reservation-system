package com.flightreservation.flight.flight_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightreservation.flight.flight_service.dto.CityDTO;
import com.flightreservation.flight.flight_service.services.CityService;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    // Yeni şehir oluşturma
    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO dto) {
        return ResponseEntity.ok(cityService.createCity(dto));
    }

    // Şehir güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id, @RequestBody CityDTO dto) {
        return ResponseEntity.ok(cityService.updateCity(id, dto));
    }

    // Tüm şehirleri listeleme
    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    // ID ile şehir bulma
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    // Şehir silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
