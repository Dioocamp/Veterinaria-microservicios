package com.veterinaria.especialidades.service;

import com.veterinaria.especialidades.dto.EspecialidadRequest;
import com.veterinaria.especialidades.dto.EspecialidadResponse;
import com.veterinaria.especialidades.entity.Especialidad;
import com.veterinaria.especialidades.exception.DuplicateResourceException;
import com.veterinaria.especialidades.exception.ResourceNotFoundException;
import com.veterinaria.especialidades.repository.EspecialidadRepository;
import com.veterinaria.especialidades.service.impl.EspecialidadServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EspecialidadServiceImpl especialidadService;

    private Especialidad especialidad() {
        return Especialidad.builder()
                .idEspecialidad(1L).nombre("Cardiologia").descripcion("Corazon").area("Clinica").estado(true)
                .build();
    }

    @Test
    @DisplayName("Debe listar todas las especialidades")
    void debeListar() {
        when(especialidadRepository.findAll()).thenReturn(List.of(especialidad()));
        List<EspecialidadResponse> r = especialidadService.listar();
        assertEquals(1, r.size());
        assertEquals("Cardiologia", r.get(0).getNombre());
        verify(especialidadRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener una especialidad por id")
    void debeObtenerPorId() {
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad()));
        EspecialidadResponse r = especialidadService.obtenerPorId(1L);
        assertEquals(1L, r.getIdEspecialidad());
        assertEquals("Clinica", r.getArea());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando no existe")
    void debeLanzarNotFound() {
        when(especialidadRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> especialidadService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear una especialidad correctamente")
    void debeCrear() {
        EspecialidadRequest req = EspecialidadRequest.builder()
                .nombre("Cardiologia").descripcion("Corazon").area("Clinica").estado(true).build();
        when(especialidadRepository.existsByNombre("Cardiologia")).thenReturn(false);
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidad());
        EspecialidadResponse r = especialidadService.crear(req);
        assertNotNull(r);
        assertEquals("Cardiologia", r.getNombre());
        verify(especialidadRepository).save(any(Especialidad.class));
    }

    @Test
    @DisplayName("Debe lanzar Duplicate cuando el nombre ya existe")
    void debeLanzarDuplicate() {
        EspecialidadRequest req = EspecialidadRequest.builder().nombre("Cardiologia").estado(true).build();
        when(especialidadRepository.existsByNombre("Cardiologia")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> especialidadService.crear(req));
        verify(especialidadRepository, never()).save(any());
    }
}
