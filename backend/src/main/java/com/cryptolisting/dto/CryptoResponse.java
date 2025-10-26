package com.cryptolisting.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CryptoResponse {

    private final UUID id;
    private final String symbol;
    private final String name;
    private final String description;
    private final String category;
    private final BigDecimal marketCap;
    private final OffsetDateTime listedAt;
    private final boolean listed;
    private final String createdBy;

    public CryptoResponse(UUID id,
                          String symbol,
                          String name,
                          String description,
                          String category,
                          BigDecimal marketCap,
                          OffsetDateTime listedAt,
                          boolean listed,
                          String createdBy) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.description = description;
        this.category = category;
        this.marketCap = marketCap;
        this.listedAt = listedAt;
        this.listed = listed;
        this.createdBy = createdBy;
    }

    public UUID getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public OffsetDateTime getListedAt() {
        return listedAt;
    }

    public boolean isListed() {
        return listed;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
