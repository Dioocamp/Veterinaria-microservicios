package com.veterinaria.productos.service;

import com.veterinaria.productos.client.CategoriaClient;
import com.veterinaria.productos.client.dto.CategoriaDTO;
import com.veterinaria.productos.dto.ProductoRequest;
import com.veterinaria.productos.dto.ProductoResponse;
import com.veterinaria.productos.entity.Producto;
import com.veterinaria.productos.exception.BadRequestException;
import com.veterinaria.productos.exception.ResourceNotFoundException;
import com.veterinaria.productos.repository.ProductoRepository;
import com.veterinaria.productos.service.impl.ProductoServiceImpl;
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

import java.math.BigDecimal;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaClient categoriaClient;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto() {
        return Producto.builder()
                .idProducto(1L).nombre("Croquetas").categoria("Alimentos").marca("ProPlan")
                .descripcion("Alimento").stock(50).precio(new BigDecimal("45990.00")).idCategoria(1L).estado(true)
                .build();
    }

    private CategoriaDTO categoriaDTO() {
        return CategoriaDTO.builder().idCategoria(1L).nombre("Alimentos").estado(true).build();
    }

    private FeignException feignNotFound() {
        Request request = Request.create(Request.HttpMethod.GET, "http://localhost",
                Collections.emptyMap(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder().status(404).reason("Not Found")
                .request(request).headers(Collections.emptyMap()).build();
        return FeignException.errorStatus("CategoriaClient#obtenerCategoria", response);
    }

    @Test
    @DisplayName("Debe listar todos los productos")
    void debeListar() {
        when(productoRepository.findAll()).thenReturn(List.of(producto()));
        List<ProductoResponse> r = productoService.listar();
        assertEquals(1, r.size());
        assertEquals("Croquetas", r.get(0).getNombre());
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("Debe obtener un producto por id con su categoria embebida")
    void debeObtenerPorId() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto()));
        when(categoriaClient.obtenerCategoria(1L)).thenReturn(categoriaDTO());
        ProductoResponse r = productoService.obtenerPorId(1L);
        assertEquals("Croquetas", r.getNombre());
        assertNotNull(r.getCategoriaRegistrada());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFound cuando el producto no existe")
    void debeLanzarNotFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productoService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un producto validando la categoria por Feign")
    void debeCrear() {
        ProductoRequest req = ProductoRequest.builder()
                .nombre("Croquetas").stock(50).precio(new BigDecimal("45990.00")).idCategoria(1L).estado(true).build();
        when(categoriaClient.obtenerCategoria(1L)).thenReturn(categoriaDTO());
        when(productoRepository.save(any(Producto.class))).thenReturn(producto());
        ProductoResponse r = productoService.crear(req);
        assertNotNull(r);
        assertNotNull(r.getCategoriaRegistrada());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequest cuando la categoria no existe")
    void debeLanzarBadRequestSiCategoriaNoExiste() {
        ProductoRequest req = ProductoRequest.builder()
                .nombre("Croquetas").stock(50).precio(new BigDecimal("45990.00")).idCategoria(99L).estado(true).build();
        when(categoriaClient.obtenerCategoria(99L)).thenThrow(feignNotFound());
        assertThrows(BadRequestException.class, () -> productoService.crear(req));
        verify(productoRepository, never()).save(any());
    }
}
