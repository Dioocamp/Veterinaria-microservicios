package com.veterinaria.especialidades.service;

import com.veterinaria.especialidades.dto.EspecialidadRequest;
import com.veterinaria.especialidades.dto.EspecialidadResponse;

import java.util.List;

public interface EspecialidadService {

    List<EspecialidadResponse> listar();

    EspecialidadResponse obtenerPorId(Long id);

    EspecialidadResponse crear(EspecialidadRequest request);

    EspecialidadResponse actualizar(Long id, EspecialidadRequest request);

    void eliminar(Long id);
}
