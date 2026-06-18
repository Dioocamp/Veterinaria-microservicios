package com.veterinaria.procedimientos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimientoRequest {

    @NotNull(message = "El id del paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "El id del profesional es obligatorio")
    private Long idProfesional;

    @NotBlank(message = "El nombre del procedimiento es obligatorio")
    @Size(max = 150, message = "El nombre del procedimiento no puede superar los 150 caracteres")
    private String nombreProcedimiento;

    @Size(max = 100, message = "El tipo de procedimiento no puede superar los 100 caracteres")
    private String tipoProcedimiento;

    @NotNull(message = "La fecha del procedimiento es obligatoria")
    private LocalDate fechaProcedimiento;

    @Size(max = 300, message = "El resultado no puede superar los 300 caracteres")
    private String resultado;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
