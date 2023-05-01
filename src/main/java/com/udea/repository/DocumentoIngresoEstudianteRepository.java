package com.udea.repository;

import com.udea.domain.DocumentoIngresoEstudiante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentoIngresoEstudiante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoIngresoEstudianteRepository extends JpaRepository<DocumentoIngresoEstudiante, Long> {}
