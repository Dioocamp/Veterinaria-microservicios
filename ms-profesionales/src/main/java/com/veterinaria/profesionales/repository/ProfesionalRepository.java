package com.veterinaria.profesionales.repository;

import com.veterinaria.profesionales.entity.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    boolean existsByRut(String rut);
}
