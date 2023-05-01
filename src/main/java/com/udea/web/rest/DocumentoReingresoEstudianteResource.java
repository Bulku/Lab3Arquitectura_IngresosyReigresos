package com.udea.web.rest;

import com.udea.domain.DocumentoReingresoEstudiante;
import com.udea.repository.DocumentoReingresoEstudianteRepository;
import com.udea.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.udea.domain.DocumentoReingresoEstudiante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentoReingresoEstudianteResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoReingresoEstudianteResource.class);

    private static final String ENTITY_NAME = "documentoReingresoEstudiante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentoReingresoEstudianteRepository documentoReingresoEstudianteRepository;

    public DocumentoReingresoEstudianteResource(DocumentoReingresoEstudianteRepository documentoReingresoEstudianteRepository) {
        this.documentoReingresoEstudianteRepository = documentoReingresoEstudianteRepository;
    }

    /**
     * {@code POST  /documento-reingreso-estudiantes} : Create a new documentoReingresoEstudiante.
     *
     * @param documentoReingresoEstudiante the documentoReingresoEstudiante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentoReingresoEstudiante, or with status {@code 400 (Bad Request)} if the documentoReingresoEstudiante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documento-reingreso-estudiantes")
    public ResponseEntity<DocumentoReingresoEstudiante> createDocumentoReingresoEstudiante(
        @RequestBody DocumentoReingresoEstudiante documentoReingresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to save DocumentoReingresoEstudiante : {}", documentoReingresoEstudiante);
        if (documentoReingresoEstudiante.getId() != null) {
            throw new BadRequestAlertException("A new documentoReingresoEstudiante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentoReingresoEstudiante result = documentoReingresoEstudianteRepository.save(documentoReingresoEstudiante);
        return ResponseEntity
            .created(new URI("/api/documento-reingreso-estudiantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /documento-reingreso-estudiantes/:id} : Updates an existing documentoReingresoEstudiante.
     *
     * @param id the id of the documentoReingresoEstudiante to save.
     * @param documentoReingresoEstudiante the documentoReingresoEstudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoReingresoEstudiante,
     * or with status {@code 400 (Bad Request)} if the documentoReingresoEstudiante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentoReingresoEstudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documento-reingreso-estudiantes/{id}")
    public ResponseEntity<DocumentoReingresoEstudiante> updateDocumentoReingresoEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoReingresoEstudiante documentoReingresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentoReingresoEstudiante : {}, {}", id, documentoReingresoEstudiante);
        if (documentoReingresoEstudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoReingresoEstudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoReingresoEstudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentoReingresoEstudiante result = documentoReingresoEstudianteRepository.save(documentoReingresoEstudiante);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoReingresoEstudiante.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /documento-reingreso-estudiantes/:id} : Partial updates given fields of an existing documentoReingresoEstudiante, field will ignore if it is null
     *
     * @param id the id of the documentoReingresoEstudiante to save.
     * @param documentoReingresoEstudiante the documentoReingresoEstudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoReingresoEstudiante,
     * or with status {@code 400 (Bad Request)} if the documentoReingresoEstudiante is not valid,
     * or with status {@code 404 (Not Found)} if the documentoReingresoEstudiante is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentoReingresoEstudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documento-reingreso-estudiantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentoReingresoEstudiante> partialUpdateDocumentoReingresoEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoReingresoEstudiante documentoReingresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentoReingresoEstudiante partially : {}, {}", id, documentoReingresoEstudiante);
        if (documentoReingresoEstudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoReingresoEstudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoReingresoEstudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentoReingresoEstudiante> result = documentoReingresoEstudianteRepository
            .findById(documentoReingresoEstudiante.getId())
            .map(existingDocumentoReingresoEstudiante -> {
                if (documentoReingresoEstudiante.getNombreDocumento() != null) {
                    existingDocumentoReingresoEstudiante.setNombreDocumento(documentoReingresoEstudiante.getNombreDocumento());
                }
                if (documentoReingresoEstudiante.getDescripcionDocumento() != null) {
                    existingDocumentoReingresoEstudiante.setDescripcionDocumento(documentoReingresoEstudiante.getDescripcionDocumento());
                }

                return existingDocumentoReingresoEstudiante;
            })
            .map(documentoReingresoEstudianteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoReingresoEstudiante.getId().toString())
        );
    }

    /**
     * {@code GET  /documento-reingreso-estudiantes} : get all the documentoReingresoEstudiantes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentoReingresoEstudiantes in body.
     */
    @GetMapping("/documento-reingreso-estudiantes")
    public List<DocumentoReingresoEstudiante> getAllDocumentoReingresoEstudiantes() {
        log.debug("REST request to get all DocumentoReingresoEstudiantes");
        return documentoReingresoEstudianteRepository.findAll();
    }

    /**
     * {@code GET  /documento-reingreso-estudiantes/:id} : get the "id" documentoReingresoEstudiante.
     *
     * @param id the id of the documentoReingresoEstudiante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentoReingresoEstudiante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/documento-reingreso-estudiantes/{id}")
    public ResponseEntity<DocumentoReingresoEstudiante> getDocumentoReingresoEstudiante(@PathVariable Long id) {
        log.debug("REST request to get DocumentoReingresoEstudiante : {}", id);
        Optional<DocumentoReingresoEstudiante> documentoReingresoEstudiante = documentoReingresoEstudianteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(documentoReingresoEstudiante);
    }

    /**
     * {@code DELETE  /documento-reingreso-estudiantes/:id} : delete the "id" documentoReingresoEstudiante.
     *
     * @param id the id of the documentoReingresoEstudiante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/documento-reingreso-estudiantes/{id}")
    public ResponseEntity<Void> deleteDocumentoReingresoEstudiante(@PathVariable Long id) {
        log.debug("REST request to delete DocumentoReingresoEstudiante : {}", id);
        documentoReingresoEstudianteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
