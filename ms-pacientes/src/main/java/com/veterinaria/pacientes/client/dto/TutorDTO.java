package com.veterinaria.pacientes.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO {

    private Long idTutor;
    private String rut;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String direccion;
    private Boolean estado;
}
