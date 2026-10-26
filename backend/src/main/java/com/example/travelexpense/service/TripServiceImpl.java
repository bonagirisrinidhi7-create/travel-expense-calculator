package com.example.travelexpense.service;

import com.example.travelexpense.dto.TripRequestDTO;
import com.example.travelexpense.dto.TripResponseDTO;
import com.example.travelexpense.entity.Trip;
import com.example.travelexpense.exception.ResourceNotFoundException;
import com.example.travelexpense.repository.TripRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    public TripServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public TripResponseDTO createTrip(TripRequestDTO requestDTO) {
        validateTripRequest(requestDTO);

        BigDecimal totalCost = calculateTotalCost(
                requestDTO.getTravelExpense(),
                requestDTO.getAccommodationExpense(),
                requestDTO.getFoodExpense()
        );

        BigDecimal costPerTraveller = calculateCostPerTraveller(
                totalCost,
                requestDTO.getNumberOfTravellers()
        );

        Trip trip = new Trip();
        trip.setDestination(requestDTO.getDestination().trim());
        trip.setNumberOfDays(requestDTO.getNumberOfDays());
        trip.setNumberOfTravellers(requestDTO.getNumberOfTravellers());
        trip.setTravelExpense(requestDTO.getTravelExpense().setScale(2, RoundingMode.HALF_UP));
        trip.setAccommodationExpense(requestDTO.getAccommodationExpense().setScale(2, RoundingMode.HALF_UP));
        trip.setFoodExpense(requestDTO.getFoodExpense().setScale(2, RoundingMode.HALF_UP));
        trip.setTotalCost(totalCost);
        trip.setCostPerTraveller(costPerTraveller);

        Trip savedTrip = tripRepository.save(trip);
        return TripResponseDTO.fromEntity(savedTrip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponseDTO> getAllTrips() {
        return tripRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TripResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponseDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
        return TripResponseDTO.fromEntity(trip);
    }

    @Override
    public TripResponseDTO updateTrip(Long id, TripRequestDTO requestDTO) {
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        validateTripRequest(requestDTO);

        BigDecimal totalCost = calculateTotalCost(
                requestDTO.getTravelExpense(),
                requestDTO.getAccommodationExpense(),
                requestDTO.getFoodExpense()
        );

        BigDecimal costPerTraveller = calculateCostPerTraveller(
                totalCost,
                requestDTO.getNumberOfTravellers()
        );

        existingTrip.setDestination(requestDTO.getDestination().trim());
        existingTrip.setNumberOfDays(requestDTO.getNumberOfDays());
        existingTrip.setNumberOfTravellers(requestDTO.getNumberOfTravellers());
        existingTrip.setTravelExpense(requestDTO.getTravelExpense().setScale(2, RoundingMode.HALF_UP));
        existingTrip.setAccommodationExpense(requestDTO.getAccommodationExpense().setScale(2, RoundingMode.HALF_UP));
        existingTrip.setFoodExpense(requestDTO.getFoodExpense().setScale(2, RoundingMode.HALF_UP));
        existingTrip.setTotalCost(totalCost);
        existingTrip.setCostPerTraveller(costPerTraveller);

        Trip updatedTrip = tripRepository.save(existingTrip);
        return TripResponseDTO.fromEntity(updatedTrip);
    }

    @Override
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    }

    @Override
    public BigDecimal calculateTotalCost(BigDecimal travelExpense, BigDecimal accommodationExpense, BigDecimal foodExpense) {
        BigDecimal travel = travelExpense != null ? travelExpense : BigDecimal.ZERO;
        BigDecimal accommodation = accommodationExpense != null ? accommodationExpense : BigDecimal.ZERO;
        BigDecimal food = foodExpense != null ? foodExpense : BigDecimal.ZERO;

        return travel.add(accommodation).add(food).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateCostPerTraveller(BigDecimal totalCost, Integer numberOfTravellers) {
        if (numberOfTravellers == null || numberOfTravellers <= 0) {
            throw new IllegalArgumentException("Number of travellers must be greater than zero");
        }
        BigDecimal total = totalCost != null ? totalCost : BigDecimal.ZERO;
        return total.divide(BigDecimal.valueOf(numberOfTravellers), 2, RoundingMode.HALF_UP);
    }

    private void validateTripRequest(TripRequestDTO dto) {
        if (dto.getDestination() == null || dto.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be blank");
        }
        if (dto.getNumberOfDays() == null || dto.getNumberOfDays() <= 0) {
            throw new IllegalArgumentException("Number of days must be greater than zero");
        }
        if (dto.getNumberOfTravellers() == null || dto.getNumberOfTravellers() <= 0) {
            throw new IllegalArgumentException("Number of travellers must be greater than zero");
        }
        if (dto.getTravelExpense() == null || dto.getTravelExpense().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Travel expense cannot be negative");
        }
        if (dto.getAccommodationExpense() == null || dto.getAccommodationExpense().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Accommodation expense cannot be negative");
        }
        if (dto.getFoodExpense() == null || dto.getFoodExpense().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Food expense cannot be negative");
        }
    }
}
