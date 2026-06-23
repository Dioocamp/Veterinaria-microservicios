package com.veterinaria.productos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Productos - API",
                version = "1.0",
                description = "Inventario de productos. Valida la categoria en ms-categorias via OpenFeign",
                contact = @Contact(name = "Dinko Ocampo")
        )
)
public class OpenApiConfig {
}
