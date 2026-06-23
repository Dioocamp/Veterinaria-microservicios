package com.veterinaria.tutores.service;

import com.veterinaria.tutores.dto.TutorRequest;
import com.veterinaria.tutores.dto.TutorResponse;
import com.veterinaria.tutores.entity.Tutor;
import com.veterinaria.tutores.exception.DuplicateResourceException;
import com.veterinaria.tutores.exception.ResourceNotFoundException;
import com.veterinaria.tutores.repository.TutorRepository;
import com.veterinaria.tutores.service.impl.TutorServiceImpl;
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
class TutorServiceTest {

    @Mock
    private TutorRepository tutorRepository;

    @InjectMocks
    private TutorServiceImpl tutorService;

    private Tutor tutor() {
        return Tutor.builder()
                .idTutor(1L).rut("11111111-1").nombre("Camila").apellido("Rojas")
                .telefono("123").correo("c@mail.com").direccion("Calle 1").estado(true)
                .build();
    }

    @Test
    @DisplayName("Debe listar todos los tutores")
    void debeListar() {
        when(tutorRepository.findAll()).thenReturn(List.of(tutor()));
        List<TutorResponse> r = tutorService.listar();
        assertEquals(1, r.size());
        assertEquals("Camila", r.get(0).getNombre());
        verify(tutorRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener un tutor por id")
    void debeObtenerPorId() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor()));
        TutorResponse r = tutorService.obtenerPorId(1L);
        assertEquals(1L, r.getIdTutor());
        assertEquals("Rojas", r.getApellido());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando el tutor no existe")
    void debeLanzarNotFound() {
        when(tutorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tutorService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un tutor correctamente")
    void debeCrear() {
        TutorRequest req = TutorRequest.builder()
                .rut("11111111-1").nombre("Camila").apellido("Rojas").estado(true).build();
        when(tutorRepository.existsByRut("11111111-1")).thenReturn(false);
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor());
        TutorResponse r = tutorService.crear(req);
        assertNotNull(r);
        assertEquals("Camila", r.getNombre());
        verify(tutorRepository).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Debe lanzar Duplicate cuando el rut ya existe")
    void debeLanzarDuplicate() {
        TutorRequest req = TutorRequest.builder()
                .rut("11111111-1").nombre("X").apellido("Y").estado(true).build();
        when(tutorRepository.existsByRut("11111111-1")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> tutorService.crear(req));
        verify(tutorRepository, never()).save(any());
    }
}
