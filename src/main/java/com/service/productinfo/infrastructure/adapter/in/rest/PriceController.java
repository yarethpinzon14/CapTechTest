package com.service.productinfo.infrastructure.adapter.in.rest;

import com.service.productinfo.domain.port.in.FindApplicablePriceUseCase;
import com.service.productinfo.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.service.productinfo.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;
    private final PriceRestMapper priceRestMapper;

    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Long brandId
    ) {
        PriceResponse response = priceRestMapper.toResponse(
                findApplicablePriceUseCase.execute(applicationDate, productId, brandId)
        );
        return ResponseEntity.ok(response);
    }
}
