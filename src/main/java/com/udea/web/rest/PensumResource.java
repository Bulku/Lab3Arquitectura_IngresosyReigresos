package com.udea.web.rest;

import com.udea.domain.Pensum;
import com.udea.repository.PensumRepository;
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
 * REST controller for managing {@link com.udea.domain.Pensum}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PensumResource {

    private final Logger log = LoggerFactory.getLogger(PensumResource.class);

    private static final String ENTITY_NAME = "pensum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PensumRepository pensumRepository;

    public PensumResource(PensumRepository pensumRepository) {
        this.pensumRepository = pensumRepository;
    }

    /**
     * {@code POST  /pensums} : Create a new pensum.
     *
     * @param pensum the pensum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pensum, or with status {@code 400 (Bad Request)} if the pensum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pensums")
    public ResponseEntity<Pensum> createPensum(@RequestBody Pensum pensum) throws URISyntaxException {
        log.debug("REST request to save Pensum : {}", pensum);
        if (pensum.getId() != null) {
            throw new BadRequestAlertException("A new pensum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pensum result = pensumRepository.save(pensum);
        return ResponseEntity
            .created(new URI("/api/pensums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pensums/:id} : Updates an existing pensum.
     *
     * @param id the id of the pensum to save.
     * @param pensum the pensum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pensum,
     * or with status {@code 400 (Bad Request)} if the pensum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pensum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pensums/{id}")
    public ResponseEntity<Pensum> updatePensum(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pensum pensum)
        throws URISyntaxException {
        log.debug("REST request to update Pensum : {}, {}", id, pensum);
        if (pensum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pensum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pensumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pensum result = pensumRepository.save(pensum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pensum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pensums/:id} : Partial updates given fields of an existing pensum, field will ignore if it is null
     *
     * @param id the id of the pensum to save.
     * @param pensum the pensum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pensum,
     * or with status {@code 400 (Bad Request)} if the pensum is not valid,
     * or with status {@code 404 (Not Found)} if the pensum is not found,
     * or with status {@code 500 (Internal Server Error)} if the pensum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pensums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pensum> partialUpdatePensum(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pensum pensum
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pensum partially : {}, {}", id, pensum);
        if (pensum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pensum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pensumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pensum> result = pensumRepository
            .findById(pensum.getId())
            .map(existingPensum -> {
                if (pensum.getNumero() != null) {
                    existingPensum.setNumero(pensum.getNumero());
                }

                return existingPensum;
            })
            .map(pensumRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pensum.getId().toString())
        );
    }

    /**
     * {@code GET  /pensums} : get all the pensums.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pensums in body.
     */
    @GetMapping("/pensums")
    public ResponseEntity<List<Pensum>> getAllPensums(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Pensums");
        Page<Pensum> page;
        if (eagerload) {
            page = pensumRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = pensumRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pensums/:id} : get the "id" pensum.
     *
     * @param id the id of the pensum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pensum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pensums/{id}")
    public ResponseEntity<Pensum> getPensum(@PathVariable Long id) {
        log.debug("REST request to get Pensum : {}", id);
        Optional<Pensum> pensum = pensumRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(pensum);
    }

    /**
     * {@code DELETE  /pensums/:id} : delete the "id" pensum.
     *
     * @param id the id of the pensum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pensums/{id}")
    public ResponseEntity<Void> deletePensum(@PathVariable Long id) {
        log.debug("REST request to delete Pensum : {}", id);
        pensumRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
