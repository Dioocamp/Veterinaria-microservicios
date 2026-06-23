package com.veterinaria.servicios.service;

import com.veterinaria.servicios.dto.ServicioRequest;
import com.veterinaria.servicios.dto.ServicioResponse;
import com.veterinaria.servicios.entity.Servicio;
import com.veterinaria.servicios.exception.DuplicateResourceException;
import com.veterinaria.servicios.exception.ResourceNotFoundException;
import com.veterinaria.servicios.repository.ServicioRepository;
import com.veterinaria.servicios.service.impl.ServicioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    private Servicio servicio() {
        return Servicio.builder()
                .idServicio(1L).nombre("Bano").tipoServicio("Estetica").descripcion("Bano completo")
                .valor(new BigDecimal("25000.00")).duracionEstimada(60).estado(true)
                .build();
    }

    @Test
    @DisplayName("Debe listar todos los servicios")
    void debeListar() {
        when(servicioRepository.findAll()).thenReturn(List.of(servicio()));
        List<ServicioResponse> r = servicioService.listar();
        assertEquals(1, r.size());
        assertEquals("Bano", r.get(0).getNombre());
        verify(servicioRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener un servicio por id")
    void debeObtenerPorId() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio()));
        ServicioResponse r = servicioService.obtenerPorId(1L);
        assertEquals(1L, r.getIdServicio());
        assertEquals(new BigDecimal("25000.00"), r.getValor());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando no existe")
    void debeLanzarNotFound() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> servicioService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un servicio correctamente")
    void debeCrear() {
        ServicioRequest req = ServicioRequest.builder()
                .nombre("Bano").tipoServicio("Estetica").valor(new BigDecimal("25000.00"))
                .duracionEstimada(60).estado(true).build();
        when(servicioRepository.existsByNombre("Bano")).thenReturn(false);
        when(servicioRepository.save(any(Servicio.class))).thenReturn(servicio());
        ServicioResponse r = servicioService.crear(req);
        assertNotNull(r);
        assertEquals("Bano", r.getNombre());
        verify(servicioRepository).save(any(Servicio.class));
    }

    @Test
    @DisplayName("Debe lanzar Duplicate cuando el nombre ya existe")
    void debeLanzarDuplicate() {
        ServicioRequest req = ServicioRequest.builder()
                .nombre("Bano").valor(new BigDecimal("100.00")).estado(true).build();
        when(servicioRepository.existsByNombre("Bano")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> servicioService.crear(req));
        verify(servicioRepository, never()).save(any());
    }
}
