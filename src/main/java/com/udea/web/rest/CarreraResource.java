package com.udea.web.rest;

import com.udea.domain.Carrera;
import com.udea.repository.CarreraRepository;
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
 * REST controller for managing {@link com.udea.domain.Carrera}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CarreraResource {

    private final Logger log = LoggerFactory.getLogger(CarreraResource.class);

    private static final String ENTITY_NAME = "carrera";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarreraRepository carreraRepository;

    public CarreraResource(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    /**
     * {@code POST  /carreras} : Create a new carrera.
     *
     * @param carrera the carrera to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carrera, or with status {@code 400 (Bad Request)} if the carrera has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carreras")
    public ResponseEntity<Carrera> createCarrera(@RequestBody Carrera carrera) throws URISyntaxException {
        log.debug("REST request to save Carrera : {}", carrera);
        if (carrera.getId() != null) {
            throw new BadRequestAlertException("A new carrera cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Carrera result = carreraRepository.save(carrera);
        return ResponseEntity
            .created(new URI("/api/carreras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /carreras/:id} : Updates an existing carrera.
     *
     * @param id the id of the carrera to save.
     * @param carrera the carrera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carrera,
     * or with status {@code 400 (Bad Request)} if the carrera is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carrera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carreras/{id}")
    public ResponseEntity<Carrera> updateCarrera(@PathVariable(value = "id", required = false) final Long id, @RequestBody Carrera carrera)
        throws URISyntaxException {
        log.debug("REST request to update Carrera : {}, {}", id, carrera);
        if (carrera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carrera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carreraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Carrera result = carreraRepository.save(carrera);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carrera.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /carreras/:id} : Partial updates given fields of an existing carrera, field will ignore if it is null
     *
     * @param id the id of the carrera to save.
     * @param carrera the carrera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carrera,
     * or with status {@code 400 (Bad Request)} if the carrera is not valid,
     * or with status {@code 404 (Not Found)} if the carrera is not found,
     * or with status {@code 500 (Internal Server Error)} if the carrera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carreras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Carrera> partialUpdateCarrera(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Carrera carrera
    ) throws URISyntaxException {
        log.debug("REST request to partial update Carrera partially : {}, {}", id, carrera);
        if (carrera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carrera.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carreraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Carrera> result = carreraRepository
            .findById(carrera.getId())
            .map(existingCarrera -> {
                if (carrera.getNombre() != null) {
                    existingCarrera.setNombre(carrera.getNombre());
                }
                if (carrera.getModalidad() != null) {
                    existingCarrera.setModalidad(carrera.getModalidad());
                }

                return existingCarrera;
            })
            .map(carreraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carrera.getId().toString())
        );
    }

    /**
     * {@code GET  /carreras} : get all the carreras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carreras in body.
     */
    @GetMapping("/carreras")
    public ResponseEntity<List<Carrera>> getAllCarreras(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Carreras");
        Page<Carrera> page = carreraRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /carreras/:id} : get the "id" carrera.
     *
     * @param id the id of the carrera to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carrera, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carreras/{id}")
    public ResponseEntity<Carrera> getCarrera(@PathVariable Long id) {
        log.debug("REST request to get Carrera : {}", id);
        Optional<Carrera> carrera = carreraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(carrera);
    }

    /**
     * {@code DELETE  /carreras/:id} : delete the "id" carrera.
     *
     * @param id the id of the carrera to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carreras/{id}")
    public ResponseEntity<Void> deleteCarrera(@PathVariable Long id) {
        log.debug("REST request to delete Carrera : {}", id);
        carreraRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
