package com.veterinaria.productos.service;

import com.veterinaria.productos.dto.ProductoRequest;
import com.veterinaria.productos.dto.ProductoResponse;

import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listar();

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);
}
