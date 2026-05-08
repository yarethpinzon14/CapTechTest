package com.service.productinfo.domain.port.in;

import com.service.productinfo.domain.model.Price;

import java.time.LocalDateTime;

public interface FindApplicablePriceUseCase {

    Price execute(LocalDateTime applicationDate, Long productId, Long brandId);
}