package com.example.travelexpense.controller;

import com.example.travelexpense.dto.TripRequestDTO;
import com.example.travelexpense.dto.TripResponseDTO;
import com.example.travelexpense.exception.GlobalExceptionHandler;
import com.example.travelexpense.exception.ResourceNotFoundException;
import com.example.travelexpense.service.TripService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
@Import(GlobalExceptionHandler.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TripService tripService;

    @Test
    @DisplayName("POST /api/trips: creates trip and returns 201 Created")
    void createTrip_validRequest_returns201() throws Exception {
        TripRequestDTO request = new TripRequestDTO(
                "Kyoto",
                7,
                2,
                new BigDecimal("500.00"),
                new BigDecimal("700.00"),
                new BigDecimal("300.00")
        );

        TripResponseDTO response = new TripResponseDTO(
                1L,
                "Kyoto",
                7,
                2,
                new BigDecimal("500.00"),
                new BigDecimal("700.00"),
                new BigDecimal("300.00"),
                new BigDecimal("1500.00"),
                new BigDecimal("750.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(tripService.createTrip(any(TripRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.destination").value("Kyoto"))
                .andExpect(jsonPath("$.totalCost").value(1500.00))
                .andExpect(jsonPath("$.costPerTraveller").value(750.00));
    }

    @Test
    @DisplayName("POST /api/trips: returns 400 Bad Request when validation fails")
    void createTrip_invalidRequest_returns400WithErrors() throws Exception {
        TripRequestDTO invalidRequest = new TripRequestDTO(
                "", // Blank destination
                0,  // Days <= 0
                -1, // Travellers <= 0
                new BigDecimal("-10.00"), // Negative expense
                new BigDecimal("100.00"),
                new BigDecimal("100.00")
        );

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.destination").exists())
                .andExpect(jsonPath("$.validationErrors.numberOfDays").exists())
                .andExpect(jsonPath("$.validationErrors.numberOfTravellers").exists())
                .andExpect(jsonPath("$.validationErrors.travelExpense").exists());
    }

    @Test
    @DisplayName("GET /api/trips: returns 200 OK and list of trips")
    void getAllTrips_returns200() throws Exception {
        TripResponseDTO response = new TripResponseDTO(
                1L,
                "Kyoto",
                7,
                2,
                new BigDecimal("500.00"),
                new BigDecimal("700.00"),
                new BigDecimal("300.00"),
                new BigDecimal("1500.00"),
                new BigDecimal("750.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(tripService.getAllTrips()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].destination").value("Kyoto"));
    }

    @Test
    @DisplayName("GET /api/trips/{id}: returns 200 OK when found")
    void getTripById_found_returns200() throws Exception {
        TripResponseDTO response = new TripResponseDTO(
                1L,
                "Kyoto",
                7,
                2,
                new BigDecimal("500.00"),
                new BigDecimal("700.00"),
                new BigDecimal("300.00"),
                new BigDecimal("1500.00"),
                new BigDecimal("750.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(tripService.getTripById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.destination").value("Kyoto"));
    }

    @Test
    @DisplayName("GET /api/trips/{id}: returns 404 Not Found when missing")
    void getTripById_notFound_returns404() throws Exception {
        when(tripService.getTripById(99L)).thenThrow(new ResourceNotFoundException("Trip not found with id: 99"));

        mockMvc.perform(get("/api/trips/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Trip not found with id: 99"));
    }

    @Test
    @DisplayName("PUT /api/trips/{id}: updates trip and returns 200 OK")
    void updateTrip_validRequest_returns200() throws Exception {
        TripRequestDTO request = new TripRequestDTO(
                "Kyoto Updated",
                8,
                2,
                new BigDecimal("600.00"),
                new BigDecimal("700.00"),
                new BigDecimal("300.00")
        );

        TripResponseDTO response = new TripResponseDTO(
                1L,
                "Kyoto Updated",
                8,
                2,
                new BigDecimal("600.00"),
                new BigDecimal("700.00"),
                new BigDecimal("300.00"),
                new BigDecimal("1600.00"),
                new BigDecimal("800.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(tripService.updateTrip(eq(1L), any(TripRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/trips/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.destination").value("Kyoto Updated"))
                .andExpect(jsonPath("$.totalCost").value(1600.00))
                .andExpect(jsonPath("$.costPerTraveller").value(800.00));
    }

    @Test
    @DisplayName("DELETE /api/trips/{id}: deletes trip and returns 204 No Content")
    void deleteTrip_found_returns204() throws Exception {
        doNothing().when(tripService).deleteTrip(1L);

        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/trips/{id}: returns 404 when missing")
    void deleteTrip_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Trip not found with id: 99")).when(tripService).deleteTrip(99L);

        mockMvc.perform(delete("/api/trips/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
