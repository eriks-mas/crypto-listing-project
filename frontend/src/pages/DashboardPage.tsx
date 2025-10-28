import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createCrypto, deleteCrypto, fetchCryptos } from '../api/cryptoApi';
import { CryptoAsset } from '../types';
import CryptoForm from '../components/CryptoForm';
import CryptoList from '../components/CryptoList';

function DashboardPage() {
  const queryClient = useQueryClient();

  const { data, isLoading, isError } = useQuery<CryptoAsset[]>({
    queryKey: ['cryptos', 'dashboard'],
    queryFn: () => fetchCryptos()
  });

  const createMutation = useMutation({
    mutationFn: createCrypto,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cryptos'] });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: deleteCrypto,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cryptos'] });
    }
  });

  return (
    <section className="page">
      <h1>Manage your listings</h1>
      <CryptoForm
        onSubmit={async (values) => {
          await createMutation.mutateAsync(values);
        }}
      />
      {isLoading && <p>Loading your assets...</p>}
      {isError && <p>Unable to load assets.</p>}
      {data && (
        <CryptoList
          assets={data}
          canManage
          onDelete={(id) => {
            deleteMutation.mutate(id);
          }}
        />
      )}
    </section>
  );
}

export default DashboardPage;
