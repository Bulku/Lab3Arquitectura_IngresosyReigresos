package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.Sede;
import com.udea.repository.SedeRepository;
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
 * Integration tests for the {@link SedeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SedeResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sedes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSedeMockMvc;

    private Sede sede;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sede createEntity(EntityManager em) {
        Sede sede = new Sede().nombre(DEFAULT_NOMBRE).direccion(DEFAULT_DIRECCION);
        return sede;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sede createUpdatedEntity(EntityManager em) {
        Sede sede = new Sede().nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION);
        return sede;
    }

    @BeforeEach
    public void initTest() {
        sede = createEntity(em);
    }

    @Test
    @Transactional
    void createSede() throws Exception {
        int databaseSizeBeforeCreate = sedeRepository.findAll().size();
        // Create the Sede
        restSedeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isCreated());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate + 1);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSede.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void createSedeWithExistingId() throws Exception {
        // Create the Sede with an existing ID
        sede.setId(1L);

        int databaseSizeBeforeCreate = sedeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSedeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSedes() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        // Get all the sedeList
        restSedeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sede.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));
    }

    @Test
    @Transactional
    void getSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        // Get the sede
        restSedeMockMvc
            .perform(get(ENTITY_API_URL_ID, sede.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sede.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION));
    }

    @Test
    @Transactional
    void getNonExistingSede() throws Exception {
        // Get the sede
        restSedeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Update the sede
        Sede updatedSede = sedeRepository.findById(sede.getId()).get();
        // Disconnect from session so that the updates on updatedSede are not directly saved in db
        em.detach(updatedSede);
        updatedSede.nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION);

        restSedeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSede.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSede))
            )
            .andExpect(status().isOk());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void putNonExistingSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();
        sede.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSedeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sede.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sede))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();
        sede.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSedeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sede))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();
        sede.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSedeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSedeWithPatch() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Update the sede using partial update
        Sede partialUpdatedSede = new Sede();
        partialUpdatedSede.setId(sede.getId());

        partialUpdatedSede.nombre(UPDATED_NOMBRE);

        restSedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSede.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSede))
            )
            .andExpect(status().isOk());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void fullUpdateSedeWithPatch() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();

        // Update the sede using partial update
        Sede partialUpdatedSede = new Sede();
        partialUpdatedSede.setId(sede.getId());

        partialUpdatedSede.nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION);

        restSedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSede.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSede))
            )
            .andExpect(status().isOk());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
        Sede testSede = sedeList.get(sedeList.size() - 1);
        assertThat(testSede.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSede.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void patchNonExistingSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();
        sede.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sede.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sede))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();
        sede.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSedeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sede))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSede() throws Exception {
        int databaseSizeBeforeUpdate = sedeRepository.findAll().size();
        sede.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSedeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sede)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sede in the database
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSede() throws Exception {
        // Initialize the database
        sedeRepository.saveAndFlush(sede);

        int databaseSizeBeforeDelete = sedeRepository.findAll().size();

        // Delete the sede
        restSedeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sede.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sede> sedeList = sedeRepository.findAll();
        assertThat(sedeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
