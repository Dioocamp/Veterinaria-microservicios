package com.veterinaria.profesionales.service;

import com.veterinaria.profesionales.client.EspecialidadClient;
import com.veterinaria.profesionales.client.dto.EspecialidadDTO;
import com.veterinaria.profesionales.dto.ProfesionalRequest;
import com.veterinaria.profesionales.dto.ProfesionalResponse;
import com.veterinaria.profesionales.entity.Profesional;
import com.veterinaria.profesionales.exception.BadRequestException;
import com.veterinaria.profesionales.exception.DuplicateResourceException;
import com.veterinaria.profesionales.exception.ResourceNotFoundException;
import com.veterinaria.profesionales.repository.ProfesionalRepository;
import com.veterinaria.profesionales.service.impl.ProfesionalServiceImpl;
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
class ProfesionalServiceTest {

    @Mock
    private ProfesionalRepository profesionalRepository;

    @Mock
    private EspecialidadClient especialidadClient;

    @InjectMocks
    private ProfesionalServiceImpl profesionalService;

    private Profesional profesional() {
        return Profesional.builder()
                .idProfesional(1L).rut("9-9").nombre("Diego").apellido("Morales")
                .telefono("123").correo("d@vet.com").cargo("Veterinario").idEspecialidad(1L).estado(true)
                .build();
    }

    private EspecialidadDTO especialidadDTO() {
        return EspecialidadDTO.builder().idEspecialidad(1L).nombre("Cardiologia").estado(true).build();
    }

    private FeignException feignNotFound() {
        Request request = Request.create(Request.HttpMethod.GET, "http://localhost",
                Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder().status(404).reason("Not Found")
                .request(request).headers(Collections.emptyMap()).build();
        return FeignException.errorStatus("EspecialidadClient#obtenerEspecialidad", response);
    }

    @Test
    @DisplayName("Debe listar todos los profesionales")
    void debeListar() {
        when(profesionalRepository.findAll()).thenReturn(List.of(profesional()));
        List<ProfesionalResponse> r = profesionalService.listar();
        assertEquals(1, r.size());
        assertEquals("Diego", r.get(0).getNombre());
        verify(profesionalRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener un profesional por id con su especialidad embebida")
    void debeObtenerPorId() {
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional()));
        when(especialidadClient.obtenerEspecialidad(1L)).thenReturn(especialidadDTO());
        ProfesionalResponse r = profesionalService.obtenerPorId(1L);
        assertEquals("Diego", r.getNombre());
        assertNotNull(r.getEspecialidad());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando el profesional no existe")
    void debeLanzarNotFound() {
        when(profesionalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> profesionalService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un profesional validando la especialidad por Feign")
    void debeCrear() {
        ProfesionalRequest req = ProfesionalRequest.builder()
                .rut("9-9").nombre("Diego").apellido("Morales").idEspecialidad(1L).estado(true).build();
        when(profesionalRepository.existsByRut("9-9")).thenReturn(false);
        when(especialidadClient.obtenerEspecialidad(1L)).thenReturn(especialidadDTO());
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(profesional());
        ProfesionalResponse r = profesionalService.crear(req);
        assertNotNull(r);
        assertNotNull(r.getEspecialidad());
        verify(profesionalRepository).save(any(Profesional.class));
    }

    @Test
    @DisplayName("Debe lanzar Duplicate cuando el rut ya existe")
    void debeLanzarDuplicate() {
        ProfesionalRequest req = ProfesionalRequest.builder()
                .rut("9-9").nombre("Diego").apellido("Morales").idEspecialidad(1L).estado(true).build();
        when(profesionalRepository.existsByRut("9-9")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> profesionalService.crear(req));
        verify(profesionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar BadRequest cuando la especialidad no existe")
    void debeLanzarBadRequestSiEspecialidadNoExiste() {
        ProfesionalRequest req = ProfesionalRequest.builder()
                .rut("9-9").nombre("Diego").apellido("Morales").idEspecialidad(99L).estado(true).build();
        when(profesionalRepository.existsByRut("9-9")).thenReturn(false);
        when(especialidadClient.obtenerEspecialidad(99L)).thenThrow(feignNotFound());
        assertThrows(BadRequestException.class, () -> profesionalService.crear(req));
        verify(profesionalRepository, never()).save(any());
    }
}
