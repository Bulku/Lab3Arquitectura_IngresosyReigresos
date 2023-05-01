package com.udea.repository;

import com.udea.domain.DocumentoReingresoEstudiante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentoReingresoEstudiante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoReingresoEstudianteRepository extends JpaRepository<DocumentoReingresoEstudiante, Long> {}
