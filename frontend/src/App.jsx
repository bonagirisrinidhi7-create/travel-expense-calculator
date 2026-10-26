import React, { useState, useEffect, useCallback } from 'react';
import Navbar from './components/Navbar';
import SummaryHeader from './components/SummaryHeader';
import TripForm from './components/TripForm';
import TripList from './components/TripList';
import NotificationToast from './components/NotificationToast';
import { tripApi } from './api/tripApi';

function App() {
  const [trips, setTrips] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editingTrip, setEditingTrip] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [deletingId, setDeletingId] = useState(null);
  const [notification, setNotification] = useState(null);

  const fetchTrips = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await tripApi.getAllTrips();
      setTrips(data || []);
    } catch (err) {
      console.error('Failed to fetch trips:', err);
      const msg =
        err.response?.data?.message ||
        (err.code === 'ERR_NETWORK'
          ? 'Unable to connect to Spring Boot backend at http://localhost:8080. Please ensure the backend is running.'
          : 'Failed to load trips from database.');
      setError(msg);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchTrips();
  }, [fetchTrips]);

  const showNotification = (type, message) => {
    setNotification({ type, message });
  };

  const handleSaveTrip = async (payload) => {
    setIsSubmitting(true);
    try {
      if (editingTrip) {
        const updated = await tripApi.updateTrip(editingTrip.id, payload);
        showNotification('success', `Trip to "${updated.destination}" updated successfully!`);
        setEditingTrip(null);
      } else {
        const created = await tripApi.createTrip(payload);
        showNotification('success', `Trip to "${created.destination}" added and calculated!`);
      }
      await fetchTrips();

      // Scroll smoothly to trips list if created
      const listElement = document.getElementById('trip-list-anchor');
      if (listElement) {
        listElement.scrollIntoView({ behavior: 'smooth' });
      }
    } catch (err) {
      console.error('Error saving trip:', err);
      let errMsg = 'Failed to save trip.';
      if (err.response?.data?.validationErrors) {
        const fieldErrors = Object.values(err.response.data.validationErrors).join(', ');
        errMsg = `Validation error: ${fieldErrors}`;
      } else if (err.response?.data?.message) {
        errMsg = err.response.data.message;
      } else if (err.code === 'ERR_NETWORK') {
        errMsg = 'Network error: Backend server unreachable.';
      }
      showNotification('error', errMsg);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleStartEdit = (trip) => {
    setEditingTrip(trip);
    const formElement = document.getElementById('trip-form-container');
    if (formElement) {
      formElement.scrollIntoView({ behavior: 'smooth' });
    }
  };

  const handleCancelEdit = () => {
    setEditingTrip(null);
  };

  const handleDeleteTrip = async (id, destination) => {
    const confirmed = window.confirm(`Are you sure you want to delete the trip to "${destination}"?`);
    if (!confirmed) return;

    setDeletingId(id);
    try {
      await tripApi.deleteTrip(id);
      showNotification('success', `Trip to "${destination}" deleted.`);
      if (editingTrip && editingTrip.id === id) {
        setEditingTrip(null);
      }
      await fetchTrips();
    } catch (err) {
      console.error('Failed to delete trip:', err);
      const errMsg = err.response?.data?.message || 'Failed to delete trip from database.';
      showNotification('error', errMsg);
    } finally {
      setDeletingId(null);
    }
  };

  const scrollToForm = () => {
    const formElement = document.getElementById('trip-form-container');
    if (formElement) {
      formElement.scrollIntoView({ behavior: 'smooth' });
    }
  };

  return (
    <div className="app-wrapper">
      <Navbar onScrollToForm={scrollToForm} />

      <NotificationToast
        notification={notification}
        onClose={() => setNotification(null)}
      />

      <main className="main-container">
        <SummaryHeader trips={trips} />

        <div className="content-grid">
          <TripForm
            currentTrip={editingTrip}
            onSave={handleSaveTrip}
            onCancelEdit={handleCancelEdit}
            isSubmitting={isSubmitting}
          />
        </div>

        <div id="trip-list-anchor">
          <TripList
            trips={trips}
            isLoading={isLoading}
            error={error}
            onEdit={handleStartEdit}
            onDelete={handleDeleteTrip}
            onRetry={fetchTrips}
            deletingId={deletingId}
          />
        </div>
      </main>

      <footer className="footer">
        <div className="footer-content">
          <p>© 2026 Travel Expense Calculator. Built with React, Vite, Spring Boot 3, and MySQL.</p>
        </div>
      </footer>
    </div>
  );
}

export default App;
