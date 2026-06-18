package com.veterinaria.procedimientos.service.impl;

import com.veterinaria.procedimientos.client.PacienteClient;
import com.veterinaria.procedimientos.client.ProfesionalClient;
import com.veterinaria.procedimientos.client.dto.PacienteDTO;
import com.veterinaria.procedimientos.client.dto.ProfesionalDTO;
import com.veterinaria.procedimientos.dto.ProcedimientoRequest;
import com.veterinaria.procedimientos.dto.ProcedimientoResponse;
import com.veterinaria.procedimientos.entity.Procedimiento;
import com.veterinaria.procedimientos.exception.BadRequestException;
import com.veterinaria.procedimientos.exception.ResourceNotFoundException;
import com.veterinaria.procedimientos.repository.ProcedimientoRepository;
import com.veterinaria.procedimientos.service.ProcedimientoService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProcedimientoServiceImpl implements ProcedimientoService {

    private final ProcedimientoRepository procedimientoRepository;
    private final PacienteClient pacienteClient;
    private final ProfesionalClient profesionalClient;

    @Override
    public List<ProcedimientoResponse> listar() {
        return procedimientoRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProcedimientoResponse obtenerPorId(Long id) {
        Procedimiento procedimiento = procedimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id: " + id));
        ProcedimientoResponse response = mapToResponse(procedimiento);
        response.setPaciente(obtenerPacienteOpcional(procedimiento.getIdPaciente()));
        response.setProfesional(obtenerProfesionalOpcional(procedimiento.getIdProfesional()));
        return response;
    }

    @Override
    public ProcedimientoResponse crear(ProcedimientoRequest request) {
        PacienteDTO paciente = validarPaciente(request.getIdPaciente());
        ProfesionalDTO profesional = validarProfesional(request.getIdProfesional());
        Procedimiento procedimiento = Procedimiento.builder()
                .idPaciente(request.getIdPaciente())
                .idProfesional(request.getIdProfesional())
                .nombreProcedimiento(request.getNombreProcedimiento())
                .tipoProcedimiento(request.getTipoProcedimiento())
                .fechaProcedimiento(request.getFechaProcedimiento())
                .resultado(request.getResultado())
                .observaciones(request.getObservaciones())
                .estado(request.getEstado())
                .build();
        ProcedimientoResponse response = mapToResponse(procedimientoRepository.save(procedimiento));
        response.setPaciente(paciente);
        response.setProfesional(profesional);
        return response;
    }

    @Override
    public ProcedimientoResponse actualizar(Long id, ProcedimientoRequest request) {
        Procedimiento procedimiento = procedimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id: " + id));
        PacienteDTO paciente = validarPaciente(request.getIdPaciente());
        ProfesionalDTO profesional = validarProfesional(request.getIdProfesional());
        procedimiento.setIdPaciente(request.getIdPaciente());
        procedimiento.setIdProfesional(request.getIdProfesional());
        procedimiento.setNombreProcedimiento(request.getNombreProcedimiento());
        procedimiento.setTipoProcedimiento(request.getTipoProcedimiento());
        procedimiento.setFechaProcedimiento(request.getFechaProcedimiento());
        procedimiento.setResultado(request.getResultado());
        procedimiento.setObservaciones(request.getObservaciones());
        procedimiento.setEstado(request.getEstado());
        ProcedimientoResponse response = mapToResponse(procedimientoRepository.save(procedimiento));
        response.setPaciente(paciente);
        response.setProfesional(profesional);
        return response;
    }

    @Override
    public void eliminar(Long id) {
        Procedimiento procedimiento = procedimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Procedimiento no encontrado con id: " + id));
        procedimientoRepository.delete(procedimiento);
    }

    private PacienteDTO validarPaciente(Long idPaciente) {
        try {
            return pacienteClient.obtenerPaciente(idPaciente);
        } catch (FeignException.NotFound ex) {
            throw new BadRequestException("El paciente con id " + idPaciente + " no existe");
        } catch (FeignException ex) {
            throw new BadRequestException("No se pudo validar el paciente con id " + idPaciente + ": el servicio de pacientes no esta disponible");
        }
    }

    private ProfesionalDTO validarProfesional(Long idProfesional) {
        try {
            return profesionalClient.obtenerProfesional(idProfesional);
        } catch (FeignException.NotFound ex) {
            throw new BadRequestException("El profesional con id " + idProfesional + " no existe");
        } catch (FeignException ex) {
            throw new BadRequestException("No se pudo validar el profesional con id " + idProfesional + ": el servicio de profesionales no esta disponible");
        }
    }

    private PacienteDTO obtenerPacienteOpcional(Long idPaciente) {
        try {
            return pacienteClient.obtenerPaciente(idPaciente);
        } catch (FeignException ex) {
            return null;
        }
    }

    private ProfesionalDTO obtenerProfesionalOpcional(Long idProfesional) {
        try {
            return profesionalClient.obtenerProfesional(idProfesional);
        } catch (FeignException ex) {
            return null;
        }
    }

    private ProcedimientoResponse mapToResponse(Procedimiento procedimiento) {
        return ProcedimientoResponse.builder()
                .idProcedimiento(procedimiento.getIdProcedimiento())
                .idPaciente(procedimiento.getIdPaciente())
                .idProfesional(procedimiento.getIdProfesional())
                .nombreProcedimiento(procedimiento.getNombreProcedimiento())
                .tipoProcedimiento(procedimiento.getTipoProcedimiento())
                .fechaProcedimiento(procedimiento.getFechaProcedimiento())
                .resultado(procedimiento.getResultado())
                .observaciones(procedimiento.getObservaciones())
                .estado(procedimiento.getEstado())
                .build();
    }
}
