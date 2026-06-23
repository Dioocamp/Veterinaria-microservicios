package com.veterinaria.profesionales.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Profesionales - API",
                version = "1.0",
                description = "Gestion de profesionales. Valida la especialidad en ms-especialidades via OpenFeign",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
