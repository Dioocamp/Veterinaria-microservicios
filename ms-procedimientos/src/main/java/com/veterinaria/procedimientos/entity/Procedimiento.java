package com.veterinaria.procedimientos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "procedimientos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Procedimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_procedimiento")
    private Long idProcedimiento;

    @Column(name = "id_paciente", nullable = false)
    private Long idPaciente;

    @Column(name = "id_profesional", nullable = false)
    private Long idProfesional;

    @Column(name = "nombre_procedimiento", nullable = false, length = 150)
    private String nombreProcedimiento;

    @Column(name = "tipo_procedimiento", length = 100)
    private String tipoProcedimiento;

    @Column(name = "fecha_procedimiento", nullable = false)
    private LocalDate fechaProcedimiento;

    @Column(name = "resultado", length = 300)
    private String resultado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
