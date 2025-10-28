import { ChangeEvent, FormEvent, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { login } from '../api/authApi';
import useAuth from '../hooks/useAuth';
import { AuthResponse } from '../types';

type FormState = {
  username: string;
  password: string;
};

interface LocationState {
  from?: {
    pathname: string;
  };
}

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const { login: setAuth } = useAuth();
  const [form, setForm] = useState<FormState>({ username: '', password: '' });
  const [error, setError] = useState<string | null>(null);

  const mutation = useMutation({
    mutationFn: () => login(form),
  onSuccess: (auth: AuthResponse) => {
      setAuth(auth);
      const target = (location.state as LocationState)?.from?.pathname ?? '/dashboard';
      navigate(target, { replace: true });
    },
    onError: () => {
      setError('Invalid credentials. Please try again.');
    }
  });

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);
    mutation.mutate();
  };

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
  setForm((current: FormState) => ({ ...current, [name]: value }));
  };

  return (
    <section className="auth-page">
      <h1>Login</h1>
      <form onSubmit={handleSubmit} className="auth-form">
        {error && <p className="auth-form__error">{error}</p>}
        <label>
          Username
          <input name="username" value={form.username} onChange={handleChange} required />
        </label>
        <label>
          Password
          <input name="password" type="password" value={form.password} onChange={handleChange} required />
        </label>
        <button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? 'Signing in...' : 'Login'}
        </button>
      </form>
    </section>
  );
}

export default LoginPage;
