package com.veterinaria.procedimientos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Procedimientos - API",
                version = "1.0",
                description = "Registro de procedimientos clinicos. Valida paciente y profesional via OpenFeign",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
