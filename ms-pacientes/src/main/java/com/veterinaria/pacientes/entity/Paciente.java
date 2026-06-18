package com.veterinaria.pacientes.entity;

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

@Entity
@Table(name = "pacientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long idPaciente;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "especie", nullable = false, length = 60)
    private String especie;

    @Column(name = "raza", length = 60)
    private String raza;

    @Column(name = "sexo", length = 20)
    private String sexo;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "color", length = 60)
    private String color;

    @Column(name = "id_tutor", nullable = false)
    private Long idTutor;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
