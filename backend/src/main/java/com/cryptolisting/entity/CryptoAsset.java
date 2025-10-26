package com.cryptolisting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "crypto_assets")
public class CryptoAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 512)
    private String description;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(name = "market_cap", nullable = false, precision = 19, scale = 2)
    private BigDecimal marketCap;

    @Column(name = "listed_at", nullable = false)
    private OffsetDateTime listedAt;

    @Column(name = "is_listed", nullable = false)
    private boolean listed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    protected CryptoAsset() {
        // JPA only
    }

    public CryptoAsset(String symbol,
                       String name,
                       String description,
                       String category,
                       BigDecimal marketCap,
                       OffsetDateTime listedAt,
                       boolean listed,
                       AppUser createdBy) {
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

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void updateDetails(String name,
                               String description,
                               String category,
                               BigDecimal marketCap,
                               boolean listed) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.marketCap = marketCap;
        this.listed = listed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CryptoAsset that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
