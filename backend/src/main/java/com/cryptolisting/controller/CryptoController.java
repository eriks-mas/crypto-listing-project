package com.cryptolisting.controller;

import com.cryptolisting.dto.CryptoFilterRequest;
import com.cryptolisting.dto.CryptoRequest;
import com.cryptolisting.dto.CryptoResponse;
import com.cryptolisting.service.CryptoService;
import com.cryptolisting.service.filter.CryptoFilterCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.net.URI;

@RestController
@RequestMapping("/api/cryptos")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping
    public ResponseEntity<List<CryptoResponse>> findAll(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String category,
                                                        @RequestParam(required = false) BigDecimal minMarketCap,
                                                        @RequestParam(required = false) BigDecimal maxMarketCap) {
        CryptoFilterCriteria criteria = CryptoFilterCriteria.builder()
                .withName(name)
                .withCategory(category)
                .withMinMarketCap(minMarketCap)
                .withMaxMarketCap(maxMarketCap)
                .build();
        return ResponseEntity.ok(cryptoService.findAll(criteria));
    }

    @PostMapping
    public ResponseEntity<CryptoResponse> create(@Valid @RequestBody CryptoRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        CryptoResponse response = cryptoService.create(request, userDetails.getUsername());
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.getId())
        .toUri();
    return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        cryptoService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
