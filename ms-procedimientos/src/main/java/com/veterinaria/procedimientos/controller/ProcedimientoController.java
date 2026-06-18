package com.veterinaria.procedimientos.controller;

import com.veterinaria.procedimientos.dto.ProcedimientoRequest;
import com.veterinaria.procedimientos.dto.ProcedimientoResponse;
import com.veterinaria.procedimientos.service.ProcedimientoService;
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
@RequestMapping("/api/v1/procedimientos")
@RequiredArgsConstructor
public class ProcedimientoController {

    private final ProcedimientoService procedimientoService;

    @GetMapping
    public ResponseEntity<List<ProcedimientoResponse>> listar() {
        return ResponseEntity.ok(procedimientoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimientoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procedimientoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProcedimientoResponse> crear(@Valid @RequestBody ProcedimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(procedimientoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimientoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProcedimientoRequest request) {
        return ResponseEntity.ok(procedimientoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        procedimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
