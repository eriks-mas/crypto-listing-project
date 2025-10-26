package com.cryptolisting.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CryptoFilterRequest {

    @Size(max = 120)
    private String name;

    @Size(max = 80)
    private String category;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal minMarketCap;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal maxMarketCap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getMinMarketCap() {
        return minMarketCap;
    }

    public void setMinMarketCap(BigDecimal minMarketCap) {
        this.minMarketCap = minMarketCap;
    }

    public BigDecimal getMaxMarketCap() {
        return maxMarketCap;
    }

    public void setMaxMarketCap(BigDecimal maxMarketCap) {
        this.maxMarketCap = maxMarketCap;
    }
}
