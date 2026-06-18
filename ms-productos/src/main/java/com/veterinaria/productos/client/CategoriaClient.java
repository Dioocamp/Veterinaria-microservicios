package com.veterinaria.productos.client;

import com.veterinaria.productos.client.dto.CategoriaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-categorias")
public interface CategoriaClient {

    @GetMapping("/api/v1/categorias/{id}")
    CategoriaDTO obtenerCategoria(@PathVariable("id") Long id);
}
