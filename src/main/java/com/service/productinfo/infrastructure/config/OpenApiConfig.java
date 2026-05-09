package com.service.productinfo.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Info API")
                        .version("1.0.0")
                        .description("REST API for querying applicable product prices by brand, product and date")
                        .contact(new Contact()
                                .name("Product Info Team")));
    }
}
