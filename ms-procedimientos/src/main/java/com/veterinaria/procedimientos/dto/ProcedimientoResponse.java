package com.veterinaria.procedimientos.dto;

import com.veterinaria.procedimientos.client.dto.PacienteDTO;
import com.veterinaria.procedimientos.client.dto.ProfesionalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedimientoResponse {

    private Long idProcedimiento;
    private Long idPaciente;
    private Long idProfesional;
    private String nombreProcedimiento;
    private String tipoProcedimiento;
    private LocalDate fechaProcedimiento;
    private String resultado;
    private String observaciones;
    private Boolean estado;
    private PacienteDTO paciente;
    private ProfesionalDTO profesional;
}
