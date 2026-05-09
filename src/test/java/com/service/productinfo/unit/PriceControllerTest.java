package com.service.productinfo.unit;

import com.service.productinfo.domain.exception.PriceNotFoundException;
import com.service.productinfo.domain.model.Price;
import com.service.productinfo.domain.port.in.FindApplicablePriceUseCase;
import com.service.productinfo.infrastructure.adapter.in.rest.PriceController;
import com.service.productinfo.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import com.service.productinfo.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
@Import({PriceRestMapper.class, GlobalExceptionHandler.class})
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindApplicablePriceUseCase findApplicablePriceUseCase;

    @Test
    @DisplayName("Should return 200 with valid price response when use case returns a price")
    void shouldReturn200WithValidPriceResponse() throws Exception {
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

        when(findApplicablePriceUseCase.execute(any(), any(), any())).thenReturn(price);

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Should return 404 when use case throws PriceNotFoundException")
    void shouldReturn404WhenPriceNotFound() throws Exception {
        when(findApplicablePriceUseCase.execute(any(), any(), any()))
                .thenThrow(new PriceNotFoundException(35455L, 1L, "2020-06-14T10:00:00"));

        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should return 400 when applicationDate parameter is missing")
    void shouldReturn400WhenApplicationDateIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return 400 when productId parameter is missing")
    void shouldReturn400WhenProductIdIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return 400 when brandId parameter is missing")
    void shouldReturn400WhenBrandIdIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
