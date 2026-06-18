package com.veterinaria.profesionales.service.impl;

import com.veterinaria.profesionales.client.EspecialidadClient;
import com.veterinaria.profesionales.client.dto.EspecialidadDTO;
import com.veterinaria.profesionales.dto.ProfesionalRequest;
import com.veterinaria.profesionales.dto.ProfesionalResponse;
import com.veterinaria.profesionales.entity.Profesional;
import com.veterinaria.profesionales.exception.BadRequestException;
import com.veterinaria.profesionales.exception.DuplicateResourceException;
import com.veterinaria.profesionales.exception.ResourceNotFoundException;
import com.veterinaria.profesionales.repository.ProfesionalRepository;
import com.veterinaria.profesionales.service.ProfesionalService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProfesionalServiceImpl implements ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final EspecialidadClient especialidadClient;

    @Override
    public List<ProfesionalResponse> listar() {
        return profesionalRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProfesionalResponse obtenerPorId(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));
        ProfesionalResponse response = mapToResponse(profesional);
        response.setEspecialidad(obtenerEspecialidadOpcional(profesional.getIdEspecialidad()));
        return response;
    }

    @Override
    public ProfesionalResponse crear(ProfesionalRequest request) {
        if (profesionalRepository.existsByRut(request.getRut())) {
            throw new DuplicateResourceException("Ya existe un profesional con el rut: " + request.getRut());
        }
        EspecialidadDTO especialidad = validarEspecialidad(request.getIdEspecialidad());
        Profesional profesional = Profesional.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .correo(request.getCorreo())
                .cargo(request.getCargo())
                .idEspecialidad(request.getIdEspecialidad())
                .estado(request.getEstado())
                .build();
        ProfesionalResponse response = mapToResponse(profesionalRepository.save(profesional));
        response.setEspecialidad(especialidad);
        return response;
    }

    @Override
    public ProfesionalResponse actualizar(Long id, ProfesionalRequest request) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));
        if (!profesional.getRut().equals(request.getRut()) && profesionalRepository.existsByRut(request.getRut())) {
            throw new DuplicateResourceException("Ya existe un profesional con el rut: " + request.getRut());
        }
        EspecialidadDTO especialidad = validarEspecialidad(request.getIdEspecialidad());
        profesional.setRut(request.getRut());
        profesional.setNombre(request.getNombre());
        profesional.setApellido(request.getApellido());
        profesional.setTelefono(request.getTelefono());
        profesional.setCorreo(request.getCorreo());
        profesional.setCargo(request.getCargo());
        profesional.setIdEspecialidad(request.getIdEspecialidad());
        profesional.setEstado(request.getEstado());
        ProfesionalResponse response = mapToResponse(profesionalRepository.save(profesional));
        response.setEspecialidad(especialidad);
        return response;
    }

    @Override
    public void eliminar(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado con id: " + id));
        profesionalRepository.delete(profesional);
    }

    private EspecialidadDTO validarEspecialidad(Long idEspecialidad) {
        try {
            return especialidadClient.obtenerEspecialidad(idEspecialidad);
        } catch (FeignException.NotFound ex) {
            throw new BadRequestException("La especialidad con id " + idEspecialidad + " no existe");
        } catch (FeignException ex) {
            throw new BadRequestException("No se pudo validar la especialidad con id " + idEspecialidad + ": el servicio de especialidades no esta disponible");
        }
    }

    private EspecialidadDTO obtenerEspecialidadOpcional(Long idEspecialidad) {
        try {
            return especialidadClient.obtenerEspecialidad(idEspecialidad);
        } catch (FeignException ex) {
            return null;
        }
    }

    private ProfesionalResponse mapToResponse(Profesional profesional) {
        return ProfesionalResponse.builder()
                .idProfesional(profesional.getIdProfesional())
                .rut(profesional.getRut())
                .nombre(profesional.getNombre())
                .apellido(profesional.getApellido())
                .telefono(profesional.getTelefono())
                .correo(profesional.getCorreo())
                .cargo(profesional.getCargo())
                .idEspecialidad(profesional.getIdEspecialidad())
                .estado(profesional.getEstado())
                .build();
    }
}
