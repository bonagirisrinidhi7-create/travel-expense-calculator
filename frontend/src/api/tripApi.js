import apiClient from './client';

export const tripApi = {
  getAllTrips: async () => {
    const response = await apiClient.get('/trips');
    return response.data;
  },

  getTripById: async (id) => {
    const response = await apiClient.get(`/trips/${id}`);
    return response.data;
  },

  createTrip: async (tripData) => {
    const response = await apiClient.post('/trips', tripData);
    return response.data;
  },

  updateTrip: async (id, tripData) => {
    const response = await apiClient.put(`/trips/${id}`, tripData);
    return response.data;
  },

  deleteTrip: async (id) => {
    const response = await apiClient.delete(`/trips/${id}`);
    return response.data;
  },
};

export const formatCurrency = (amount) => {
  const num = Number(amount) || 0;
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(num);
};
