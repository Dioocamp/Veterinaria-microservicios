package com.veterinaria.categorias.service;

import com.veterinaria.categorias.dto.CategoriaRequest;
import com.veterinaria.categorias.dto.CategoriaResponse;
import com.veterinaria.categorias.entity.Categoria;
import com.veterinaria.categorias.exception.DuplicateResourceException;
import com.veterinaria.categorias.exception.ResourceNotFoundException;
import com.veterinaria.categorias.repository.CategoriaRepository;
import com.veterinaria.categorias.service.impl.CategoriaServiceImpl;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria() {
        return Categoria.builder()
                .idCategoria(1L).nombre("Alimentos").descripcion("Alimentos para mascotas").estado(true)
                .build();
    }

    @Test
    @DisplayName("Debe listar todas las categorias")
    void debeListar() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria()));
        List<CategoriaResponse> r = categoriaService.listar();
        assertEquals(1, r.size());
        assertEquals("Alimentos", r.get(0).getNombre());
        verify(categoriaRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener una categoria por id")
    void debeObtenerPorId() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria()));
        CategoriaResponse r = categoriaService.obtenerPorId(1L);
        assertEquals(1L, r.getIdCategoria());
        assertEquals("Alimentos", r.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando no existe")
    void debeLanzarNotFound() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoriaService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear una categoria correctamente")
    void debeCrear() {
        CategoriaRequest req = CategoriaRequest.builder()
                .nombre("Alimentos").descripcion("Alimentos para mascotas").estado(true).build();
        when(categoriaRepository.existsByNombre("Alimentos")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria());
        CategoriaResponse r = categoriaService.crear(req);
        assertNotNull(r);
        assertEquals("Alimentos", r.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe lanzar Duplicate cuando el nombre ya existe")
    void debeLanzarDuplicate() {
        CategoriaRequest req = CategoriaRequest.builder().nombre("Alimentos").estado(true).build();
        when(categoriaRepository.existsByNombre("Alimentos")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> categoriaService.crear(req));
        verify(categoriaRepository, never()).save(any());
    }
}
