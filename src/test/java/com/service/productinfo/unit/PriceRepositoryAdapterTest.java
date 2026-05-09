package com.service.productinfo.unit;

import com.service.productinfo.domain.model.Price;
import com.service.productinfo.infrastructure.adapter.out.persistence.adapter.PriceRepositoryAdapter;
import com.service.productinfo.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.service.productinfo.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterTest {

    @Mock
    private PriceJpaRepository priceJpaRepository;

    @InjectMocks
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    @DisplayName("Should return mapped domain prices when JPA repository returns entities")
    void shouldReturnMappedDomainPrices() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceEntity entity = buildPriceEntity(1, 0, new BigDecimal("35.50"));

        when(priceJpaRepository.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(entity));

        List<Price> result = priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).productId()).isEqualTo(productId);
        assertThat(result.get(0).brandId()).isEqualTo(brandId);
        assertThat(result.get(0).priceList()).isEqualTo(1);
        assertThat(result.get(0).price()).isEqualByComparingTo(new BigDecimal("35.50"));
        assertThat(result.get(0).currency()).isEqualTo("EUR");

        verify(priceJpaRepository).findApplicablePrices(applicationDate, productId, brandId);
    }

    @Test
    @DisplayName("Should return empty list when JPA repository returns no entities")
    void shouldReturnEmptyListWhenNoEntitiesFound() {
        LocalDateTime applicationDate = LocalDateTime.of(2019, 1, 1, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        when(priceJpaRepository.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(Collections.emptyList());

        List<Price> result = priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

        assertThat(result).isEmpty();
        verify(priceJpaRepository).findApplicablePrices(applicationDate, productId, brandId);
    }

    @Test
    @DisplayName("Should correctly map all fields from entity to domain model")
    void shouldCorrectlyMapAllFieldsFromEntityToDomain() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceEntity entity = PriceEntity.builder()
                .brandId(brandId)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(1)
                .productId(productId)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        when(priceJpaRepository.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(entity));

        List<Price> result = priceRepositoryAdapter.findApplicablePrices(applicationDate, productId, brandId);

        Price price = result.get(0);
        assertThat(price.brandId()).isEqualTo(brandId);
        assertThat(price.startDate()).isEqualTo(startDate);
        assertThat(price.endDate()).isEqualTo(endDate);
        assertThat(price.priceList()).isEqualTo(1);
        assertThat(price.productId()).isEqualTo(productId);
        assertThat(price.priority()).isEqualTo(0);
        assertThat(price.price()).isEqualByComparingTo(new BigDecimal("35.50"));
        assertThat(price.currency()).isEqualTo("EUR");
    }

    private PriceEntity buildPriceEntity(Integer priceList, Integer priority, BigDecimal price) {
        return PriceEntity.builder()
                .brandId(1L)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .priceList(priceList)
                .productId(35455L)
                .priority(priority)
                .price(price)
                .currency("EUR")
                .build();
    }
}
