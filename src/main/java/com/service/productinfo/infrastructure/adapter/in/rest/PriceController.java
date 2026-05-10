package com.service.productinfo.infrastructure.adapter.in.rest;

import com.service.productinfo.domain.port.in.FindApplicablePriceUseCase;
import com.service.productinfo.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.service.productinfo.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Tag(name = "Prices", description = "Endpoint for querying applicable product prices")
public class PriceController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;
    private final PriceRestMapper priceRestMapper;

    @GetMapping
    @Operation(summary = "Get applicable price", description = "Returns the applicable price for a product given a brand and application date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applicable price found",
                    content = @Content(schema = @Schema(implementation = PriceResponse.class))),
            @ApiResponse(responseCode = "404", description = "No applicable price found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Missing or invalid parameters",
                    content = @Content)
    })
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @Parameter(description = "Application date in ISO format. Example: 2020-06-14T10:00:00")
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @Parameter(description = "Product identifier. Example: 35455")
            @RequestParam @NotNull @Positive Long productId,
            @Parameter(description = "Brand identifier. Example: 1 (ZARA)")
            @RequestParam @NotNull @Positive Long brandId
    ) {
        PriceResponse response = priceRestMapper.toResponse(
                findApplicablePriceUseCase.execute(applicationDate, productId, brandId)
        );
        return ResponseEntity.ok(response);
    }
}
