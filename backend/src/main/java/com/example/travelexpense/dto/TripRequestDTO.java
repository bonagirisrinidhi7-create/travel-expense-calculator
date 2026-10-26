package com.example.travelexpense.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class TripRequestDTO {

    @NotBlank(message = "Destination is required and cannot be blank")
    @Size(max = 150, message = "Destination must not exceed 150 characters")
    private String destination;

    @NotNull(message = "Number of days is required")
    @Min(value = 1, message = "Number of days must be greater than zero")
    private Integer numberOfDays;

    @NotNull(message = "Number of travellers is required")
    @Min(value = 1, message = "Number of travellers must be greater than zero")
    private Integer numberOfTravellers;

    @NotNull(message = "Travel expense is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Travel expense cannot be negative")
    private BigDecimal travelExpense;

    @NotNull(message = "Accommodation expense is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Accommodation expense cannot be negative")
    private BigDecimal accommodationExpense;

    @NotNull(message = "Food expense is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Food expense cannot be negative")
    private BigDecimal foodExpense;

    public TripRequestDTO() {
    }

    public TripRequestDTO(String destination, Integer numberOfDays, Integer numberOfTravellers,
                          BigDecimal travelExpense, BigDecimal accommodationExpense, BigDecimal foodExpense) {
        this.destination = destination;
        this.numberOfDays = numberOfDays;
        this.numberOfTravellers = numberOfTravellers;
        this.travelExpense = travelExpense;
        this.accommodationExpense = accommodationExpense;
        this.foodExpense = foodExpense;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Integer getNumberOfTravellers() {
        return numberOfTravellers;
    }

    public void setNumberOfTravellers(Integer numberOfTravellers) {
        this.numberOfTravellers = numberOfTravellers;
    }

    public BigDecimal getTravelExpense() {
        return travelExpense;
    }

    public void setTravelExpense(BigDecimal travelExpense) {
        this.travelExpense = travelExpense;
    }

    public BigDecimal getAccommodationExpense() {
        return accommodationExpense;
    }

    public void setAccommodationExpense(BigDecimal accommodationExpense) {
        this.accommodationExpense = accommodationExpense;
    }

    public BigDecimal getFoodExpense() {
        return foodExpense;
    }

    public void setFoodExpense(BigDecimal foodExpense) {
        this.foodExpense = foodExpense;
    }
}
