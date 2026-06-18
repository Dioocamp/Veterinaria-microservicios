package com.veterinaria.especialidades.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 300, message = "La descripcion no puede superar los 300 caracteres")
    private String descripcion;

    @Size(max = 100, message = "El area no puede superar los 100 caracteres")
    private String area;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
