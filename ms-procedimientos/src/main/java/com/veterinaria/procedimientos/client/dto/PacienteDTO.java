package com.veterinaria.procedimientos.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PacienteDTO {

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
}
