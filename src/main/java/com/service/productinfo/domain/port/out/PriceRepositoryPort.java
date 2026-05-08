package com.service.productinfo.domain.port.out;

import com.service.productinfo.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    List<Price> findApplicablePrices(LocalDateTime applicationDate, Long productId, Long brandId);
}