package com.veterinaria.pacientes.service;

import com.veterinaria.pacientes.client.TutorClient;
import com.veterinaria.pacientes.client.dto.TutorDTO;
import com.veterinaria.pacientes.dto.PacienteRequest;
import com.veterinaria.pacientes.dto.PacienteResponse;
import com.veterinaria.pacientes.entity.Paciente;
import com.veterinaria.pacientes.exception.BadRequestException;
import com.veterinaria.pacientes.exception.ResourceNotFoundException;
import com.veterinaria.pacientes.repository.PacienteRepository;
import com.veterinaria.pacientes.service.impl.PacienteServiceImpl;
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
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private TutorClient tutorClient;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    private Paciente paciente() {
        return Paciente.builder()
                .idPaciente(1L).nombre("Firulais").especie("Perro").raza("Labrador")
                .sexo("Macho").edad(4).peso(28.5).color("Dorado").idTutor(1L).estado(true)
                .build();
    }

    private TutorDTO tutorDTO() {
        return TutorDTO.builder().idTutor(1L).nombre("Camila").apellido("Rojas").estado(true).build();
    }

    private FeignException feignNotFound() {
        Request request = Request.create(Request.HttpMethod.GET, "http://localhost",
                Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder().status(404).reason("Not Found")
                .request(request).headers(Collections.emptyMap()).build();
        return FeignException.errorStatus("TutorClient#obtenerTutor", response);
    }

    @Test
    @DisplayName("Debe listar todos los pacientes")
    void debeListar() {
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente()));
        List<PacienteResponse> r = pacienteService.listar();
        assertEquals(1, r.size());
        assertEquals("Firulais", r.get(0).getNombre());
        verify(pacienteRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener un paciente por id con su tutor embebido")
    void debeObtenerPorId() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente()));
        when(tutorClient.obtenerTutor(1L)).thenReturn(tutorDTO());
        PacienteResponse r = pacienteService.obtenerPorId(1L);
        assertEquals("Firulais", r.getNombre());
        assertNotNull(r.getTutor());
        assertEquals("Camila", r.getTutor().getNombre());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando el paciente no existe")
    void debeLanzarNotFound() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un paciente validando el tutor por Feign")
    void debeCrear() {
        PacienteRequest req = PacienteRequest.builder()
                .nombre("Firulais").especie("Perro").idTutor(1L).estado(true).build();
        when(tutorClient.obtenerTutor(1L)).thenReturn(tutorDTO());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente());
        PacienteResponse r = pacienteService.crear(req);
        assertNotNull(r);
        assertNotNull(r.getTutor());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequest cuando el tutor no existe en ms-tutores")
    void debeLanzarBadRequestSiTutorNoExiste() {
        PacienteRequest req = PacienteRequest.builder()
                .nombre("Mishi").especie("Gato").idTutor(99L).estado(true).build();
        when(tutorClient.obtenerTutor(99L)).thenThrow(feignNotFound());
        assertThrows(BadRequestException.class, () -> pacienteService.crear(req));
        verify(pacienteRepository, never()).save(any());
    }
}
