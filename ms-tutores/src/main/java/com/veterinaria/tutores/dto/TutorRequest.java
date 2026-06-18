package com.veterinaria.tutores.dto;

import jakarta.validation.constraints.Email;
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
public class TutorRequest {

    @NotBlank(message = "El rut es obligatorio")
    @Size(max = 20, message = "El rut no puede superar los 20 caracteres")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    private String apellido;

    @Size(max = 20, message = "El telefono no puede superar los 20 caracteres")
    private String telefono;

    @Email(message = "El correo debe tener un formato valido")
    @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
    private String correo;

    @Size(max = 200, message = "La direccion no puede superar los 200 caracteres")
    private String direccion;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
