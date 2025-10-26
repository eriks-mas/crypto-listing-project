package com.cryptolisting.service.filter;

import com.cryptolisting.entity.CryptoAsset;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CryptoSpecifications {

    private CryptoSpecifications() {
    }

    public static Specification<CryptoAsset> fromCriteria(CryptoFilterCriteria criteria) {
        return (Root<CryptoAsset> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            criteria.name().ifPresent(name -> {
                String like = "%" + name.toLowerCase() + "%";
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("name")), like),
                        builder.like(builder.lower(root.get("symbol")), like)
                ));
            });

            criteria.category().ifPresent(category ->
                    predicates.add(builder.equal(builder.lower(root.get("category")), category.toLowerCase())));

            criteria.minMarketCap().ifPresent(min ->
                    predicates.add(builder.greaterThanOrEqualTo(root.get("marketCap"), min)));

            criteria.maxMarketCap().ifPresent(max ->
                    predicates.add(builder.lessThanOrEqualTo(root.get("marketCap"), max)));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
