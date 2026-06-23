package com.veterinaria.categorias.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Categorias - API",
                version = "1.0",
                description = "Microservicio maestro del catalogo de categorias de productos",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
