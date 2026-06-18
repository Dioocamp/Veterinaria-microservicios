package com.veterinaria.categorias.service;

import com.veterinaria.categorias.dto.CategoriaRequest;
import com.veterinaria.categorias.dto.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> listar();

    CategoriaResponse obtenerPorId(Long id);

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse actualizar(Long id, CategoriaRequest request);

    void eliminar(Long id);
}
