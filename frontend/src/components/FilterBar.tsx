import { ChangeEvent, FormEvent, useState } from 'react';
import { CryptoFilter } from '../types';
import './FilterBar.css';

interface FilterBarProps {
  onFilter: (filter: CryptoFilter) => void;
}

const initialFilter: CryptoFilter = {
  name: '',
  category: '',
  minMarketCap: undefined,
  maxMarketCap: undefined
};

function FilterBar({ onFilter }: FilterBarProps) {
  const [filter, setFilter] = useState<CryptoFilter>(initialFilter);

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFilter((current: CryptoFilter) => ({
      ...current,
      [name]:
        value === ''
          ? undefined
          : name === 'minMarketCap' || name === 'maxMarketCap'
          ? Number(value)
          : value
    }));
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    onFilter(filter);
  };

  const handleReset = () => {
    const cleared: CryptoFilter = {};
    setFilter(cleared);
    onFilter(cleared);
  };

  return (
    <form className="filter-bar" onSubmit={handleSubmit}>
      <input
        name="name"
        placeholder="Search name or symbol"
        value={filter.name ?? ''}
        onChange={handleChange}
      />
      <input name="category" placeholder="Category" value={filter.category ?? ''} onChange={handleChange} />
      <input
        name="minMarketCap"
        type="number"
        placeholder="Min Market Cap"
        value={filter.minMarketCap ?? ''}
        onChange={handleChange}
      />
      <input
        name="maxMarketCap"
        type="number"
        placeholder="Max Market Cap"
        value={filter.maxMarketCap ?? ''}
        onChange={handleChange}
      />
      <button type="submit">Apply</button>
      <button type="button" onClick={handleReset}>
        Reset
      </button>
    </form>
  );
}

export default FilterBar;
