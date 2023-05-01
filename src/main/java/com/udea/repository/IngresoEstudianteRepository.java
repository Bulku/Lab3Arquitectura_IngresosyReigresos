package com.udea.repository;

import com.udea.domain.IngresoEstudiante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IngresoEstudiante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngresoEstudianteRepository extends JpaRepository<IngresoEstudiante, Long> {}
