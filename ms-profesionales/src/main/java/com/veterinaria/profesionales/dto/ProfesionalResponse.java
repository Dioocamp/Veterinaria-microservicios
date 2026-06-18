package com.veterinaria.profesionales.dto;

import com.veterinaria.profesionales.client.dto.EspecialidadDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionalResponse {

    private Long idProfesional;
    private String rut;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String cargo;
    private Long idEspecialidad;
    private Boolean estado;
    private EspecialidadDTO especialidad;
}
