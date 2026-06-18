package com.veterinaria.productos.dto;

import com.veterinaria.productos.client.dto.CategoriaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private Long idProducto;
    private String nombre;
    private String categoria;
    private String marca;
    private String descripcion;
    private Integer stock;
    private BigDecimal precio;
    private Long idCategoria;
    private Boolean estado;
    private CategoriaDTO categoriaRegistrada;
}
