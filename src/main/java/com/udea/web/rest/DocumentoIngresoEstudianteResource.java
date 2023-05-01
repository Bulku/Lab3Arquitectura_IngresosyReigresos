package com.udea.web.rest;

import com.udea.domain.DocumentoIngresoEstudiante;
import com.udea.repository.DocumentoIngresoEstudianteRepository;
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
 * REST controller for managing {@link com.udea.domain.DocumentoIngresoEstudiante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentoIngresoEstudianteResource {

    private final Logger log = LoggerFactory.getLogger(DocumentoIngresoEstudianteResource.class);

    private static final String ENTITY_NAME = "documentoIngresoEstudiante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentoIngresoEstudianteRepository documentoIngresoEstudianteRepository;

    public DocumentoIngresoEstudianteResource(DocumentoIngresoEstudianteRepository documentoIngresoEstudianteRepository) {
        this.documentoIngresoEstudianteRepository = documentoIngresoEstudianteRepository;
    }

    /**
     * {@code POST  /documento-ingreso-estudiantes} : Create a new documentoIngresoEstudiante.
     *
     * @param documentoIngresoEstudiante the documentoIngresoEstudiante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentoIngresoEstudiante, or with status {@code 400 (Bad Request)} if the documentoIngresoEstudiante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documento-ingreso-estudiantes")
    public ResponseEntity<DocumentoIngresoEstudiante> createDocumentoIngresoEstudiante(
        @RequestBody DocumentoIngresoEstudiante documentoIngresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to save DocumentoIngresoEstudiante : {}", documentoIngresoEstudiante);
        if (documentoIngresoEstudiante.getId() != null) {
            throw new BadRequestAlertException("A new documentoIngresoEstudiante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentoIngresoEstudiante result = documentoIngresoEstudianteRepository.save(documentoIngresoEstudiante);
        return ResponseEntity
            .created(new URI("/api/documento-ingreso-estudiantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /documento-ingreso-estudiantes/:id} : Updates an existing documentoIngresoEstudiante.
     *
     * @param id the id of the documentoIngresoEstudiante to save.
     * @param documentoIngresoEstudiante the documentoIngresoEstudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoIngresoEstudiante,
     * or with status {@code 400 (Bad Request)} if the documentoIngresoEstudiante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentoIngresoEstudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documento-ingreso-estudiantes/{id}")
    public ResponseEntity<DocumentoIngresoEstudiante> updateDocumentoIngresoEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoIngresoEstudiante documentoIngresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentoIngresoEstudiante : {}, {}", id, documentoIngresoEstudiante);
        if (documentoIngresoEstudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoIngresoEstudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoIngresoEstudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentoIngresoEstudiante result = documentoIngresoEstudianteRepository.save(documentoIngresoEstudiante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoIngresoEstudiante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /documento-ingreso-estudiantes/:id} : Partial updates given fields of an existing documentoIngresoEstudiante, field will ignore if it is null
     *
     * @param id the id of the documentoIngresoEstudiante to save.
     * @param documentoIngresoEstudiante the documentoIngresoEstudiante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoIngresoEstudiante,
     * or with status {@code 400 (Bad Request)} if the documentoIngresoEstudiante is not valid,
     * or with status {@code 404 (Not Found)} if the documentoIngresoEstudiante is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentoIngresoEstudiante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documento-ingreso-estudiantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentoIngresoEstudiante> partialUpdateDocumentoIngresoEstudiante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentoIngresoEstudiante documentoIngresoEstudiante
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentoIngresoEstudiante partially : {}, {}", id, documentoIngresoEstudiante);
        if (documentoIngresoEstudiante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoIngresoEstudiante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoIngresoEstudianteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentoIngresoEstudiante> result = documentoIngresoEstudianteRepository
            .findById(documentoIngresoEstudiante.getId())
            .map(existingDocumentoIngresoEstudiante -> {
                if (documentoIngresoEstudiante.getNombreDocumento() != null) {
                    existingDocumentoIngresoEstudiante.setNombreDocumento(documentoIngresoEstudiante.getNombreDocumento());
                }
                if (documentoIngresoEstudiante.getDescripcionDocumento() != null) {
                    existingDocumentoIngresoEstudiante.setDescripcionDocumento(documentoIngresoEstudiante.getDescripcionDocumento());
                }

                return existingDocumentoIngresoEstudiante;
            })
            .map(documentoIngresoEstudianteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoIngresoEstudiante.getId().toString())
        );
    }

    /**
     * {@code GET  /documento-ingreso-estudiantes} : get all the documentoIngresoEstudiantes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentoIngresoEstudiantes in body.
     */
    @GetMapping("/documento-ingreso-estudiantes")
    public List<DocumentoIngresoEstudiante> getAllDocumentoIngresoEstudiantes() {
        log.debug("REST request to get all DocumentoIngresoEstudiantes");
        return documentoIngresoEstudianteRepository.findAll();
    }

    /**
     * {@code GET  /documento-ingreso-estudiantes/:id} : get the "id" documentoIngresoEstudiante.
     *
     * @param id the id of the documentoIngresoEstudiante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentoIngresoEstudiante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/documento-ingreso-estudiantes/{id}")
    public ResponseEntity<DocumentoIngresoEstudiante> getDocumentoIngresoEstudiante(@PathVariable Long id) {
        log.debug("REST request to get DocumentoIngresoEstudiante : {}", id);
        Optional<DocumentoIngresoEstudiante> documentoIngresoEstudiante = documentoIngresoEstudianteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(documentoIngresoEstudiante);
    }

    /**
     * {@code DELETE  /documento-ingreso-estudiantes/:id} : delete the "id" documentoIngresoEstudiante.
     *
     * @param id the id of the documentoIngresoEstudiante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/documento-ingreso-estudiantes/{id}")
    public ResponseEntity<Void> deleteDocumentoIngresoEstudiante(@PathVariable Long id) {
        log.debug("REST request to delete DocumentoIngresoEstudiante : {}", id);
        documentoIngresoEstudianteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
