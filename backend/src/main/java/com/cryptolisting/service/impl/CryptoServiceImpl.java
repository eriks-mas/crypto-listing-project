package com.cryptolisting.service.impl;

import com.cryptolisting.dto.CryptoRequest;
import com.cryptolisting.dto.CryptoResponse;
import com.cryptolisting.entity.AppUser;
import com.cryptolisting.entity.CryptoAsset;
import com.cryptolisting.exception.DuplicateResourceException;
import com.cryptolisting.exception.ResourceNotFoundException;
import com.cryptolisting.exception.UnauthorizedOperationException;
import com.cryptolisting.mapper.CryptoAssetMapper;
import com.cryptolisting.repository.CryptoAssetRepository;
import com.cryptolisting.service.CryptoService;
import com.cryptolisting.service.UserService;
import com.cryptolisting.service.filter.CryptoFilterCriteria;
import com.cryptolisting.service.filter.CryptoSpecifications;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CryptoServiceImpl implements CryptoService {

    private final CryptoAssetRepository cryptoAssetRepository;
    private final UserService userService;

    public CryptoServiceImpl(CryptoAssetRepository cryptoAssetRepository, UserService userService) {
        this.cryptoAssetRepository = cryptoAssetRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<CryptoResponse> findAll(CryptoFilterCriteria criteria) {
        Specification<CryptoAsset> specification = CryptoSpecifications.fromCriteria(criteria);
        return cryptoAssetRepository.findAll(specification).stream()
                .map(CryptoAssetMapper::toResponse)
                .toList();
    }

    @Override
    public CryptoResponse create(CryptoRequest request, String username) {
        String normalizedSymbol = request.getSymbol().trim().toUpperCase();
        if (cryptoAssetRepository.existsBySymbolIgnoreCase(normalizedSymbol)) {
            throw new DuplicateResourceException("Crypto asset with symbol %s already exists".formatted(normalizedSymbol));
        }

        AppUser creator = userService.findByUsername(username);
        CryptoAsset cryptoAsset = CryptoAssetMapper.toEntity(request, creator);
        CryptoAsset saved = cryptoAssetRepository.save(cryptoAsset);
        return CryptoAssetMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id, String username) {
        CryptoAsset asset = cryptoAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crypto asset %s was not found".formatted(id)));

        if (!asset.getCreatedBy().getUsername().equalsIgnoreCase(username)) {
            throw new UnauthorizedOperationException("User %s cannot delete crypto %s".formatted(username, asset.getSymbol()));
        }

        cryptoAssetRepository.delete(asset);
    }
}
