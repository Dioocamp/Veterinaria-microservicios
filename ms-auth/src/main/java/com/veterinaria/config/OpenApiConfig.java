package com.veterinaria.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Auth - API de Autenticacion",
                version = "1.0",
                description = "Emite tokens JWT con roles para acceder a traves del API Gateway",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
