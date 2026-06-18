package com.veterinaria.productos.service.impl;

import com.veterinaria.productos.client.CategoriaClient;
import com.veterinaria.productos.client.dto.CategoriaDTO;
import com.veterinaria.productos.dto.ProductoRequest;
import com.veterinaria.productos.dto.ProductoResponse;
import com.veterinaria.productos.entity.Producto;
import com.veterinaria.productos.exception.BadRequestException;
import com.veterinaria.productos.exception.ResourceNotFoundException;
import com.veterinaria.productos.repository.ProductoRepository;
import com.veterinaria.productos.service.ProductoService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaClient categoriaClient;

    @Override
    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        ProductoResponse response = mapToResponse(producto);
        response.setCategoriaRegistrada(obtenerCategoriaOpcional(producto.getIdCategoria()));
        return response;
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        CategoriaDTO categoria = validarCategoria(request.getIdCategoria());
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .marca(request.getMarca())
                .descripcion(request.getDescripcion())
                .stock(request.getStock())
                .precio(request.getPrecio())
                .idCategoria(request.getIdCategoria())
                .estado(request.getEstado())
                .build();
        ProductoResponse response = mapToResponse(productoRepository.save(producto));
        response.setCategoriaRegistrada(categoria);
        return response;
    }

    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        CategoriaDTO categoria = validarCategoria(request.getIdCategoria());
        producto.setNombre(request.getNombre());
        producto.setCategoria(request.getCategoria());
        producto.setMarca(request.getMarca());
        producto.setDescripcion(request.getDescripcion());
        producto.setStock(request.getStock());
        producto.setPrecio(request.getPrecio());
        producto.setIdCategoria(request.getIdCategoria());
        producto.setEstado(request.getEstado());
        ProductoResponse response = mapToResponse(productoRepository.save(producto));
        response.setCategoriaRegistrada(categoria);
        return response;
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }

    private CategoriaDTO validarCategoria(Long idCategoria) {
        try {
            return categoriaClient.obtenerCategoria(idCategoria);
        } catch (FeignException.NotFound ex) {
            throw new BadRequestException("La categoria con id " + idCategoria + " no existe");
        } catch (FeignException ex) {
            throw new BadRequestException("No se pudo validar la categoria con id " + idCategoria + ": el servicio de categorias no esta disponible");
        }
    }

    private CategoriaDTO obtenerCategoriaOpcional(Long idCategoria) {
        try {
            return categoriaClient.obtenerCategoria(idCategoria);
        } catch (FeignException ex) {
            return null;
        }
    }

    private ProductoResponse mapToResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .marca(producto.getMarca())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .precio(producto.getPrecio())
                .idCategoria(producto.getIdCategoria())
                .estado(producto.getEstado())
                .build();
    }
}
