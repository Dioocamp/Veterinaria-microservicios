package com.veterinaria.especialidades.service.impl;

import com.veterinaria.especialidades.dto.EspecialidadRequest;
import com.veterinaria.especialidades.dto.EspecialidadResponse;
import com.veterinaria.especialidades.entity.Especialidad;
import com.veterinaria.especialidades.exception.DuplicateResourceException;
import com.veterinaria.especialidades.exception.ResourceNotFoundException;
import com.veterinaria.especialidades.repository.EspecialidadRepository;
import com.veterinaria.especialidades.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Override
    public List<EspecialidadResponse> listar() {
        return especialidadRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public EspecialidadResponse obtenerPorId(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));
        return mapToResponse(especialidad);
    }

    @Override
    public EspecialidadResponse crear(EspecialidadRequest request) {
        if (especialidadRepository.existsByNombre(request.getNombre())) {
            throw new DuplicateResourceException("Ya existe una especialidad con el nombre: " + request.getNombre());
        }
        Especialidad especialidad = Especialidad.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .area(request.getArea())
                .estado(request.getEstado())
                .build();
        return mapToResponse(especialidadRepository.save(especialidad));
    }

    @Override
    public EspecialidadResponse actualizar(Long id, EspecialidadRequest request) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));
        especialidad.setNombre(request.getNombre());
        especialidad.setDescripcion(request.getDescripcion());
        especialidad.setArea(request.getArea());
        especialidad.setEstado(request.getEstado());
        return mapToResponse(especialidadRepository.save(especialidad));
    }

    @Override
    public void eliminar(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));
        especialidadRepository.delete(especialidad);
    }

    private EspecialidadResponse mapToResponse(Especialidad especialidad) {
        return EspecialidadResponse.builder()
                .idEspecialidad(especialidad.getIdEspecialidad())
                .nombre(especialidad.getNombre())
                .descripcion(especialidad.getDescripcion())
                .area(especialidad.getArea())
                .estado(especialidad.getEstado())
                .build();
    }
}
