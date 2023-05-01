package com.udea.web.rest;

import com.udea.domain.IngresoEstudiante;
import com.udea.repository.IngresoEstudianteRepository;
import com.udea.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.udea.domain.IngresoEstudiante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IngresoEstudianteResource {

    private final Logger log = LoggerFactory.getLogger(IngresoEstudianteResource.class);

    private static final String ENTITY_NAME = "ingresoEstudiante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngresoEstudianteRepository ingresoEstudianteRepository;

    public IngresoEstudianteResource(IngresoEstudianteRepository ingresoEstudianteRepository) {
        this.ingresoEstudianteRepository = ingresoEstudianteRepository;
    }

    /**
     * {@code POST  /ingreso-estudiantes} : Create a new ingresoEstudiante.
     *
     * @param ingresoEstudiante the ingresoEstudiante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingresoEstudiante, or with status {@code 400 (Bad Request)} if the ingresoEstudiante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingreso-estudiantes")
    public ResponseEntity<IngresoEstudiante> createIngresoEstudiante(@RequestBody IngresoEstudiante ingresoEstudiante)
        throws URISyntaxException {
        log.debug("REST request to save IngresoEstudiante : {}", ingresoEstudiante);
        if (ingresoEstudiante.getId() != null) {
            throw new BadRequestAlertException("A new ingresoEstudiante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IngresoEstudiante result = ingresoEstudianteRepository.save(ingresoEstudiante);
        return ResponseEntity
            .created(new URI("/api/ingreso-estudiantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingreso-estudiantes/:id} : Updates an existing ingresoEstudiante.
     *
     * @param id the id of the ingresoEstudiante to save.
     * @param ingresoEstudiante the ingresoEstudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingresoEstudiante,
     * or with status {@code 400 (Bad Request)} if the ingresoEstudiante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingresoEstudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingreso-estudiantes/{id}")
    public ResponseEntity<IngresoEstudiante> updateIngresoEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IngresoEstudiante ingresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to update IngresoEstudiante : {}, {}", id, ingresoEstudiante);
        if (ingresoEstudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingresoEstudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingresoEstudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IngresoEstudiante result = ingresoEstudianteRepository.save(ingresoEstudiante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingresoEstudiante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingreso-estudiantes/:id} : Partial updates given fields of an existing ingresoEstudiante, field will ignore if it is null
     *
     * @param id the id of the ingresoEstudiante to save.
     * @param ingresoEstudiante the ingresoEstudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingresoEstudiante,
     * or with status {@code 400 (Bad Request)} if the ingresoEstudiante is not valid,
     * or with status {@code 404 (Not Found)} if the ingresoEstudiante is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingresoEstudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingreso-estudiantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngresoEstudiante> partialUpdateIngresoEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IngresoEstudiante ingresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to partial update IngresoEstudiante partially : {}, {}", id, ingresoEstudiante);
        if (ingresoEstudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingresoEstudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingresoEstudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngresoEstudiante> result = ingresoEstudianteRepository
            .findById(ingresoEstudiante.getId())
            .map(existingIngresoEstudiante -> {
                if (ingresoEstudiante.getFechaIngreso() != null) {
                    existingIngresoEstudiante.setFechaIngreso(ingresoEstudiante.getFechaIngreso());
                }

                return existingIngresoEstudiante;
            })
            .map(ingresoEstudianteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingresoEstudiante.getId().toString())
        );
    }

    /**
     * {@code GET  /ingreso-estudiantes} : get all the ingresoEstudiantes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingresoEstudiantes in body.
     */
    @GetMapping("/ingreso-estudiantes")
    public ResponseEntity<List<IngresoEstudiante>> getAllIngresoEstudiantes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of IngresoEstudiantes");
        Page<IngresoEstudiante> page = ingresoEstudianteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ingreso-estudiantes/:id} : get the "id" ingresoEstudiante.
     *
     * @param id the id of the ingresoEstudiante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingresoEstudiante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingreso-estudiantes/{id}")
    public ResponseEntity<IngresoEstudiante> getIngresoEstudiante(@PathVariable Long id) {
        log.debug("REST request to get IngresoEstudiante : {}", id);
        Optional<IngresoEstudiante> ingresoEstudiante = ingresoEstudianteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ingresoEstudiante);
    }

    /**
     * {@code DELETE  /ingreso-estudiantes/:id} : delete the "id" ingresoEstudiante.
     *
     * @param id the id of the ingresoEstudiante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingreso-estudiantes/{id}")
    public ResponseEntity<Void> deleteIngresoEstudiante(@PathVariable Long id) {
        log.debug("REST request to delete IngresoEstudiante : {}", id);
        ingresoEstudianteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
