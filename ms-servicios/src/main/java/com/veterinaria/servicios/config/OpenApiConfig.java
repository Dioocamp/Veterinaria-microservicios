package com.veterinaria.servicios.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Servicios - API",
                version = "1.0",
                description = "Microservicio del catalogo de servicios complementarios",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
