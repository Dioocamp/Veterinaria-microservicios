package com.veterinaria.pacientes.client;

import com.veterinaria.pacientes.client.dto.TutorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-tutores")
public interface TutorClient {

    @GetMapping("/api/v1/tutores/{id}")
    TutorDTO obtenerTutor(@PathVariable("id") Long id);
}
