package com.veterinaria.procedimientos.repository;

import com.veterinaria.procedimientos.entity.Procedimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedimientoRepository extends JpaRepository<Procedimiento, Long> {
}
