package com.veterinaria.categorias.service.impl;

import com.veterinaria.categorias.dto.CategoriaRequest;
import com.veterinaria.categorias.dto.CategoriaResponse;
import com.veterinaria.categorias.entity.Categoria;
import com.veterinaria.categorias.exception.DuplicateResourceException;
import com.veterinaria.categorias.exception.ResourceNotFoundException;
import com.veterinaria.categorias.repository.CategoriaRepository;
import com.veterinaria.categorias.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public CategoriaResponse obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        return mapToResponse(categoria);
    }

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new DuplicateResourceException("Ya existe una categoria con el nombre: " + request.getNombre());
        }
        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .estado(request.getEstado())
                .build();
        return mapToResponse(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        if (!categoria.getNombre().equals(request.getNombre()) && categoriaRepository.existsByNombre(request.getNombre())) {
            throw new DuplicateResourceException("Ya existe una categoria con el nombre: " + request.getNombre());
        }
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setEstado(request.getEstado());
        return mapToResponse(categoriaRepository.save(categoria));
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        categoriaRepository.delete(categoria);
    }

    private CategoriaResponse mapToResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .estado(categoria.getEstado())
                .build();
    }
}
