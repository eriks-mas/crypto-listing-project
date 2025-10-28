export interface AuthResponse {
  token: string;
  username: string;
  roles: string[];
}

export interface CryptoAsset {
  id: string;
  symbol: string;
  name: string;
  description: string;
  category: string;
  marketCap: number;
  listedAt: string;
  listed: boolean;
  createdBy: string;
}

export interface CryptoFilter {
  name?: string;
  category?: string;
  minMarketCap?: number;
  maxMarketCap?: number;
}
