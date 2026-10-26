import React, { useState } from 'react';
import { formatCurrency } from '../api/tripApi';

function TripCard({ trip, onEdit, onDelete, isDeleting }) {
  const [showDetails, setShowDetails] = useState(false);

  return (
    <div className="card trip-card">
      <div className="trip-card-header">
        <div className="destination-badge-group">
          <span className="destination-pill">{trip.destination}</span>
          <span className="duration-tag">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            {trip.numberOfDays} {trip.numberOfDays === 1 ? 'day' : 'days'}
          </span>
        </div>

        <div className="traveller-tag">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
            <circle cx="9" cy="7" r="4"></circle>
          </svg>
          {trip.numberOfTravellers} {trip.numberOfTravellers === 1 ? 'traveller' : 'travellers'}
        </div>
      </div>

      <div className="trip-cost-display">
        <div className="cost-main-box">
          <span className="cost-label">Total Trip Cost</span>
          <span className="cost-value-total">{formatCurrency(trip.totalCost)}</span>
        </div>
        <div className="cost-sub-box">
          <span className="cost-label">Cost per Traveller</span>
          <span className="cost-value-per-person">{formatCurrency(trip.costPerTraveller)}</span>
        </div>
      </div>

      {/* Itemized expense breakdown */}
      <div className="expense-pills">
        <div className="expense-pill">
          <span className="pill-dot pill-travel"></span>
          <span className="pill-name">Travel:</span>
          <span className="pill-amount">{formatCurrency(trip.travelExpense)}</span>
        </div>
        <div className="expense-pill">
          <span className="pill-dot pill-accom"></span>
          <span className="pill-name">Stay:</span>
          <span className="pill-amount">{formatCurrency(trip.accommodationExpense)}</span>
        </div>
        <div className="expense-pill">
          <span className="pill-dot pill-food"></span>
          <span className="pill-name">Food:</span>
          <span className="pill-amount">{formatCurrency(trip.foodExpense)}</span>
        </div>
      </div>

      {/* Expanded details section */}
      {showDetails && (
        <div className="trip-expanded-details">
          <h4>Trip Breakdown</h4>
          <div className="details-table">
            <div className="details-row">
              <span>Travel / Transit:</span>
              <strong>{formatCurrency(trip.travelExpense)}</strong>
            </div>
            <div className="details-row">
              <span>Accommodation:</span>
              <strong>{formatCurrency(trip.accommodationExpense)}</strong>
            </div>
            <div className="details-row">
              <span>Food & Drinks:</span>
              <strong>{formatCurrency(trip.foodExpense)}</strong>
            </div>
            <div className="details-row details-highlight">
              <span>Total Cost:</span>
              <strong>{formatCurrency(trip.totalCost)}</strong>
            </div>
            <div className="details-row details-highlight">
              <span>Split by {trip.numberOfTravellers} travellers:</span>
              <strong>{formatCurrency(trip.costPerTraveller)} / person</strong>
            </div>
          </div>
          {trip.createdAt && (
            <div className="details-meta">
              Saved on: {new Date(trip.createdAt).toLocaleDateString('en-IN', {
                day: 'numeric',
                month: 'short',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
              })}
            </div>
          )}
        </div>
      )}

      {/* Action Buttons */}
      <div className="card-actions">
        <button
          type="button"
          className="btn btn-sm btn-ghost"
          onClick={() => setShowDetails(!showDetails)}
          title="View detailed expense split"
        >
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="16" x2="12" y2="12"></line>
            <line x1="12" y1="8" x2="12.01" y2="8"></line>
          </svg>
          <span>{showDetails ? 'Hide Details' : 'View Details'}</span>
        </button>

        <div className="card-actions-right">
          <button
            type="button"
            className="btn btn-sm btn-edit"
            onClick={() => onEdit(trip)}
            title="Edit this trip"
          >
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
            </svg>
            <span>Edit</span>
          </button>

          <button
            type="button"
            className="btn btn-sm btn-delete"
            onClick={() => onDelete(trip.id, trip.destination)}
            disabled={isDeleting}
            title="Delete this trip"
          >
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <polyline points="3 6 5 6 21 6"></polyline>
              <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
            </svg>
            <span>Delete</span>
          </button>
        </div>
      </div>
    </div>
  );
}

export default TripCard;
