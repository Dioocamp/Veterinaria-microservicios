package com.veterinaria.profesionales.controller;

import com.veterinaria.profesionales.dto.ProfesionalRequest;
import com.veterinaria.profesionales.dto.ProfesionalResponse;
import com.veterinaria.profesionales.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @GetMapping
    public ResponseEntity<List<ProfesionalResponse>> listar() {
        return ResponseEntity.ok(profesionalService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesionalService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfesionalResponse> crear(@Valid @RequestBody ProfesionalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profesionalService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProfesionalRequest request) {
        return ResponseEntity.ok(profesionalService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesionalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
