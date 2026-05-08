package com.service.productinfo.infrastructure.adapter.out.persistence.adapter;

import com.service.productinfo.domain.model.Price;
import com.service.productinfo.domain.port.out.PriceRepositoryPort;
import com.service.productinfo.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.service.productinfo.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository priceJpaRepository;

    @Override
    public List<Price> findApplicablePrices(LocalDateTime applicationDate, Long productId, Long brandId) {
        return priceJpaRepository
                .findApplicablePrices(applicationDate, productId, brandId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}
