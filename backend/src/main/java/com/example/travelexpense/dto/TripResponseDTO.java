package com.example.travelexpense.dto;

import com.example.travelexpense.entity.Trip;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TripResponseDTO {

    private Long id;
    private String destination;
    private Integer numberOfDays;
    private Integer numberOfTravellers;
    private BigDecimal travelExpense;
    private BigDecimal accommodationExpense;
    private BigDecimal foodExpense;
    private BigDecimal totalCost;
    private BigDecimal costPerTraveller;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TripResponseDTO() {
    }

    public TripResponseDTO(Long id, String destination, Integer numberOfDays, Integer numberOfTravellers,
                           BigDecimal travelExpense, BigDecimal accommodationExpense, BigDecimal foodExpense,
                           BigDecimal totalCost, BigDecimal costPerTraveller, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = id;
        this.destination = destination;
        this.numberOfDays = numberOfDays;
        this.numberOfTravellers = numberOfTravellers;
        this.travelExpense = travelExpense;
        this.accommodationExpense = accommodationExpense;
        this.foodExpense = foodExpense;
        this.totalCost = totalCost;
        this.costPerTraveller = costPerTraveller;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TripResponseDTO fromEntity(Trip trip) {
        if (trip == null) {
            return null;
        }
        return new TripResponseDTO(
                trip.getId(),
                trip.getDestination(),
                trip.getNumberOfDays(),
                trip.getNumberOfTravellers(),
                trip.getTravelExpense(),
                trip.getAccommodationExpense(),
                trip.getFoodExpense(),
                trip.getTotalCost(),
                trip.getCostPerTraveller(),
                trip.getCreatedAt(),
                trip.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getCostPerTraveller() {
        return costPerTraveller;
    }

    public void setCostPerTraveller(BigDecimal costPerTraveller) {
        this.costPerTraveller = costPerTraveller;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
