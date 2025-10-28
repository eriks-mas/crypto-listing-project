import axios, { InternalAxiosRequestConfig } from 'axios';

const client = axios.create({
  baseURL: '/api'
});

client.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const raw = localStorage.getItem('crypto-listing-auth');
  if (raw) {
    try {
      const { token } = JSON.parse(raw) as { token?: string };
      if (token) {
        config.headers = config.headers ?? {};
        config.headers.Authorization = `Bearer ${token}`;
      }
    } catch (error) {
      console.warn('Failed to parse auth token', error);
    }
  }
  return config;
});

export default client;
