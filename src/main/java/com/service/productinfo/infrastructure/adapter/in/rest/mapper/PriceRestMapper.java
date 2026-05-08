package com.service.productinfo.infrastructure.adapter.in.rest.mapper;

import com.service.productinfo.domain.model.Price;
import com.service.productinfo.infrastructure.adapter.in.rest.dto.PriceResponse;
import org.springframework.stereotype.Component;

@Component
public class PriceRestMapper {

    public PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price()
        );
    }
}
