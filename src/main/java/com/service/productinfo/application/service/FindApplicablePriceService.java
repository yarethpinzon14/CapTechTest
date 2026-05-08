package com.service.productinfo.application.service;

import com.service.productinfo.domain.exception.PriceNotFoundException;
import com.service.productinfo.domain.model.Price;
import com.service.productinfo.domain.port.in.FindApplicablePriceUseCase;
import com.service.productinfo.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindApplicablePriceService implements FindApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Price execute(LocalDateTime applicationDate, Long productId, Long brandId) {
        List<Price> applicablePrices = priceRepositoryPort
                .findApplicablePrices(applicationDate, productId, brandId);

        return applicablePrices.stream()
                .max(Comparator.comparingInt(Price::priority))
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId, applicationDate.toString()));
    }
}