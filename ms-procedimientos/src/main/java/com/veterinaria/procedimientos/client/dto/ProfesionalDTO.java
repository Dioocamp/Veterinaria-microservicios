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
public class ProfesionalDTO {

    private Long idProfesional;
    private String rut;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String cargo;
    private Long idEspecialidad;
    private Boolean estado;
}
