package com.cryptolisting.repository;

import com.cryptolisting.entity.CryptoAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CryptoAssetRepository extends JpaRepository<CryptoAsset, UUID>, JpaSpecificationExecutor<CryptoAsset> {

    boolean existsBySymbolIgnoreCase(String symbol);

    Optional<CryptoAsset> findBySymbolIgnoreCase(String symbol);
}
