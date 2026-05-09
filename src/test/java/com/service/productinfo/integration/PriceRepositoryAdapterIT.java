package com.service.productinfo.integration;

import com.service.productinfo.domain.model.Price;
import com.service.productinfo.infrastructure.adapter.out.persistence.adapter.PriceRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PriceRepositoryAdapterIT {

    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    @DisplayName("Should return applicable prices for a given date, product and brand")
    void shouldReturnApplicablePrices() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        List<Price> prices = priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

        assertThat(prices).isNotEmpty();
        assertThat(prices).allMatch(p -> p.productId().equals(productId));
        assertThat(prices).allMatch(p -> p.brandId().equals(brandId));
    }

    @Test
    @DisplayName("Should return empty list when no prices apply for given date")
    void shouldReturnEmptyListWhenNoPricesApply() {
        LocalDateTime applicationDate = LocalDateTime.of(2019, 1, 1, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        List<Price> prices = priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

        assertThat(prices).isEmpty();
    }

    @Test
    @DisplayName("Should return multiple prices when date ranges overlap")
    void shouldReturnMultiplePricesWhenRangesOverlap() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        List<Price> prices = priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

        assertThat(prices).hasSizeGreaterThan(1);
    }
}
