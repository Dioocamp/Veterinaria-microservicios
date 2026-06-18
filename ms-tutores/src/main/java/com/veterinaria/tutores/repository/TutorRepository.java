package com.veterinaria.tutores.repository;

import com.veterinaria.tutores.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    boolean existsByRut(String rut);
}
