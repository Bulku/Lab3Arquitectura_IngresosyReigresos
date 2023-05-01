package com.udea.web.rest;

import com.udea.domain.SolicitudReingreso;
import com.udea.repository.SolicitudReingresoRepository;
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
 * REST controller for managing {@link com.udea.domain.SolicitudReingreso}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SolicitudReingresoResource {

    private final Logger log = LoggerFactory.getLogger(SolicitudReingresoResource.class);

    private static final String ENTITY_NAME = "solicitudReingreso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolicitudReingresoRepository solicitudReingresoRepository;

    public SolicitudReingresoResource(SolicitudReingresoRepository solicitudReingresoRepository) {
        this.solicitudReingresoRepository = solicitudReingresoRepository;
    }

    /**
     * {@code POST  /solicitud-reingresos} : Create a new solicitudReingreso.
     *
     * @param solicitudReingreso the solicitudReingreso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solicitudReingreso, or with status {@code 400 (Bad Request)} if the solicitudReingreso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/solicitud-reingresos")
    public ResponseEntity<SolicitudReingreso> createSolicitudReingreso(@RequestBody SolicitudReingreso solicitudReingreso)
        throws URISyntaxException {
        log.debug("REST request to save SolicitudReingreso : {}", solicitudReingreso);
        if (solicitudReingreso.getId() != null) {
            throw new BadRequestAlertException("A new solicitudReingreso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolicitudReingreso result = solicitudReingresoRepository.save(solicitudReingreso);
        return ResponseEntity
            .created(new URI("/api/solicitud-reingresos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solicitud-reingresos/:id} : Updates an existing solicitudReingreso.
     *
     * @param id the id of the solicitudReingreso to save.
     * @param solicitudReingreso the solicitudReingreso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitudReingreso,
     * or with status {@code 400 (Bad Request)} if the solicitudReingreso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solicitudReingreso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/solicitud-reingresos/{id}")
    public ResponseEntity<SolicitudReingreso> updateSolicitudReingreso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SolicitudReingreso solicitudReingreso
    ) throws URISyntaxException {
        log.debug("REST request to update SolicitudReingreso : {}, {}", id, solicitudReingreso);
        if (solicitudReingreso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solicitudReingreso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solicitudReingresoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SolicitudReingreso result = solicitudReingresoRepository.save(solicitudReingreso);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solicitudReingreso.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /solicitud-reingresos/:id} : Partial updates given fields of an existing solicitudReingreso, field will ignore if it is null
     *
     * @param id the id of the solicitudReingreso to save.
     * @param solicitudReingreso the solicitudReingreso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solicitudReingreso,
     * or with status {@code 400 (Bad Request)} if the solicitudReingreso is not valid,
     * or with status {@code 404 (Not Found)} if the solicitudReingreso is not found,
     * or with status {@code 500 (Internal Server Error)} if the solicitudReingreso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/solicitud-reingresos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SolicitudReingreso> partialUpdateSolicitudReingreso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SolicitudReingreso solicitudReingreso
    ) throws URISyntaxException {
        log.debug("REST request to partial update SolicitudReingreso partially : {}, {}", id, solicitudReingreso);
        if (solicitudReingreso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, solicitudReingreso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!solicitudReingresoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SolicitudReingreso> result = solicitudReingresoRepository
            .findById(solicitudReingreso.getId())
            .map(existingSolicitudReingreso -> {
                if (solicitudReingreso.getFechaSolicitud() != null) {
                    existingSolicitudReingreso.setFechaSolicitud(solicitudReingreso.getFechaSolicitud());
                }
                if (solicitudReingreso.getMotivo() != null) {
                    existingSolicitudReingreso.setMotivo(solicitudReingreso.getMotivo());
                }

                return existingSolicitudReingreso;
            })
            .map(solicitudReingresoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solicitudReingreso.getId().toString())
        );
    }

    /**
     * {@code GET  /solicitud-reingresos} : get all the solicitudReingresos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solicitudReingresos in body.
     */
    @GetMapping("/solicitud-reingresos")
    public List<SolicitudReingreso> getAllSolicitudReingresos() {
        log.debug("REST request to get all SolicitudReingresos");
        return solicitudReingresoRepository.findAll();
    }

    /**
     * {@code GET  /solicitud-reingresos/:id} : get the "id" solicitudReingreso.
     *
     * @param id the id of the solicitudReingreso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solicitudReingreso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/solicitud-reingresos/{id}")
    public ResponseEntity<SolicitudReingreso> getSolicitudReingreso(@PathVariable Long id) {
        log.debug("REST request to get SolicitudReingreso : {}", id);
        Optional<SolicitudReingreso> solicitudReingreso = solicitudReingresoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(solicitudReingreso);
    }

    /**
     * {@code DELETE  /solicitud-reingresos/:id} : delete the "id" solicitudReingreso.
     *
     * @param id the id of the solicitudReingreso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/solicitud-reingresos/{id}")
    public ResponseEntity<Void> deleteSolicitudReingreso(@PathVariable Long id) {
        log.debug("REST request to delete SolicitudReingreso : {}", id);
        solicitudReingresoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
