package com.veterinaria.procedimientos.client;

import com.veterinaria.procedimientos.client.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-pacientes")
public interface PacienteClient {

    @GetMapping("/api/v1/pacientes/{id}")
    PacienteDTO obtenerPaciente(@PathVariable("id") Long id);
}
