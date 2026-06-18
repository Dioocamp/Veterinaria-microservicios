package com.veterinaria.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @Size(max = 80, message = "El tipo de servicio no puede superar los 80 caracteres")
    private String tipoServicio;

    @Size(max = 300, message = "La descripcion no puede superar los 300 caracteres")
    private String descripcion;

    @NotNull(message = "El valor es obligatorio")
    @Positive(message = "El valor debe ser mayor a cero")
    private BigDecimal valor;

    @PositiveOrZero(message = "La duracion estimada no puede ser negativa")
    private Integer duracionEstimada;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
