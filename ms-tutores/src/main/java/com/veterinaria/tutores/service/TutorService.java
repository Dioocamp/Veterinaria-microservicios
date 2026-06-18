package com.veterinaria.tutores.service;

import com.veterinaria.tutores.dto.TutorRequest;
import com.veterinaria.tutores.dto.TutorResponse;

import java.util.List;

public interface TutorService {

    List<TutorResponse> listar();

    TutorResponse obtenerPorId(Long id);

    TutorResponse crear(TutorRequest request);

    TutorResponse actualizar(Long id, TutorRequest request);

    void eliminar(Long id);
}
