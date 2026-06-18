package com.veterinaria.profesionales.service;

import com.veterinaria.profesionales.dto.ProfesionalRequest;
import com.veterinaria.profesionales.dto.ProfesionalResponse;

import java.util.List;

public interface ProfesionalService {

    List<ProfesionalResponse> listar();

    ProfesionalResponse obtenerPorId(Long id);

    ProfesionalResponse crear(ProfesionalRequest request);

    ProfesionalResponse actualizar(Long id, ProfesionalRequest request);

    void eliminar(Long id);
}
