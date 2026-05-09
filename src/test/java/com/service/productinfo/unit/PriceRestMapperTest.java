package com.service.productinfo.unit;

import com.service.productinfo.domain.model.Price;
import com.service.productinfo.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.service.productinfo.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PriceRestMapperTest {

    private PriceRestMapper priceRestMapper;

    @BeforeEach
    void setUp() {
        priceRestMapper = new PriceRestMapper();
    }

    @Test
    @DisplayName("Should correctly map Price domain to PriceResponse DTO")
    void shouldCorrectlyMapPriceToPriceResponse() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

        Price price = new Price(
                1L,
                startDate,
                endDate,
                1,
                35455L,
                0,
                new BigDecimal("35.50"),
                "EUR"
        );

        PriceResponse response = priceRestMapper.toResponse(price);

        assertThat(response.productId()).isEqualTo(35455L);
        assertThat(response.brandId()).isEqualTo(1L);
        assertThat(response.priceList()).isEqualTo(1);
        assertThat(response.startDate()).isEqualTo(startDate);
        assertThat(response.endDate()).isEqualTo(endDate);
        assertThat(response.price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    @DisplayName("Should not include priority or currency in PriceResponse")
    void shouldNotIncludePriorityOrCurrencyInResponse() {
        Price price = new Price(
                1L,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                1,
                35455L,
                0,
                new BigDecimal("35.50"),
                "EUR"
        );

        PriceResponse response = priceRestMapper.toResponse(price);

        assertThat(response).hasNoNullFieldsOrProperties();
        assertThat(response).isInstanceOf(PriceResponse.class);
    }
}
