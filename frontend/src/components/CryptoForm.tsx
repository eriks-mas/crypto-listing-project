import { ChangeEvent, FormEvent, useState } from 'react';
import './CryptoForm.css';

interface CryptoFormValues {
  symbol: string;
  name: string;
  description: string;
  category: string;
  marketCap: number;
}

interface CryptoFormProps {
  onSubmit: (values: CryptoFormValues) => Promise<void> | void;
}

const initialValues: CryptoFormValues = {
  symbol: '',
  name: '',
  description: '',
  category: '',
  marketCap: 0
};

function CryptoForm({ onSubmit }: CryptoFormProps) {
  const [values, setValues] = useState<CryptoFormValues>(initialValues);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = event.target;
    setValues((current: CryptoFormValues) => ({
      ...current,
      [name]: name === 'marketCap' ? Number(value) : value
    }));
  };

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);
    setLoading(true);

    try {
      if (!values.symbol || !values.name || !values.category || values.marketCap <= 0) {
        throw new Error('All fields are required and market cap must be positive.');
      }
      await onSubmit(values);
      setValues(initialValues);
    } catch (submissionError) {
      setError((submissionError as Error).message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="crypto-form" onSubmit={handleSubmit}>
      <h2>Add a new cryptocurrency</h2>
      {error && <p className="crypto-form__error">{error}</p>}
      <div className="crypto-form__grid">
        <label>
          Symbol
          <input name="symbol" value={values.symbol} onChange={handleChange} maxLength={20} required />
        </label>
        <label>
          Name
          <input name="name" value={values.name} onChange={handleChange} maxLength={120} required />
        </label>
        <label>
          Category
          <input name="category" value={values.category} onChange={handleChange} maxLength={80} required />
        </label>
        <label>
          Market Cap (USD)
          <input
            name="marketCap"
            type="number"
            min="0"
            step="1000000"
            value={values.marketCap}
            onChange={handleChange}
            required
          />
        </label>
      </div>
      <label>
        Description
        <textarea name="description" value={values.description} onChange={handleChange} maxLength={512} />
      </label>
      <button type="submit" disabled={loading}>
        {loading ? 'Saving...' : 'Add Crypto'}
      </button>
    </form>
  );
}

export default CryptoForm;
