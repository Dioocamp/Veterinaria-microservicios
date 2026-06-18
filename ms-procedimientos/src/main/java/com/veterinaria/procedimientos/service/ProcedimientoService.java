package com.veterinaria.procedimientos.service;

import com.veterinaria.procedimientos.dto.ProcedimientoRequest;
import com.veterinaria.procedimientos.dto.ProcedimientoResponse;

import java.util.List;

public interface ProcedimientoService {

    List<ProcedimientoResponse> listar();

    ProcedimientoResponse obtenerPorId(Long id);

    ProcedimientoResponse crear(ProcedimientoRequest request);

    ProcedimientoResponse actualizar(Long id, ProcedimientoRequest request);

    void eliminar(Long id);
}
