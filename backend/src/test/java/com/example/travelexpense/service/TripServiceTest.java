package com.example.travelexpense.service;

import com.example.travelexpense.dto.TripRequestDTO;
import com.example.travelexpense.dto.TripResponseDTO;
import com.example.travelexpense.entity.Trip;
import com.example.travelexpense.exception.ResourceNotFoundException;
import com.example.travelexpense.repository.TripRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    @Test
    @DisplayName("Calculate total cost: travel + accommodation + food")
    void calculateTotalCost_shouldSumExpensesCorrectly() {
        BigDecimal travel = new BigDecimal("150.50");
        BigDecimal accommodation = new BigDecimal("320.25");
        BigDecimal food = new BigDecimal("80.25");

        BigDecimal total = tripService.calculateTotalCost(travel, accommodation, food);

        assertEquals(new BigDecimal("551.00"), total);
    }

    @Test
    @DisplayName("Calculate total cost: nulls treated as zero")
    void calculateTotalCost_shouldHandleNullsAsZero() {
        BigDecimal total = tripService.calculateTotalCost(null, new BigDecimal("100.00"), null);
        assertEquals(new BigDecimal("100.00"), total);
    }

    @Test
    @DisplayName("Calculate cost per traveller: rounded using HALF_UP")
    void calculateCostPerTraveller_shouldDivideAndRoundHalfUp() {
        // 100.00 / 3 = 33.3333... -> 33.33
        BigDecimal cost1 = tripService.calculateCostPerTraveller(new BigDecimal("100.00"), 3);
        assertEquals(new BigDecimal("33.33"), cost1);

        // 200.00 / 3 = 66.6666... -> 66.67
        BigDecimal cost2 = tripService.calculateCostPerTraveller(new BigDecimal("200.00"), 3);
        assertEquals(new BigDecimal("66.67"), cost2);

        // 100.00 / 6 = 16.6666... -> 16.67
        BigDecimal cost3 = tripService.calculateCostPerTraveller(new BigDecimal("100.00"), 6);
        assertEquals(new BigDecimal("16.67"), cost3);
    }

    @Test
    @DisplayName("Calculate cost per traveller: throws exception if travellers <= 0")
    void calculateCostPerTraveller_shouldThrowExceptionForZeroOrNegativeTravellers() {
        assertThrows(IllegalArgumentException.class, () ->
                tripService.calculateCostPerTraveller(new BigDecimal("100.00"), 0));

        assertThrows(IllegalArgumentException.class, () ->
                tripService.calculateCostPerTraveller(new BigDecimal("100.00"), -2));

        assertThrows(IllegalArgumentException.class, () ->
                tripService.calculateCostPerTraveller(new BigDecimal("100.00"), null));
    }

    @Test
    @DisplayName("Create trip: recalculates costs and saves to repository")
    void createTrip_shouldRecalculateCostsAndSave() {
        TripRequestDTO request = new TripRequestDTO(
                "Paris",
                5,
                2,
                new BigDecimal("200.00"),
                new BigDecimal("300.00"),
                new BigDecimal("100.00")
        );

        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        TripResponseDTO response = tripService.createTrip(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Paris", response.getDestination());
        assertEquals(new BigDecimal("600.00"), response.getTotalCost());
        assertEquals(new BigDecimal("300.00"), response.getCostPerTraveller());

        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    @DisplayName("Create trip: throws exception for blank destination")
    void createTrip_shouldThrowExceptionForBlankDestination() {
        TripRequestDTO request = new TripRequestDTO(
                "   ",
                3,
                1,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThrows(IllegalArgumentException.class, () -> tripService.createTrip(request));
        verify(tripRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create trip: throws exception for negative expenses")
    void createTrip_shouldThrowExceptionForNegativeExpenses() {
        TripRequestDTO request = new TripRequestDTO(
                "Tokyo",
                4,
                2,
                new BigDecimal("-50.00"),
                new BigDecimal("100.00"),
                new BigDecimal("100.00")
        );

        assertThrows(IllegalArgumentException.class, () -> tripService.createTrip(request));
        verify(tripRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create trip: throws exception for zero days")
    void createTrip_shouldThrowExceptionForZeroDays() {
        TripRequestDTO request = new TripRequestDTO(
                "Rome",
                0,
                2,
                new BigDecimal("50.00"),
                new BigDecimal("100.00"),
                new BigDecimal("100.00")
        );

        assertThrows(IllegalArgumentException.class, () -> tripService.createTrip(request));
        verify(tripRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get all trips: returns list of response DTOs")
    void getAllTrips_shouldReturnListOfDTOs() {
        Trip t1 = new Trip(1L, "London", 3, 1, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("50.00"), new BigDecimal("350.00"), new BigDecimal("350.00"));
        Trip t2 = new Trip(2L, "Berlin", 4, 2, new BigDecimal("80.00"), new BigDecimal("120.00"), new BigDecimal("40.00"), new BigDecimal("240.00"), new BigDecimal("120.00"));

        when(tripRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(t1, t2));

        List<TripResponseDTO> result = tripService.getAllTrips();

        assertEquals(2, result.size());
        assertEquals("London", result.get(0).getDestination());
        assertEquals("Berlin", result.get(1).getDestination());
    }

    @Test
    @DisplayName("Get trip by id: returns trip when found")
    void getTripById_shouldReturnTripWhenExists() {
        Trip t = new Trip(1L, "Madrid", 3, 1, new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("50.00"), new BigDecimal("350.00"), new BigDecimal("350.00"));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(t));

        TripResponseDTO result = tripService.getTripById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Madrid", result.getDestination());
    }

    @Test
    @DisplayName("Get trip by id: throws ResourceNotFoundException when missing")
    void getTripById_shouldThrowResourceNotFoundException() {
        when(tripRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(99L));
    }

    @Test
    @DisplayName("Update trip: recalculates totals and saves")
    void updateTrip_shouldRecalculateAndSave() {
        Trip existing = new Trip(1L, "Old Destination", 2, 1, new BigDecimal("50.00"), new BigDecimal("50.00"), new BigDecimal("50.00"), new BigDecimal("150.00"), new BigDecimal("150.00"));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripRequestDTO updateRequest = new TripRequestDTO(
                "New York",
                4,
                2,
                new BigDecimal("500.00"),
                new BigDecimal("600.00"),
                new BigDecimal("300.00")
        );

        TripResponseDTO updated = tripService.updateTrip(1L, updateRequest);

        assertNotNull(updated);
        assertEquals("New York", updated.getDestination());
        assertEquals(4, updated.getNumberOfDays());
        assertEquals(2, updated.getNumberOfTravellers());
        assertEquals(new BigDecimal("1400.00"), updated.getTotalCost());
        assertEquals(new BigDecimal("700.00"), updated.getCostPerTraveller());

        verify(tripRepository).save(existing);
    }

    @Test
    @DisplayName("Update trip: throws ResourceNotFoundException if missing")
    void updateTrip_shouldThrowResourceNotFoundException() {
        when(tripRepository.findById(99L)).thenReturn(Optional.empty());

        TripRequestDTO request = new TripRequestDTO("New York", 4, 2, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        assertThrows(ResourceNotFoundException.class, () -> tripService.updateTrip(99L, request));
    }

    @Test
    @DisplayName("Delete trip: deletes when exists")
    void deleteTrip_shouldDeleteWhenExists() {
        when(tripRepository.existsById(1L)).thenReturn(true);

        tripService.deleteTrip(1L);

        verify(tripRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Delete trip: throws ResourceNotFoundException when missing")
    void deleteTrip_shouldThrowResourceNotFoundException() {
        when(tripRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTrip(99L));
        verify(tripRepository, never()).deleteById(any());
    }
}
