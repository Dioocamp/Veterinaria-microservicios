package com.veterinaria.tutores.service.impl;

import com.veterinaria.tutores.dto.TutorRequest;
import com.veterinaria.tutores.dto.TutorResponse;
import com.veterinaria.tutores.entity.Tutor;
import com.veterinaria.tutores.exception.DuplicateResourceException;
import com.veterinaria.tutores.exception.ResourceNotFoundException;
import com.veterinaria.tutores.repository.TutorRepository;
import com.veterinaria.tutores.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;

    @Override
    public List<TutorResponse> listar() {
        return tutorRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public TutorResponse obtenerPorId(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor no encontrado con id: " + id));
        return mapToResponse(tutor);
    }

    @Override
    public TutorResponse crear(TutorRequest request) {
        if (tutorRepository.existsByRut(request.getRut())) {
            throw new DuplicateResourceException("Ya existe un tutor con el rut: " + request.getRut());
        }
        Tutor tutor = Tutor.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .correo(request.getCorreo())
                .direccion(request.getDireccion())
                .estado(request.getEstado())
                .build();
        return mapToResponse(tutorRepository.save(tutor));
    }

    @Override
    public TutorResponse actualizar(Long id, TutorRequest request) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor no encontrado con id: " + id));
        if (!tutor.getRut().equals(request.getRut()) && tutorRepository.existsByRut(request.getRut())) {
            throw new DuplicateResourceException("Ya existe un tutor con el rut: " + request.getRut());
        }
        tutor.setRut(request.getRut());
        tutor.setNombre(request.getNombre());
        tutor.setApellido(request.getApellido());
        tutor.setTelefono(request.getTelefono());
        tutor.setCorreo(request.getCorreo());
        tutor.setDireccion(request.getDireccion());
        tutor.setEstado(request.getEstado());
        return mapToResponse(tutorRepository.save(tutor));
    }

    @Override
    public void eliminar(Long id) {
        Tutor tutor = tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor no encontrado con id: " + id));
        tutorRepository.delete(tutor);
    }

    private TutorResponse mapToResponse(Tutor tutor) {
        return TutorResponse.builder()
                .idTutor(tutor.getIdTutor())
                .rut(tutor.getRut())
                .nombre(tutor.getNombre())
                .apellido(tutor.getApellido())
                .telefono(tutor.getTelefono())
                .correo(tutor.getCorreo())
                .direccion(tutor.getDireccion())
                .estado(tutor.getEstado())
                .build();
    }
}
