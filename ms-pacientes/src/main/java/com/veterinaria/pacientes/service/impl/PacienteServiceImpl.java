package com.veterinaria.pacientes.service.impl;

import com.veterinaria.pacientes.client.TutorClient;
import com.veterinaria.pacientes.client.dto.TutorDTO;
import com.veterinaria.pacientes.dto.PacienteRequest;
import com.veterinaria.pacientes.dto.PacienteResponse;
import com.veterinaria.pacientes.entity.Paciente;
import com.veterinaria.pacientes.exception.BadRequestException;
import com.veterinaria.pacientes.exception.ResourceNotFoundException;
import com.veterinaria.pacientes.repository.PacienteRepository;
import com.veterinaria.pacientes.service.PacienteService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final TutorClient tutorClient;

    @Override
    public List<PacienteResponse> listar() {
        return pacienteRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public PacienteResponse obtenerPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        PacienteResponse response = mapToResponse(paciente);
        response.setTutor(obtenerTutorOpcional(paciente.getIdTutor()));
        return response;
    }

    @Override
    public PacienteResponse crear(PacienteRequest request) {
        TutorDTO tutor = validarTutor(request.getIdTutor());
        Paciente paciente = Paciente.builder()
                .nombre(request.getNombre())
                .especie(request.getEspecie())
                .raza(request.getRaza())
                .sexo(request.getSexo())
                .edad(request.getEdad())
                .peso(request.getPeso())
                .color(request.getColor())
                .idTutor(request.getIdTutor())
                .estado(request.getEstado())
                .build();
        PacienteResponse response = mapToResponse(pacienteRepository.save(paciente));
        response.setTutor(tutor);
        return response;
    }

    @Override
    public PacienteResponse actualizar(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        TutorDTO tutor = validarTutor(request.getIdTutor());
        paciente.setNombre(request.getNombre());
        paciente.setEspecie(request.getEspecie());
        paciente.setRaza(request.getRaza());
        paciente.setSexo(request.getSexo());
        paciente.setEdad(request.getEdad());
        paciente.setPeso(request.getPeso());
        paciente.setColor(request.getColor());
        paciente.setIdTutor(request.getIdTutor());
        paciente.setEstado(request.getEstado());
        PacienteResponse response = mapToResponse(pacienteRepository.save(paciente));
        response.setTutor(tutor);
        return response;
    }

    @Override
    public void eliminar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
        pacienteRepository.delete(paciente);
    }

    private TutorDTO validarTutor(Long idTutor) {
        try {
            return tutorClient.obtenerTutor(idTutor);
        } catch (FeignException.NotFound ex) {
            throw new BadRequestException("El tutor con id " + idTutor + " no existe");
        } catch (FeignException ex) {
            throw new BadRequestException("No se pudo validar el tutor con id " + idTutor + ": el servicio de tutores no esta disponible");
        }
    }

    private TutorDTO obtenerTutorOpcional(Long idTutor) {
        try {
            return tutorClient.obtenerTutor(idTutor);
        } catch (FeignException ex) {
            return null;
        }
    }

    private PacienteResponse mapToResponse(Paciente paciente) {
        return PacienteResponse.builder()
                .idPaciente(paciente.getIdPaciente())
                .nombre(paciente.getNombre())
                .especie(paciente.getEspecie())
                .raza(paciente.getRaza())
                .sexo(paciente.getSexo())
                .edad(paciente.getEdad())
                .peso(paciente.getPeso())
                .color(paciente.getColor())
                .idTutor(paciente.getIdTutor())
                .estado(paciente.getEstado())
                .build();
    }
}
