package com.example.travelexpense.service;

import com.example.travelexpense.dto.TripRequestDTO;
import com.example.travelexpense.dto.TripResponseDTO;
import java.math.BigDecimal;
import java.util.List;

public interface TripService {

    TripResponseDTO createTrip(TripRequestDTO requestDTO);

    List<TripResponseDTO> getAllTrips();

    TripResponseDTO getTripById(Long id);

    TripResponseDTO updateTrip(Long id, TripRequestDTO requestDTO);

    void deleteTrip(Long id);

    BigDecimal calculateTotalCost(BigDecimal travelExpense, BigDecimal accommodationExpense, BigDecimal foodExpense);

    BigDecimal calculateCostPerTraveller(BigDecimal totalCost, Integer numberOfTravellers);
}
