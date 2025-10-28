import client from './client';
import { AuthResponse } from '../types';

interface Credentials {
  username: string;
  password: string;
}

export async function login(credentials: Credentials): Promise<AuthResponse> {
  const { data } = await client.post<AuthResponse>('/auth/login', credentials);
  return data;
}

export async function register(credentials: Credentials): Promise<AuthResponse> {
  const { data } = await client.post<AuthResponse>('/auth/register', credentials);
  return data;
}
