package com.veterinaria.pacientes.service;

import com.veterinaria.pacientes.dto.PacienteRequest;
import com.veterinaria.pacientes.dto.PacienteResponse;

import java.util.List;

public interface PacienteService {

    List<PacienteResponse> listar();

    PacienteResponse obtenerPorId(Long id);

    PacienteResponse crear(PacienteRequest request);

    PacienteResponse actualizar(Long id, PacienteRequest request);

    void eliminar(Long id);
}
