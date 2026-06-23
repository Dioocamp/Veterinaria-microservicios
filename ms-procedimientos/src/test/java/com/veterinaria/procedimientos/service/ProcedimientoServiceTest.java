package com.veterinaria.procedimientos.service;

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
import com.veterinaria.procedimientos.service.impl.ProcedimientoServiceImpl;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcedimientoServiceTest {

    @Mock
    private ProcedimientoRepository procedimientoRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private ProfesionalClient profesionalClient;

    @InjectMocks
    private ProcedimientoServiceImpl procedimientoService;

    private Procedimiento procedimiento() {
        return Procedimiento.builder()
                .idProcedimiento(1L).idPaciente(1L).idProfesional(1L)
                .nombreProcedimiento("Radiografia").tipoProcedimiento("Examen")
                .fechaProcedimiento(LocalDate.now()).resultado("Ok").observaciones("Sin novedad").estado(true)
                .build();
    }

    private FeignException feignNotFound() {
        Request request = Request.create(Request.HttpMethod.GET, "http://localhost",
                Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder().status(404).reason("Not Found")
                .request(request).headers(Collections.emptyMap()).build();
        return FeignException.errorStatus("PacienteClient#obtenerPaciente", response);
    }

    @Test
    @DisplayName("Debe listar todos los procedimientos")
    void debeListar() {
        when(procedimientoRepository.findAll()).thenReturn(List.of(procedimiento()));
        List<ProcedimientoResponse> r = procedimientoService.listar();
        assertEquals(1, r.size());
        assertEquals("Radiografia", r.get(0).getNombreProcedimiento());
        verify(procedimientoRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener un procedimiento por id con paciente y profesional embebidos")
    void debeObtenerPorId() {
        when(procedimientoRepository.findById(1L)).thenReturn(Optional.of(procedimiento()));
        when(pacienteClient.obtenerPaciente(1L)).thenReturn(PacienteDTO.builder().idPaciente(1L).nombre("Firulais").build());
        when(profesionalClient.obtenerProfesional(1L)).thenReturn(ProfesionalDTO.builder().idProfesional(1L).nombre("Diego").build());
        ProcedimientoResponse r = procedimientoService.obtenerPorId(1L);
        assertNotNull(r.getPaciente());
        assertNotNull(r.getProfesional());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando el procedimiento no existe")
    void debeLanzarNotFound() {
        when(procedimientoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> procedimientoService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un procedimiento validando paciente y profesional por Feign")
    void debeCrear() {
        ProcedimientoRequest req = ProcedimientoRequest.builder()
                .idPaciente(1L).idProfesional(1L).nombreProcedimiento("Radiografia")
                .fechaProcedimiento(LocalDate.now()).estado(true).build();
        when(pacienteClient.obtenerPaciente(1L)).thenReturn(PacienteDTO.builder().idPaciente(1L).build());
        when(profesionalClient.obtenerProfesional(1L)).thenReturn(ProfesionalDTO.builder().idProfesional(1L).build());
        when(procedimientoRepository.save(any(Procedimiento.class))).thenReturn(procedimiento());
        ProcedimientoResponse r = procedimientoService.crear(req);
        assertNotNull(r);
        verify(procedimientoRepository).save(any(Procedimiento.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequest cuando el paciente no existe")
    void debeLanzarBadRequestSiPacienteNoExiste() {
        ProcedimientoRequest req = ProcedimientoRequest.builder()
                .idPaciente(99L).idProfesional(1L).nombreProcedimiento("Radiografia")
                .fechaProcedimiento(LocalDate.now()).estado(true).build();
        when(pacienteClient.obtenerPaciente(99L)).thenThrow(feignNotFound());
        assertThrows(BadRequestException.class, () -> procedimientoService.crear(req));
        verify(procedimientoRepository, never()).save(any());
    }
}
