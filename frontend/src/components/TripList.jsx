import React, { useState } from 'react';
import TripCard from './TripCard';

function TripList({ trips, isLoading, error, onEdit, onDelete, onRetry, deletingId }) {
  const [searchQuery, setSearchQuery] = useState('');

  const filteredTrips = trips.filter((trip) =>
    trip.destination.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <section className="trips-section">
      <div className="section-header">
        <div>
          <h2 className="section-title">Saved Itineraries & Trips</h2>
          <p className="section-subtitle">
            Browse through your saved itineraries with calculated totals and traveller splits
          </p>
        </div>

        {trips.length > 0 && (
          <div className="search-bar">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="11" cy="11" r="8"></circle>
              <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
            <input
              type="text"
              placeholder="Filter by destination..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            {searchQuery && (
              <button
                type="button"
                className="search-clear-btn"
                onClick={() => setSearchQuery('')}
                title="Clear search"
              >
                ✕
              </button>
            )}
          </div>
        )}
      </div>

      {/* Loading State */}
      {isLoading && (
        <div className="state-card loading-state">
          <div className="spinner-lg"></div>
          <h3>Retrieving Trips from Backend...</h3>
          <p>Connecting to MySQL database via Spring Boot REST API</p>
        </div>
      )}

      {/* Error State */}
      {!isLoading && error && (
        <div className="state-card error-state">
          <div className="error-icon">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10"></circle>
              <line x1="12" y1="8" x2="12" y2="12"></line>
              <line x1="12" y1="16" x2="12.01" y2="16"></line>
            </svg>
          </div>
          <h3>Failed to Load Trips</h3>
          <p className="error-message">{error}</p>
          {onRetry && (
            <button type="button" className="btn btn-primary" onClick={onRetry}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21.5 2v6h-6M21.34 15.57a10 10 0 1 1-.57-8.38l5.67-5.67"/>
              </svg>
              <span>Retry Connection</span>
            </button>
          )}
        </div>
      )}

      {/* Empty State: No Trips Saved Yet */}
      {!isLoading && !error && trips.length === 0 && (
        <div className="state-card empty-state">
          <div className="empty-illustration">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
              <path d="M17.8 19.2L16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.3c.4-.2.6-.6.5-1.1z"/>
            </svg>
          </div>
          <h3>No Trips Saved Yet</h3>
          <p>
            Start by adding your first trip above! Fill in your travel, accommodation, and food expenses to compute your cost breakdown.
          </p>
        </div>
      )}

      {/* No Search Match State */}
      {!isLoading && !error && trips.length > 0 && filteredTrips.length === 0 && (
        <div className="state-card empty-state">
          <h3>No Destinations Found</h3>
          <p>No saved trips matched "{searchQuery}". Try a different search keyword.</p>
          <button type="button" className="btn btn-outline" onClick={() => setSearchQuery('')}>
            View All Trips
          </button>
        </div>
      )}

      {/* Trips Grid */}
      {!isLoading && !error && filteredTrips.length > 0 && (
        <div className="trips-grid">
          {filteredTrips.map((trip) => (
            <TripCard
              key={trip.id}
              trip={trip}
              onEdit={onEdit}
              onDelete={onDelete}
              isDeleting={deletingId === trip.id}
            />
          ))}
        </div>
      )}
    </section>
  );
}

export default TripList;
