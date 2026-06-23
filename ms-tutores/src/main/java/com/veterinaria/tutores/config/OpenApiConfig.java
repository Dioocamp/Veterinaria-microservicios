package com.veterinaria.tutores.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Tutores - API",
                version = "1.0",
                description = "Microservicio maestro para la gestion de tutores (duenos de mascotas)",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
