package com.veterinaria.servicios.service;

import com.veterinaria.servicios.dto.ServicioRequest;
import com.veterinaria.servicios.dto.ServicioResponse;

import java.util.List;

public interface ServicioService {

    List<ServicioResponse> listar();

    ServicioResponse obtenerPorId(Long id);

    ServicioResponse crear(ServicioRequest request);

    ServicioResponse actualizar(Long id, ServicioRequest request);

    void eliminar(Long id);
}
