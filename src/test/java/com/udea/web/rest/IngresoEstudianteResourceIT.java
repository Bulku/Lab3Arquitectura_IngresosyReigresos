package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.IngresoEstudiante;
import com.udea.repository.IngresoEstudianteRepository;
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
 * Integration tests for the {@link IngresoEstudianteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngresoEstudianteResourceIT {

    private static final Instant DEFAULT_FECHA_INGRESO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INGRESO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ingreso-estudiantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngresoEstudianteRepository ingresoEstudianteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngresoEstudianteMockMvc;

    private IngresoEstudiante ingresoEstudiante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngresoEstudiante createEntity(EntityManager em) {
        IngresoEstudiante ingresoEstudiante = new IngresoEstudiante().fechaIngreso(DEFAULT_FECHA_INGRESO);
        return ingresoEstudiante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IngresoEstudiante createUpdatedEntity(EntityManager em) {
        IngresoEstudiante ingresoEstudiante = new IngresoEstudiante().fechaIngreso(UPDATED_FECHA_INGRESO);
        return ingresoEstudiante;
    }

    @BeforeEach
    public void initTest() {
        ingresoEstudiante = createEntity(em);
    }

    @Test
    @Transactional
    void createIngresoEstudiante() throws Exception {
        int databaseSizeBeforeCreate = ingresoEstudianteRepository.findAll().size();
        // Create the IngresoEstudiante
        restIngresoEstudianteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isCreated());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeCreate + 1);
        IngresoEstudiante testIngresoEstudiante = ingresoEstudianteList.get(ingresoEstudianteList.size() - 1);
        assertThat(testIngresoEstudiante.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void createIngresoEstudianteWithExistingId() throws Exception {
        // Create the IngresoEstudiante with an existing ID
        ingresoEstudiante.setId(1L);

        int databaseSizeBeforeCreate = ingresoEstudianteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngresoEstudianteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIngresoEstudiantes() throws Exception {
        // Initialize the database
        ingresoEstudianteRepository.saveAndFlush(ingresoEstudiante);

        // Get all the ingresoEstudianteList
        restIngresoEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingresoEstudiante.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())));
    }

    @Test
    @Transactional
    void getIngresoEstudiante() throws Exception {
        // Initialize the database
        ingresoEstudianteRepository.saveAndFlush(ingresoEstudiante);

        // Get the ingresoEstudiante
        restIngresoEstudianteMockMvc
            .perform(get(ENTITY_API_URL_ID, ingresoEstudiante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingresoEstudiante.getId().intValue()))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIngresoEstudiante() throws Exception {
        // Get the ingresoEstudiante
        restIngresoEstudianteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngresoEstudiante() throws Exception {
        // Initialize the database
        ingresoEstudianteRepository.saveAndFlush(ingresoEstudiante);

        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();

        // Update the ingresoEstudiante
        IngresoEstudiante updatedIngresoEstudiante = ingresoEstudianteRepository.findById(ingresoEstudiante.getId()).get();
        // Disconnect from session so that the updates on updatedIngresoEstudiante are not directly saved in db
        em.detach(updatedIngresoEstudiante);
        updatedIngresoEstudiante.fechaIngreso(UPDATED_FECHA_INGRESO);

        restIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIngresoEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIngresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        IngresoEstudiante testIngresoEstudiante = ingresoEstudianteList.get(ingresoEstudianteList.size() - 1);
        assertThat(testIngresoEstudiante.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void putNonExistingIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();
        ingresoEstudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingresoEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();
        ingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();
        ingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngresoEstudianteWithPatch() throws Exception {
        // Initialize the database
        ingresoEstudianteRepository.saveAndFlush(ingresoEstudiante);

        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();

        // Update the ingresoEstudiante using partial update
        IngresoEstudiante partialUpdatedIngresoEstudiante = new IngresoEstudiante();
        partialUpdatedIngresoEstudiante.setId(ingresoEstudiante.getId());

        restIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        IngresoEstudiante testIngresoEstudiante = ingresoEstudianteList.get(ingresoEstudianteList.size() - 1);
        assertThat(testIngresoEstudiante.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void fullUpdateIngresoEstudianteWithPatch() throws Exception {
        // Initialize the database
        ingresoEstudianteRepository.saveAndFlush(ingresoEstudiante);

        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();

        // Update the ingresoEstudiante using partial update
        IngresoEstudiante partialUpdatedIngresoEstudiante = new IngresoEstudiante();
        partialUpdatedIngresoEstudiante.setId(ingresoEstudiante.getId());

        partialUpdatedIngresoEstudiante.fechaIngreso(UPDATED_FECHA_INGRESO);

        restIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        IngresoEstudiante testIngresoEstudiante = ingresoEstudianteList.get(ingresoEstudianteList.size() - 1);
        assertThat(testIngresoEstudiante.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void patchNonExistingIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();
        ingresoEstudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();
        ingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = ingresoEstudianteRepository.findAll().size();
        ingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingresoEstudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IngresoEstudiante in the database
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngresoEstudiante() throws Exception {
        // Initialize the database
        ingresoEstudianteRepository.saveAndFlush(ingresoEstudiante);

        int databaseSizeBeforeDelete = ingresoEstudianteRepository.findAll().size();

        // Delete the ingresoEstudiante
        restIngresoEstudianteMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingresoEstudiante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IngresoEstudiante> ingresoEstudianteList = ingresoEstudianteRepository.findAll();
        assertThat(ingresoEstudianteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
