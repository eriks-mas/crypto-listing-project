package com.cryptolisting.service.filter;

import java.math.BigDecimal;
import java.util.Optional;

public final class CryptoFilterCriteria {

    private final String name;
    private final String category;
    private final BigDecimal minMarketCap;
    private final BigDecimal maxMarketCap;

    private CryptoFilterCriteria(Builder builder) {
        this.name = normalize(builder.name);
        this.category = normalize(builder.category);
        this.minMarketCap = builder.minMarketCap;
        this.maxMarketCap = builder.maxMarketCap;
    }

    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    public Optional<String> category() {
        return Optional.ofNullable(category);
    }

    public Optional<BigDecimal> minMarketCap() {
        return Optional.ofNullable(minMarketCap);
    }

    public Optional<BigDecimal> maxMarketCap() {
        return Optional.ofNullable(maxMarketCap);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String category;
        private BigDecimal minMarketCap;
        private BigDecimal maxMarketCap;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withMinMarketCap(BigDecimal minMarketCap) {
            this.minMarketCap = minMarketCap;
            return this;
        }

        public Builder withMaxMarketCap(BigDecimal maxMarketCap) {
            this.maxMarketCap = maxMarketCap;
            return this;
        }

        public CryptoFilterCriteria build() {
            return new CryptoFilterCriteria(this);
        }
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
