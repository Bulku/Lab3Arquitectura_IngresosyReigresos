package com.udea.repository;

import com.udea.domain.SolicitudReingreso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SolicitudReingreso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolicitudReingresoRepository extends JpaRepository<SolicitudReingreso, Long> {}
