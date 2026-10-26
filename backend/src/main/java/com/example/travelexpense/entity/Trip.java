package com.example.travelexpense.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String destination;

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    @Column(name = "number_of_travellers", nullable = false)
    private Integer numberOfTravellers;

    @Column(name = "travel_expense", nullable = false, precision = 12, scale = 2)
    private BigDecimal travelExpense;

    @Column(name = "accommodation_expense", nullable = false, precision = 12, scale = 2)
    private BigDecimal accommodationExpense;

    @Column(name = "food_expense", nullable = false, precision = 12, scale = 2)
    private BigDecimal foodExpense;

    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "cost_per_traveller", nullable = false, precision = 12, scale = 2)
    private BigDecimal costPerTraveller;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Trip() {
    }

    public Trip(Long id, String destination, Integer numberOfDays, Integer numberOfTravellers,
                BigDecimal travelExpense, BigDecimal accommodationExpense, BigDecimal foodExpense,
                BigDecimal totalCost, BigDecimal costPerTraveller) {
        this.id = id;
        this.destination = destination;
        this.numberOfDays = numberOfDays;
        this.numberOfTravellers = numberOfTravellers;
        this.travelExpense = travelExpense;
        this.accommodationExpense = accommodationExpense;
        this.foodExpense = foodExpense;
        this.totalCost = totalCost;
        this.costPerTraveller = costPerTraveller;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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
