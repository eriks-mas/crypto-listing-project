import { ReactNode, createContext, useMemo, useState } from 'react';
import { AuthResponse } from '../types';

interface AuthState {
  token: string | null;
  username: string | null;
  roles: string[];
}

interface AuthContextValue extends AuthState {
  isAuthenticated: boolean;
  login: (auth: AuthResponse) => void;
  logout: () => void;
}

const STORAGE_KEY = 'crypto-listing-auth';

function readStoredState(): AuthState {
  if (typeof window === 'undefined') {
    return { token: null, username: null, roles: [] };
  }
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return { token: null, username: null, roles: [] };
    }
    const parsed = JSON.parse(raw) as AuthState;
    return {
      token: parsed.token ?? null,
      username: parsed.username ?? null,
      roles: parsed.roles ?? []
    };
  } catch (error) {
    console.warn('Failed to parse stored auth state', error);
    return { token: null, username: null, roles: [] };
  }
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [state, setState] = useState<AuthState>(() => readStoredState());

  const login = (auth: AuthResponse) => {
    const nextState: AuthState = {
      token: auth.token,
      username: auth.username,
      roles: auth.roles
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(nextState));
    setState(nextState);
  };

  const logout = () => {
    localStorage.removeItem(STORAGE_KEY);
    setState({ token: null, username: null, roles: [] });
  };

  const value = useMemo<AuthContextValue>(() => ({
    ...state,
    roles: state.roles ?? [],
    isAuthenticated: Boolean(state.token),
    login,
    logout
  }), [state]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
