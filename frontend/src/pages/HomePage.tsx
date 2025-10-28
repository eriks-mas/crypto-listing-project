import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchCryptos } from '../api/cryptoApi';
import { CryptoAsset, CryptoFilter } from '../types';
import CryptoList from '../components/CryptoList';
import FilterBar from '../components/FilterBar';

function HomePage() {
  const [filter, setFilter] = useState<CryptoFilter>({});

  const { data, isLoading, isError } = useQuery<CryptoAsset[]>({
    queryKey: ['cryptos', filter],
    queryFn: () => fetchCryptos(filter)
  });

  return (
    <section className="page">
      <h1>Trending cryptocurrencies</h1>
      <FilterBar onFilter={setFilter} />
      {isLoading && <p>Loading assets...</p>}
      {isError && <p>Failed to load cryptocurrencies.</p>}
      {data && <CryptoList assets={data} />}
    </section>
  );
}

export default HomePage;
