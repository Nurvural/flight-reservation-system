package com.flightreservation.flight.flight_service.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flightreservation.flight.flight_service.dto.CountryDTO;
import com.flightreservation.flight.flight_service.services.CountryService;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // Yeni ülke oluşturma
    @PostMapping
    public ResponseEntity<CountryDTO> createCountry(@RequestBody CountryDTO dto) {
        return ResponseEntity.ok(countryService.createCountry(dto));
    }

    // Ülke güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<CountryDTO> updateCountry(@PathVariable Long id, @RequestBody CountryDTO dto) {
        return ResponseEntity.ok(countryService.updateCountry(id, dto));
    }

    // Tüm ülkeleri listeleme
    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    // ID ile ülke bulma
    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    // Ülke silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}