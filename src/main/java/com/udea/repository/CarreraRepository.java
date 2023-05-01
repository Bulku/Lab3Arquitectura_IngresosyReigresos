package com.udea.repository;

import com.udea.domain.Carrera;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Carrera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {}
