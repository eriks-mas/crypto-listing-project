package com.cryptolisting.service;

import com.cryptolisting.dto.CryptoRequest;
import com.cryptolisting.dto.CryptoResponse;
import com.cryptolisting.service.filter.CryptoFilterCriteria;

import java.util.List;
import java.util.UUID;

public interface CryptoService {

    List<CryptoResponse> findAll(CryptoFilterCriteria criteria);

    CryptoResponse create(CryptoRequest request, String username);

    void delete(UUID id, String username);
}
