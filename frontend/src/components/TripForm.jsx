import React, { useState, useEffect } from 'react';
import { formatCurrency } from '../api/tripApi';

const INITIAL_FORM_STATE = {
  destination: '',
  numberOfDays: 1,
  numberOfTravellers: 1,
  travelExpense: '',
  accommodationExpense: '',
  foodExpense: '',
};

function TripForm({ currentTrip, onSave, onCancelEdit, isSubmitting }) {
  const [formData, setFormData] = useState(INITIAL_FORM_STATE);
  const [errors, setErrors] = useState({});

  const resetForm = () => {
    setFormData(INITIAL_FORM_STATE);
    setErrors({});
  };

  useEffect(() => {
    if (currentTrip) {
      setFormData({
        destination: currentTrip.destination || '',
        numberOfDays: currentTrip.numberOfDays || 1,
        numberOfTravellers: currentTrip.numberOfTravellers || 1,
        travelExpense: currentTrip.travelExpense !== undefined ? currentTrip.travelExpense : '',
        accommodationExpense: currentTrip.accommodationExpense !== undefined ? currentTrip.accommodationExpense : '',
        foodExpense: currentTrip.foodExpense !== undefined ? currentTrip.foodExpense : '',
      });
      setErrors({});
    } else {
      setFormData(INITIAL_FORM_STATE);
      setErrors({});
    }
  }, [currentTrip]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    // Clear validation error on change
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  // Live calculation preview
  const travelNum = parseFloat(formData.travelExpense) || 0;
  const accomNum = parseFloat(formData.accommodationExpense) || 0;
  const foodNum = parseFloat(formData.foodExpense) || 0;
  const travellersNum = parseInt(formData.numberOfTravellers, 10) || 0;

  const liveTotalCost = (travelNum >= 0 ? travelNum : 0) + (accomNum >= 0 ? accomNum : 0) + (foodNum >= 0 ? foodNum : 0);
  const liveCostPerTraveller = travellersNum > 0 ? liveTotalCost / travellersNum : 0;

  const validate = () => {
    const newErrors = {};

    if (!formData.destination || !formData.destination.trim()) {
      newErrors.destination = 'Destination is required and cannot be blank.';
    } else if (formData.destination.trim().length > 150) {
      newErrors.destination = 'Destination must be at most 150 characters.';
    }

    const days = parseInt(formData.numberOfDays, 10);
    if (isNaN(days) || days <= 0) {
      newErrors.numberOfDays = 'Days must be greater than zero.';
    }

    const travellers = parseInt(formData.numberOfTravellers, 10);
    if (isNaN(travellers) || travellers <= 0) {
      newErrors.numberOfTravellers = 'Number of travellers must be greater than zero.';
    }

    const travel = parseFloat(formData.travelExpense);
    if (isNaN(travel)) {
      newErrors.travelExpense = 'Travel expense is required.';
    } else if (travel < 0) {
      newErrors.travelExpense = 'Travel expense cannot be negative.';
    }

    const accom = parseFloat(formData.accommodationExpense);
    if (isNaN(accom)) {
      newErrors.accommodationExpense = 'Accommodation expense is required.';
    } else if (accom < 0) {
      newErrors.accommodationExpense = 'Accommodation expense cannot be negative.';
    }

    const food = parseFloat(formData.foodExpense);
    if (isNaN(food)) {
      newErrors.foodExpense = 'Food expense is required.';
    } else if (food < 0) {
      newErrors.foodExpense = 'Food expense cannot be negative.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleManualCalculate = (e) => {
    e.preventDefault();
    if (validate()) {
      setCalculationCalculatedManually(true);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) {
      return;
    }

    const payload = {
      destination: formData.destination.trim(),
      numberOfDays: parseInt(formData.numberOfDays, 10),
      numberOfTravellers: parseInt(formData.numberOfTravellers, 10),
      travelExpense: parseFloat(formData.travelExpense).toFixed(2),
      accommodationExpense: parseFloat(formData.accommodationExpense).toFixed(2),
      foodExpense: parseFloat(formData.foodExpense).toFixed(2),
    };

    onSave(payload);
  };

  return (
    <div className="card form-card" id="trip-form-container">
      <div className="card-header">
        <div className="card-header-icon">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
          </svg>
        </div>
        <div>
          <h2 className="card-title">{currentTrip ? 'Edit Trip Expenses' : 'Calculate & Add New Trip'}</h2>
          <p className="card-description">
            {currentTrip
              ? 'Update trip parameters and recalculate itemized costs'
              : 'Enter trip itinerary details and expenses to calculate totals'}
          </p>
        </div>
      </div>

      <form onSubmit={handleSubmit} noValidate>
        <div className="form-grid">
          {/* Destination */}
          <div className="form-group full-width">
            <label htmlFor="destination">
              Destination <span className="required-star">*</span>
            </label>
            <div className="input-with-icon">
              <svg className="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                <circle cx="12" cy="10" r="3"></circle>
              </svg>
              <input
                id="destination"
                type="text"
                name="destination"
                placeholder="e.g. Manali, Goa, Paris, Tokyo"
                value={formData.destination}
                onChange={handleChange}
                className={errors.destination ? 'input-error' : ''}
              />
            </div>
            {errors.destination && <span className="field-error">{errors.destination}</span>}
          </div>

          {/* Number of Days */}
          <div className="form-group">
            <label htmlFor="numberOfDays">
              Duration (Days) <span className="required-star">*</span>
            </label>
            <div className="input-with-icon">
              <svg className="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                <line x1="16" y1="2" x2="16" y2="6"></line>
                <line x1="8" y1="2" x2="8" y2="6"></line>
                <line x1="3" y1="10" x2="21" y2="10"></line>
              </svg>
              <input
                id="numberOfDays"
                type="number"
                name="numberOfDays"
                min="1"
                placeholder="1"
                value={formData.numberOfDays}
                onChange={handleChange}
                className={errors.numberOfDays ? 'input-error' : ''}
              />
            </div>
            {errors.numberOfDays && <span className="field-error">{errors.numberOfDays}</span>}
          </div>

          {/* Number of Travellers */}
          <div className="form-group">
            <label htmlFor="numberOfTravellers">
              Number of Travellers <span className="required-star">*</span>
            </label>
            <div className="input-with-icon">
              <svg className="input-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                <circle cx="9" cy="7" r="4"></circle>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
              </svg>
              <input
                id="numberOfTravellers"
                type="number"
                name="numberOfTravellers"
                min="1"
                placeholder="1"
                value={formData.numberOfTravellers}
                onChange={handleChange}
                className={errors.numberOfTravellers ? 'input-error' : ''}
              />
            </div>
            {errors.numberOfTravellers && <span className="field-error">{errors.numberOfTravellers}</span>}
          </div>

          {/* Travel Expense */}
          <div className="form-group">
            <label htmlFor="travelExpense">
              Travel / Flight Expense (₹) <span className="required-star">*</span>
            </label>
            <div className="input-with-icon">
              <span className="currency-prefix">₹</span>
              <input
                id="travelExpense"
                type="number"
                step="0.01"
                min="0"
                name="travelExpense"
                placeholder="0.00"
                value={formData.travelExpense}
                onChange={handleChange}
                className={errors.travelExpense ? 'input-error' : ''}
              />
            </div>
            {errors.travelExpense && <span className="field-error">{errors.travelExpense}</span>}
          </div>

          {/* Accommodation Expense */}
          <div className="form-group">
            <label htmlFor="accommodationExpense">
              Accommodation / Hotel (₹) <span className="required-star">*</span>
            </label>
            <div className="input-with-icon">
              <span className="currency-prefix">₹</span>
              <input
                id="accommodationExpense"
                type="number"
                step="0.01"
                min="0"
                name="accommodationExpense"
                placeholder="0.00"
                value={formData.accommodationExpense}
                onChange={handleChange}
                className={errors.accommodationExpense ? 'input-error' : ''}
              />
            </div>
            {errors.accommodationExpense && <span className="field-error">{errors.accommodationExpense}</span>}
          </div>

          {/* Food Expense */}
          <div className="form-group full-width">
            <label htmlFor="foodExpense">
              Food & Dining Expense (₹) <span className="required-star">*</span>
            </label>
            <div className="input-with-icon">
              <span className="currency-prefix">₹</span>
              <input
                id="foodExpense"
                type="number"
                step="0.01"
                min="0"
                name="foodExpense"
                placeholder="0.00"
                value={formData.foodExpense}
                onChange={handleChange}
                className={errors.foodExpense ? 'input-error' : ''}
              />
            </div>
            {errors.foodExpense && <span className="field-error">{errors.foodExpense}</span>}
          </div>
        </div>

        {/* Live Calculation Preview Banner */}
        <div className="live-preview-box">
          <div className="preview-header">
            <span className="preview-tag">
              <span className="pulse-indicator"></span> Live Cost Estimation
            </span>
            <span className="preview-formula">
              Formula: Travel + Lodging + Food ÷ Travellers
            </span>
          </div>
          <div className="preview-metrics">
            <div className="preview-item">
              <span className="preview-label">Estimated Total Cost</span>
              <span className="preview-amount total-amount">
                {formatCurrency(liveTotalCost)}
              </span>
            </div>
            <div className="preview-divider"></div>
            <div className="preview-item">
              <span className="preview-label">Cost per Traveller ({travellersNum || 1} {travellersNum === 1 ? 'person' : 'people'})</span>
              <span className="preview-amount traveller-amount">
                {formatCurrency(liveCostPerTraveller)}
              </span>
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="form-actions">
          <button
            type="button"
            className="btn btn-secondary"
            onClick={handleManualCalculate}
            title="Preview calculation results"
          >
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <rect x="4" y="2" width="16" height="20" rx="2"></rect>
              <line x1="8" y1="6" x2="16" y2="6"></line>
              <line x1="16" y1="14" x2="16" y2="18"></line>
              <path d="M16 10h.01"></path>
              <path d="M12 10h.01"></path>
              <path d="M8 10h.01"></path>
              <path d="M12 14h.01"></path>
              <path d="M8 14h.01"></path>
              <path d="M12 18h.01"></path>
              <path d="M8 18h.01"></path>
            </svg>
            <span>Calculate</span>
          </button>

          <button
            type="submit"
            className="btn btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? (
              <>
                <span className="spinner-sm"></span>
                <span>Saving...</span>
              </>
            ) : (
              <>
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
                  <polyline points="17 21 17 13 7 13 7 21"></polyline>
                  <polyline points="7 3 7 8 15 8"></polyline>
                </svg>
                <span>{currentTrip ? 'Update Trip' : 'Save Trip'}</span>
              </>
            )}
          </button>

          {currentTrip && (
            <button
              type="button"
              className="btn btn-outline"
              onClick={onCancelEdit}
              disabled={isSubmitting}
            >
              Cancel Edit
            </button>
          )}

          {!currentTrip && (
            <button
              type="button"
              className="btn btn-ghost"
              onClick={resetForm}
              disabled={isSubmitting}
            >
              Clear
            </button>
          )}
        </div>
      </form>
    </div>
  );
}

export default TripForm;
