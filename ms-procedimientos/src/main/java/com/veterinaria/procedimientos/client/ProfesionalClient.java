package com.veterinaria.procedimientos.client;

import com.veterinaria.procedimientos.client.dto.ProfesionalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-profesionales")
public interface ProfesionalClient {

    @GetMapping("/api/v1/profesionales/{id}")
    ProfesionalDTO obtenerProfesional(@PathVariable("id") Long id);
}
