package com.example.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "Product API", version = "1.0.0", description = "CRUD operations for Products")
)
@Configuration
public class OpenApiConfig {}
