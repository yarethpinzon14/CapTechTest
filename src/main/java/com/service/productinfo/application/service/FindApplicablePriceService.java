package com.service.productinfo.application.service;

import com.service.productinfo.domain.exception.PriceNotFoundException;
import com.service.productinfo.domain.model.Price;
import com.service.productinfo.domain.port.in.FindApplicablePriceUseCase;
import com.service.productinfo.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindApplicablePriceService implements FindApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Price execute(LocalDateTime applicationDate, Long productId, Long brandId) {
        log.info("Finding applicable price for productId={}, brandId={}, date={}",
                productId, brandId, applicationDate);

        List<Price> applicablePrices = priceRepositoryPort
                .findApplicablePrices(applicationDate, productId, brandId);

        log.debug("Found {} applicable prices for productId={}, brandId={}",
                applicablePrices.size(), productId, brandId);

        return applicablePrices.stream()
                .max(Comparator.comparingInt(Price::priority))
                .orElseThrow(() -> {
                    log.warn("No applicable price found for productId={}, brandId={}, date={}",
                            productId, brandId, applicationDate);
                    return new PriceNotFoundException(productId, brandId, applicationDate.toString());
                });
    }
}
