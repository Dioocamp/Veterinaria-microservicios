package com.veterinaria.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponse {

    private Long idServicio;
    private String nombre;
    private String tipoServicio;
    private String descripcion;
    private BigDecimal valor;
    private Integer duracionEstimada;
    private Boolean estado;
}
