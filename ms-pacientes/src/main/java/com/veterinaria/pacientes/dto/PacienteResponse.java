package com.veterinaria.pacientes.dto;

import com.veterinaria.pacientes.client.dto.TutorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {

    private Long idPaciente;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private Integer edad;
    private Double peso;
    private String color;
    private Long idTutor;
    private Boolean estado;
    private TutorDTO tutor;
}
