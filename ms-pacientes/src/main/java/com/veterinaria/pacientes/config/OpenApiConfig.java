package com.veterinaria.pacientes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Pacientes - API",
                version = "1.0",
                description = "Gestion de pacientes (mascotas). Valida el tutor en ms-tutores via OpenFeign",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
