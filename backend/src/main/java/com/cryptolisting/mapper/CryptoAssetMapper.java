package com.cryptolisting.mapper;

import com.cryptolisting.dto.CryptoRequest;
import com.cryptolisting.dto.CryptoResponse;
import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.CryptoAsset;

import java.time.OffsetDateTime;

public final class CryptoAssetMapper {

    private CryptoAssetMapper() {
    }

    public static CryptoAsset toEntity(CryptoRequest request, AppUser user) {
        return new CryptoAsset(
                request.getSymbol().trim().toUpperCase(),
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getMarketCap(),
                OffsetDateTime.now(),
                true,
                user
        );
    }

    public static CryptoResponse toResponse(CryptoAsset asset) {
        return new CryptoResponse(
                asset.getId(),
                asset.getSymbol(),
                asset.getName(),
                asset.getDescription(),
                asset.getCategory(),
                asset.getMarketCap(),
                asset.getListedAt(),
                asset.isListed(),
                asset.getCreatedBy().getUsername()
        );
    }
}
