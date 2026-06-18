package com.veterinaria.servicios.repository;

import com.veterinaria.servicios.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    boolean existsByNombre(String nombre);
}
