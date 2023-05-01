package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.SolicitudReingreso;
import com.udea.repository.SolicitudReingresoRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SolicitudReingresoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SolicitudReingresoResourceIT {

    private static final Instant DEFAULT_FECHA_SOLICITUD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SOLICITUD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/solicitud-reingresos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SolicitudReingresoRepository solicitudReingresoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSolicitudReingresoMockMvc;

    private SolicitudReingreso solicitudReingreso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolicitudReingreso createEntity(EntityManager em) {
        SolicitudReingreso solicitudReingreso = new SolicitudReingreso().fechaSolicitud(DEFAULT_FECHA_SOLICITUD).motivo(DEFAULT_MOTIVO);
        return solicitudReingreso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolicitudReingreso createUpdatedEntity(EntityManager em) {
        SolicitudReingreso solicitudReingreso = new SolicitudReingreso().fechaSolicitud(UPDATED_FECHA_SOLICITUD).motivo(UPDATED_MOTIVO);
        return solicitudReingreso;
    }

    @BeforeEach
    public void initTest() {
        solicitudReingreso = createEntity(em);
    }

    @Test
    @Transactional
    void createSolicitudReingreso() throws Exception {
        int databaseSizeBeforeCreate = solicitudReingresoRepository.findAll().size();
        // Create the SolicitudReingreso
        restSolicitudReingresoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isCreated());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeCreate + 1);
        SolicitudReingreso testSolicitudReingreso = solicitudReingresoList.get(solicitudReingresoList.size() - 1);
        assertThat(testSolicitudReingreso.getFechaSolicitud()).isEqualTo(DEFAULT_FECHA_SOLICITUD);
        assertThat(testSolicitudReingreso.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    void createSolicitudReingresoWithExistingId() throws Exception {
        // Create the SolicitudReingreso with an existing ID
        solicitudReingreso.setId(1L);

        int databaseSizeBeforeCreate = solicitudReingresoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitudReingresoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSolicitudReingresos() throws Exception {
        // Initialize the database
        solicitudReingresoRepository.saveAndFlush(solicitudReingreso);

        // Get all the solicitudReingresoList
        restSolicitudReingresoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitudReingreso.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaSolicitud").value(hasItem(DEFAULT_FECHA_SOLICITUD.toString())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)));
    }

    @Test
    @Transactional
    void getSolicitudReingreso() throws Exception {
        // Initialize the database
        solicitudReingresoRepository.saveAndFlush(solicitudReingreso);

        // Get the solicitudReingreso
        restSolicitudReingresoMockMvc
            .perform(get(ENTITY_API_URL_ID, solicitudReingreso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(solicitudReingreso.getId().intValue()))
            .andExpect(jsonPath("$.fechaSolicitud").value(DEFAULT_FECHA_SOLICITUD.toString()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO));
    }

    @Test
    @Transactional
    void getNonExistingSolicitudReingreso() throws Exception {
        // Get the solicitudReingreso
        restSolicitudReingresoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSolicitudReingreso() throws Exception {
        // Initialize the database
        solicitudReingresoRepository.saveAndFlush(solicitudReingreso);

        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();

        // Update the solicitudReingreso
        SolicitudReingreso updatedSolicitudReingreso = solicitudReingresoRepository.findById(solicitudReingreso.getId()).get();
        // Disconnect from session so that the updates on updatedSolicitudReingreso are not directly saved in db
        em.detach(updatedSolicitudReingreso);
        updatedSolicitudReingreso.fechaSolicitud(UPDATED_FECHA_SOLICITUD).motivo(UPDATED_MOTIVO);

        restSolicitudReingresoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSolicitudReingreso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSolicitudReingreso))
            )
            .andExpect(status().isOk());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
        SolicitudReingreso testSolicitudReingreso = solicitudReingresoList.get(solicitudReingresoList.size() - 1);
        assertThat(testSolicitudReingreso.getFechaSolicitud()).isEqualTo(UPDATED_FECHA_SOLICITUD);
        assertThat(testSolicitudReingreso.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void putNonExistingSolicitudReingreso() throws Exception {
        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();
        solicitudReingreso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitudReingresoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitudReingreso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSolicitudReingreso() throws Exception {
        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();
        solicitudReingreso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitudReingresoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSolicitudReingreso() throws Exception {
        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();
        solicitudReingreso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitudReingresoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSolicitudReingresoWithPatch() throws Exception {
        // Initialize the database
        solicitudReingresoRepository.saveAndFlush(solicitudReingreso);

        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();

        // Update the solicitudReingreso using partial update
        SolicitudReingreso partialUpdatedSolicitudReingreso = new SolicitudReingreso();
        partialUpdatedSolicitudReingreso.setId(solicitudReingreso.getId());

        partialUpdatedSolicitudReingreso.motivo(UPDATED_MOTIVO);

        restSolicitudReingresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitudReingreso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitudReingreso))
            )
            .andExpect(status().isOk());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
        SolicitudReingreso testSolicitudReingreso = solicitudReingresoList.get(solicitudReingresoList.size() - 1);
        assertThat(testSolicitudReingreso.getFechaSolicitud()).isEqualTo(DEFAULT_FECHA_SOLICITUD);
        assertThat(testSolicitudReingreso.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void fullUpdateSolicitudReingresoWithPatch() throws Exception {
        // Initialize the database
        solicitudReingresoRepository.saveAndFlush(solicitudReingreso);

        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();

        // Update the solicitudReingreso using partial update
        SolicitudReingreso partialUpdatedSolicitudReingreso = new SolicitudReingreso();
        partialUpdatedSolicitudReingreso.setId(solicitudReingreso.getId());

        partialUpdatedSolicitudReingreso.fechaSolicitud(UPDATED_FECHA_SOLICITUD).motivo(UPDATED_MOTIVO);

        restSolicitudReingresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitudReingreso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitudReingreso))
            )
            .andExpect(status().isOk());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
        SolicitudReingreso testSolicitudReingreso = solicitudReingresoList.get(solicitudReingresoList.size() - 1);
        assertThat(testSolicitudReingreso.getFechaSolicitud()).isEqualTo(UPDATED_FECHA_SOLICITUD);
        assertThat(testSolicitudReingreso.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void patchNonExistingSolicitudReingreso() throws Exception {
        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();
        solicitudReingreso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitudReingresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, solicitudReingreso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSolicitudReingreso() throws Exception {
        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();
        solicitudReingreso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitudReingresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isBadRequest());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSolicitudReingreso() throws Exception {
        int databaseSizeBeforeUpdate = solicitudReingresoRepository.findAll().size();
        solicitudReingreso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitudReingresoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitudReingreso))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SolicitudReingreso in the database
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSolicitudReingreso() throws Exception {
        // Initialize the database
        solicitudReingresoRepository.saveAndFlush(solicitudReingreso);

        int databaseSizeBeforeDelete = solicitudReingresoRepository.findAll().size();

        // Delete the solicitudReingreso
        restSolicitudReingresoMockMvc
            .perform(delete(ENTITY_API_URL_ID, solicitudReingreso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolicitudReingreso> solicitudReingresoList = solicitudReingresoRepository.findAll();
        assertThat(solicitudReingresoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
