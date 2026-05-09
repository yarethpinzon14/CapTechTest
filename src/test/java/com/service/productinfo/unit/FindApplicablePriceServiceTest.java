package com.service.productinfo.unit;

import com.service.productinfo.application.service.FindApplicablePriceService;
import com.service.productinfo.domain.exception.PriceNotFoundException;
import com.service.productinfo.domain.model.Price;
import com.service.productinfo.domain.port.out.PriceRepositoryPort;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindApplicablePriceServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private FindApplicablePriceService findApplicablePriceService;

    @Test
    @DisplayName("Should return price with highest priority when multiple prices apply")
    void shouldReturnPriceWithHighestPriority() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price lowPriority = buildPrice(1, 0, new BigDecimal("35.50"));
        Price highPriority = buildPrice(2, 1, new BigDecimal("25.45"));

        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(lowPriority, highPriority));

        Price result = findApplicablePriceService.execute(applicationDate, productId, brandId);

        assertThat(result.priority()).isEqualTo(1);
        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("25.45"));
        assertThat(result.priceList()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return single price when only one applies")
    void shouldReturnSingleApplicablePrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price price = buildPrice(1, 0, new BigDecimal("35.50"));

        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(List.of(price));

        Price result = findApplicablePriceService.execute(applicationDate, productId, brandId);

        assertThat(result.price()).isEqualByComparingTo(new BigDecimal("35.50"));
        assertThat(result.priceList()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should throw PriceNotFoundException when no prices found")
    void shouldThrowPriceNotFoundExceptionWhenNoPricesFound() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 13, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        when(priceRepositoryPort.findApplicablePrices(applicationDate, productId, brandId))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> findApplicablePriceService.execute(applicationDate, productId, brandId))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("35455")
                .hasMessageContaining("1");
    }

    private Price buildPrice(Integer priceList, Integer priority, BigDecimal price) {
        return new Price(
                1L,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                priceList,
                35455L,
                priority,
                price,
                "EUR"
        );
    }
}
