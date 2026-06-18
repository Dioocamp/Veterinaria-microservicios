package com.veterinaria.categorias.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {

    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private Boolean estado;
}
