package com.example.travelexpense.controller;

import com.example.travelexpense.dto.TripRequestDTO;
import com.example.travelexpense.dto.TripResponseDTO;
import com.example.travelexpense.service.TripService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(@Valid @RequestBody TripRequestDTO requestDTO) {
        TripResponseDTO createdTrip = tripService.createTrip(requestDTO);
        return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {
        List<TripResponseDTO> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable Long id) {
        TripResponseDTO trip = tripService.getTripById(id);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponseDTO> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRequestDTO requestDTO) {
        TripResponseDTO updatedTrip = tripService.updateTrip(id, requestDTO);
        return ResponseEntity.ok(updatedTrip);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
