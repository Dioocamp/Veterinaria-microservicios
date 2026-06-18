package com.veterinaria.profesionales.client;

import com.veterinaria.profesionales.client.dto.EspecialidadDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-especialidades")
public interface EspecialidadClient {

    @GetMapping("/api/v1/especialidades/{id}")
    EspecialidadDTO obtenerEspecialidad(@PathVariable("id") Long id);
}
