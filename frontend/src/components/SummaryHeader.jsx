import React from 'react';
import { formatCurrency } from '../api/tripApi';

function SummaryHeader({ trips = [] }) {
  const totalTrips = trips.length;
  const totalSpending = trips.reduce((sum, trip) => sum + (Number(trip.totalCost) || 0), 0);
  const avgCostPerTrip = totalTrips > 0 ? totalSpending / totalTrips : 0;
  const totalTravellers = trips.reduce((sum, trip) => sum + (Number(trip.numberOfTravellers) || 0), 0);

  return (
    <header className="summary-section">
      <div className="dashboard-intro">
        <div className="badge-pill">Trip Financial Dashboard</div>
        <h1 className="dashboard-title">Smart Travel Expense Calculator</h1>
        <p className="dashboard-subtitle">
          Plan trips, calculate itemized travel expenses, and automatically compute accurate costs per traveller.
        </p>
      </div>

      <div className="metrics-grid">
        <div className="metric-card metric-primary">
          <div className="metric-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
            </svg>
          </div>
          <div className="metric-content">
            <span className="metric-label">Saved Trips</span>
            <span className="metric-value">{totalTrips}</span>
          </div>
        </div>

        <div className="metric-card metric-accent">
          <div className="metric-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10" />
              <path d="M16 8h-6a2 2 0 100 4h4a2 2 0 110 4H8" />
              <path d="M12 6v12" />
            </svg>
          </div>
          <div className="metric-content">
            <span className="metric-label">Total Cumulative Spend</span>
            <span className="metric-value">{formatCurrency(totalSpending)}</span>
          </div>
        </div>

        <div className="metric-card metric-purple">
          <div className="metric-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <rect x="2" y="7" width="20" height="14" rx="2" ry="2" />
              <path d="M16 21V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v16" />
            </svg>
          </div>
          <div className="metric-content">
            <span className="metric-label">Average Cost / Trip</span>
            <span className="metric-value">{formatCurrency(avgCostPerTrip)}</span>
          </div>
        </div>

        <div className="metric-card metric-indigo">
          <div className="metric-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2" />
              <circle cx="9" cy="7" r="4" />
              <path d="M23 21v-2a4 4 0 00-3-3.87" />
              <path d="M16 3.13a4 4 0 010 7.75" />
            </svg>
          </div>
          <div className="metric-content">
            <span className="metric-label">Total Travellers</span>
            <span className="metric-value">{totalTravellers}</span>
          </div>
        </div>
      </div>
    </header>
  );
}

export default SummaryHeader;
