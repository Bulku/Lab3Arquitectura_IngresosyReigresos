package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.Pensum;
import com.udea.repository.PensumRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PensumResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PensumResourceIT {

    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    private static final String ENTITY_API_URL = "/api/pensums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PensumRepository pensumRepository;

    @Mock
    private PensumRepository pensumRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPensumMockMvc;

    private Pensum pensum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pensum createEntity(EntityManager em) {
        Pensum pensum = new Pensum().numero(DEFAULT_NUMERO);
        return pensum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pensum createUpdatedEntity(EntityManager em) {
        Pensum pensum = new Pensum().numero(UPDATED_NUMERO);
        return pensum;
    }

    @BeforeEach
    public void initTest() {
        pensum = createEntity(em);
    }

    @Test
    @Transactional
    void createPensum() throws Exception {
        int databaseSizeBeforeCreate = pensumRepository.findAll().size();
        // Create the Pensum
        restPensumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pensum)))
            .andExpect(status().isCreated());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeCreate + 1);
        Pensum testPensum = pensumList.get(pensumList.size() - 1);
        assertThat(testPensum.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void createPensumWithExistingId() throws Exception {
        // Create the Pensum with an existing ID
        pensum.setId(1L);

        int databaseSizeBeforeCreate = pensumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPensumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pensum)))
            .andExpect(status().isBadRequest());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPensums() throws Exception {
        // Initialize the database
        pensumRepository.saveAndFlush(pensum);

        // Get all the pensumList
        restPensumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pensum.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPensumsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pensumRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPensumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pensumRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPensumsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pensumRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPensumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pensumRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPensum() throws Exception {
        // Initialize the database
        pensumRepository.saveAndFlush(pensum);

        // Get the pensum
        restPensumMockMvc
            .perform(get(ENTITY_API_URL_ID, pensum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pensum.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPensum() throws Exception {
        // Get the pensum
        restPensumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPensum() throws Exception {
        // Initialize the database
        pensumRepository.saveAndFlush(pensum);

        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();

        // Update the pensum
        Pensum updatedPensum = pensumRepository.findById(pensum.getId()).get();
        // Disconnect from session so that the updates on updatedPensum are not directly saved in db
        em.detach(updatedPensum);
        updatedPensum.numero(UPDATED_NUMERO);

        restPensumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPensum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPensum))
            )
            .andExpect(status().isOk());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
        Pensum testPensum = pensumList.get(pensumList.size() - 1);
        assertThat(testPensum.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void putNonExistingPensum() throws Exception {
        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();
        pensum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPensumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pensum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pensum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPensum() throws Exception {
        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();
        pensum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPensumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pensum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPensum() throws Exception {
        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();
        pensum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPensumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pensum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePensumWithPatch() throws Exception {
        // Initialize the database
        pensumRepository.saveAndFlush(pensum);

        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();

        // Update the pensum using partial update
        Pensum partialUpdatedPensum = new Pensum();
        partialUpdatedPensum.setId(pensum.getId());

        restPensumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPensum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPensum))
            )
            .andExpect(status().isOk());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
        Pensum testPensum = pensumList.get(pensumList.size() - 1);
        assertThat(testPensum.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void fullUpdatePensumWithPatch() throws Exception {
        // Initialize the database
        pensumRepository.saveAndFlush(pensum);

        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();

        // Update the pensum using partial update
        Pensum partialUpdatedPensum = new Pensum();
        partialUpdatedPensum.setId(pensum.getId());

        partialUpdatedPensum.numero(UPDATED_NUMERO);

        restPensumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPensum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPensum))
            )
            .andExpect(status().isOk());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
        Pensum testPensum = pensumList.get(pensumList.size() - 1);
        assertThat(testPensum.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void patchNonExistingPensum() throws Exception {
        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();
        pensum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPensumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pensum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pensum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPensum() throws Exception {
        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();
        pensum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPensumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pensum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPensum() throws Exception {
        int databaseSizeBeforeUpdate = pensumRepository.findAll().size();
        pensum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPensumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pensum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pensum in the database
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePensum() throws Exception {
        // Initialize the database
        pensumRepository.saveAndFlush(pensum);

        int databaseSizeBeforeDelete = pensumRepository.findAll().size();

        // Delete the pensum
        restPensumMockMvc
            .perform(delete(ENTITY_API_URL_ID, pensum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pensum> pensumList = pensumRepository.findAll();
        assertThat(pensumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
