import client from './client';
import { CryptoAsset, CryptoFilter } from '../types';

function filterParams(filter: CryptoFilter) {
  return Object.fromEntries(
    Object.entries(filter).filter(([, value]) => value !== undefined && value !== null && value !== '')
  );
}

export async function fetchCryptos(filter: CryptoFilter = {}): Promise<CryptoAsset[]> {
  const { data } = await client.get<CryptoAsset[]>('/cryptos', {
    params: filterParams(filter)
  });
  return data;
}

export async function createCrypto(payload: Omit<CryptoAsset, 'id' | 'listedAt' | 'listed' | 'createdBy'>): Promise<CryptoAsset> {
  const body = {
    symbol: payload.symbol,
    name: payload.name,
    description: payload.description,
    category: payload.category,
    marketCap: payload.marketCap
  };
  const { data } = await client.post<CryptoAsset>('/cryptos', body);
  return data;
}

export async function deleteCrypto(id: string): Promise<void> {
  await client.delete(`/cryptos/${id}`);
}
