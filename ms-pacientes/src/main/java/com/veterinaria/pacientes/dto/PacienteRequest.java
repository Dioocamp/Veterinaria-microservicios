package com.veterinaria.pacientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 60, message = "La especie no puede superar los 60 caracteres")
    private String especie;

    @Size(max = 60, message = "La raza no puede superar los 60 caracteres")
    private String raza;

    @Size(max = 20, message = "El sexo no puede superar los 20 caracteres")
    private String sexo;

    @PositiveOrZero(message = "La edad no puede ser negativa")
    private Integer edad;

    @Positive(message = "El peso debe ser mayor a cero")
    private Double peso;

    @Size(max = 60, message = "El color no puede superar los 60 caracteres")
    private String color;

    @NotNull(message = "El id del tutor es obligatorio")
    private Long idTutor;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
