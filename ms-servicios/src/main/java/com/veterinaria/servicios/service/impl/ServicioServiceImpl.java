package com.veterinaria.servicios.service.impl;

import com.veterinaria.servicios.dto.ServicioRequest;
import com.veterinaria.servicios.dto.ServicioResponse;
import com.veterinaria.servicios.entity.Servicio;
import com.veterinaria.servicios.exception.DuplicateResourceException;
import com.veterinaria.servicios.exception.ResourceNotFoundException;
import com.veterinaria.servicios.repository.ServicioRepository;
import com.veterinaria.servicios.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    @Override
    public List<ServicioResponse> listar() {
        return servicioRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ServicioResponse obtenerPorId(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
        return mapToResponse(servicio);
    }

    @Override
    public ServicioResponse crear(ServicioRequest request) {
        if (servicioRepository.existsByNombre(request.getNombre())) {
            throw new DuplicateResourceException("Ya existe un servicio con el nombre: " + request.getNombre());
        }
        Servicio servicio = Servicio.builder()
                .nombre(request.getNombre())
                .tipoServicio(request.getTipoServicio())
                .descripcion(request.getDescripcion())
                .valor(request.getValor())
                .duracionEstimada(request.getDuracionEstimada())
                .estado(request.getEstado())
                .build();
        return mapToResponse(servicioRepository.save(servicio));
    }

    @Override
    public ServicioResponse actualizar(Long id, ServicioRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
        servicio.setNombre(request.getNombre());
        servicio.setTipoServicio(request.getTipoServicio());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setValor(request.getValor());
        servicio.setDuracionEstimada(request.getDuracionEstimada());
        servicio.setEstado(request.getEstado());
        return mapToResponse(servicioRepository.save(servicio));
    }

    @Override
    public void eliminar(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con id: " + id));
        servicioRepository.delete(servicio);
    }

    private ServicioResponse mapToResponse(Servicio servicio) {
        return ServicioResponse.builder()
                .idServicio(servicio.getIdServicio())
                .nombre(servicio.getNombre())
                .tipoServicio(servicio.getTipoServicio())
                .descripcion(servicio.getDescripcion())
                .valor(servicio.getValor())
                .duracionEstimada(servicio.getDuracionEstimada())
                .estado(servicio.getEstado())
                .build();
    }
}
