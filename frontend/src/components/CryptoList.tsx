import { CryptoAsset } from '../types';
import './CryptoList.css';

interface CryptoListProps {
  assets: CryptoAsset[];
  canManage?: boolean;
  onDelete?: (id: string) => void;
}

const formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
  maximumFractionDigits: 0
});

function CryptoList({ assets, canManage = false, onDelete }: CryptoListProps) {
  if (!assets.length) {
    return <p>No cryptocurrencies found.</p>;
  }

  return (
    <table className="crypto-table">
      <thead>
        <tr>
          <th>Symbol</th>
          <th>Name</th>
          <th>Category</th>
          <th>Market Cap</th>
          <th>Owner</th>
          {canManage && <th>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {assets.map((asset) => (
          <tr key={asset.id}>
            <td>{asset.symbol}</td>
            <td>{asset.name}</td>
            <td>{asset.category}</td>
            <td>{formatter.format(asset.marketCap)}</td>
            <td>{asset.createdBy}</td>
            {canManage && (
              <td>
                <button type="button" onClick={() => onDelete?.(asset.id)}>
                  Delete
                </button>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default CryptoList;
