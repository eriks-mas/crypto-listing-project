import { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { register } from '../api/authApi';
import useAuth from '../hooks/useAuth';
import { AuthResponse } from '../types';

type FormState = {
  username: string;
  password: string;
  confirmPassword: string;
};

function RegisterPage() {
  const navigate = useNavigate();
  const { login: setAuth } = useAuth();
  const [form, setForm] = useState<FormState>({ username: '', password: '', confirmPassword: '' });
  const [error, setError] = useState<string | null>(null);

  const mutation = useMutation({
    mutationFn: () => register({ username: form.username, password: form.password }),
    onSuccess: (auth: AuthResponse) => {
      setAuth(auth);
      navigate('/dashboard', { replace: true });
    },
    onError: () => setError('Registration failed. Please try again.')
  });

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    setError(null);
    mutation.mutate();
  };

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
  setForm((current: FormState) => ({ ...current, [name]: value }));
  };

  return (
    <section className="auth-page">
      <h1>Create an account</h1>
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
        <label>
          Confirm Password
          <input
            name="confirmPassword"
            type="password"
            value={form.confirmPassword}
            onChange={handleChange}
            required
          />
        </label>
        <button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? 'Registering...' : 'Register'}
        </button>
      </form>
    </section>
  );
}

export default RegisterPage;
