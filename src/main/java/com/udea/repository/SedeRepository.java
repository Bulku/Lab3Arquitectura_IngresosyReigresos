package com.udea.repository;

import com.udea.domain.Sede;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sede entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {}
