package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.Carrera;
import com.udea.domain.enumeration.Modalidad;
import com.udea.repository.CarreraRepository;
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
 * Integration tests for the {@link CarreraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarreraResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Modalidad DEFAULT_MODALIDAD = Modalidad.PRESENCIAL;
    private static final Modalidad UPDATED_MODALIDAD = Modalidad.VIRTUAL;

    private static final String ENTITY_API_URL = "/api/carreras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarreraMockMvc;

    private Carrera carrera;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrera createEntity(EntityManager em) {
        Carrera carrera = new Carrera().nombre(DEFAULT_NOMBRE).modalidad(DEFAULT_MODALIDAD);
        return carrera;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrera createUpdatedEntity(EntityManager em) {
        Carrera carrera = new Carrera().nombre(UPDATED_NOMBRE).modalidad(UPDATED_MODALIDAD);
        return carrera;
    }

    @BeforeEach
    public void initTest() {
        carrera = createEntity(em);
    }

    @Test
    @Transactional
    void createCarrera() throws Exception {
        int databaseSizeBeforeCreate = carreraRepository.findAll().size();
        // Create the Carrera
        restCarreraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isCreated());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeCreate + 1);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCarrera.getModalidad()).isEqualTo(DEFAULT_MODALIDAD);
    }

    @Test
    @Transactional
    void createCarreraWithExistingId() throws Exception {
        // Create the Carrera with an existing ID
        carrera.setId(1L);

        int databaseSizeBeforeCreate = carreraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarreraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarreras() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        // Get all the carreraList
        restCarreraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrera.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].modalidad").value(hasItem(DEFAULT_MODALIDAD.toString())));
    }

    @Test
    @Transactional
    void getCarrera() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        // Get the carrera
        restCarreraMockMvc
            .perform(get(ENTITY_API_URL_ID, carrera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carrera.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.modalidad").value(DEFAULT_MODALIDAD.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCarrera() throws Exception {
        // Get the carrera
        restCarreraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarrera() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();

        // Update the carrera
        Carrera updatedCarrera = carreraRepository.findById(carrera.getId()).get();
        // Disconnect from session so that the updates on updatedCarrera are not directly saved in db
        em.detach(updatedCarrera);
        updatedCarrera.nombre(UPDATED_NOMBRE).modalidad(UPDATED_MODALIDAD);

        restCarreraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarrera.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCarrera))
            )
            .andExpect(status().isOk());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCarrera.getModalidad()).isEqualTo(UPDATED_MODALIDAD);
    }

    @Test
    @Transactional
    void putNonExistingCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carrera.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarreraWithPatch() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();

        // Update the carrera using partial update
        Carrera partialUpdatedCarrera = new Carrera();
        partialUpdatedCarrera.setId(carrera.getId());

        partialUpdatedCarrera.modalidad(UPDATED_MODALIDAD);

        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarrera))
            )
            .andExpect(status().isOk());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCarrera.getModalidad()).isEqualTo(UPDATED_MODALIDAD);
    }

    @Test
    @Transactional
    void fullUpdateCarreraWithPatch() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();

        // Update the carrera using partial update
        Carrera partialUpdatedCarrera = new Carrera();
        partialUpdatedCarrera.setId(carrera.getId());

        partialUpdatedCarrera.nombre(UPDATED_NOMBRE).modalidad(UPDATED_MODALIDAD);

        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarrera))
            )
            .andExpect(status().isOk());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
        Carrera testCarrera = carreraList.get(carreraList.size() - 1);
        assertThat(testCarrera.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCarrera.getModalidad()).isEqualTo(UPDATED_MODALIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carrera.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carrera))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarrera() throws Exception {
        int databaseSizeBeforeUpdate = carreraRepository.findAll().size();
        carrera.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarreraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carrera)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carrera in the database
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarrera() throws Exception {
        // Initialize the database
        carreraRepository.saveAndFlush(carrera);

        int databaseSizeBeforeDelete = carreraRepository.findAll().size();

        // Delete the carrera
        restCarreraMockMvc
            .perform(delete(ENTITY_API_URL_ID, carrera.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Carrera> carreraList = carreraRepository.findAll();
        assertThat(carreraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
