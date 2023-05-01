package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.DocumentoReingresoEstudiante;
import com.udea.repository.DocumentoReingresoEstudianteRepository;
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
 * Integration tests for the {@link DocumentoReingresoEstudianteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentoReingresoEstudianteResourceIT {

    private static final String DEFAULT_NOMBRE_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_DOCUMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documento-reingreso-estudiantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentoReingresoEstudianteRepository documentoReingresoEstudianteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoReingresoEstudianteMockMvc;

    private DocumentoReingresoEstudiante documentoReingresoEstudiante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoReingresoEstudiante createEntity(EntityManager em) {
        DocumentoReingresoEstudiante documentoReingresoEstudiante = new DocumentoReingresoEstudiante()
            .nombreDocumento(DEFAULT_NOMBRE_DOCUMENTO)
            .descripcionDocumento(DEFAULT_DESCRIPCION_DOCUMENTO);
        return documentoReingresoEstudiante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoReingresoEstudiante createUpdatedEntity(EntityManager em) {
        DocumentoReingresoEstudiante documentoReingresoEstudiante = new DocumentoReingresoEstudiante()
            .nombreDocumento(UPDATED_NOMBRE_DOCUMENTO)
            .descripcionDocumento(UPDATED_DESCRIPCION_DOCUMENTO);
        return documentoReingresoEstudiante;
    }

    @BeforeEach
    public void initTest() {
        documentoReingresoEstudiante = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeCreate = documentoReingresoEstudianteRepository.findAll().size();
        // Create the DocumentoReingresoEstudiante
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentoReingresoEstudiante testDocumentoReingresoEstudiante = documentoReingresoEstudianteList.get(
            documentoReingresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoReingresoEstudiante.getNombreDocumento()).isEqualTo(DEFAULT_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoReingresoEstudiante.getDescripcionDocumento()).isEqualTo(DEFAULT_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void createDocumentoReingresoEstudianteWithExistingId() throws Exception {
        // Create the DocumentoReingresoEstudiante with an existing ID
        documentoReingresoEstudiante.setId(1L);

        int databaseSizeBeforeCreate = documentoReingresoEstudianteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentoReingresoEstudiantes() throws Exception {
        // Initialize the database
        documentoReingresoEstudianteRepository.saveAndFlush(documentoReingresoEstudiante);

        // Get all the documentoReingresoEstudianteList
        restDocumentoReingresoEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoReingresoEstudiante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreDocumento").value(hasItem(DEFAULT_NOMBRE_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].descripcionDocumento").value(hasItem(DEFAULT_DESCRIPCION_DOCUMENTO)));
    }

    @Test
    @Transactional
    void getDocumentoReingresoEstudiante() throws Exception {
        // Initialize the database
        documentoReingresoEstudianteRepository.saveAndFlush(documentoReingresoEstudiante);

        // Get the documentoReingresoEstudiante
        restDocumentoReingresoEstudianteMockMvc
            .perform(get(ENTITY_API_URL_ID, documentoReingresoEstudiante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentoReingresoEstudiante.getId().intValue()))
            .andExpect(jsonPath("$.nombreDocumento").value(DEFAULT_NOMBRE_DOCUMENTO))
            .andExpect(jsonPath("$.descripcionDocumento").value(DEFAULT_DESCRIPCION_DOCUMENTO));
    }

    @Test
    @Transactional
    void getNonExistingDocumentoReingresoEstudiante() throws Exception {
        // Get the documentoReingresoEstudiante
        restDocumentoReingresoEstudianteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentoReingresoEstudiante() throws Exception {
        // Initialize the database
        documentoReingresoEstudianteRepository.saveAndFlush(documentoReingresoEstudiante);

        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();

        // Update the documentoReingresoEstudiante
        DocumentoReingresoEstudiante updatedDocumentoReingresoEstudiante = documentoReingresoEstudianteRepository
            .findById(documentoReingresoEstudiante.getId())
            .get();
        // Disconnect from session so that the updates on updatedDocumentoReingresoEstudiante are not directly saved in db
        em.detach(updatedDocumentoReingresoEstudiante);
        updatedDocumentoReingresoEstudiante.nombreDocumento(UPDATED_NOMBRE_DOCUMENTO).descripcionDocumento(UPDATED_DESCRIPCION_DOCUMENTO);

        restDocumentoReingresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentoReingresoEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentoReingresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        DocumentoReingresoEstudiante testDocumentoReingresoEstudiante = documentoReingresoEstudianteList.get(
            documentoReingresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoReingresoEstudiante.getNombreDocumento()).isEqualTo(UPDATED_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoReingresoEstudiante.getDescripcionDocumento()).isEqualTo(UPDATED_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void putNonExistingDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();
        documentoReingresoEstudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoReingresoEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();
        documentoReingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();
        documentoReingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoReingresoEstudianteWithPatch() throws Exception {
        // Initialize the database
        documentoReingresoEstudianteRepository.saveAndFlush(documentoReingresoEstudiante);

        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();

        // Update the documentoReingresoEstudiante using partial update
        DocumentoReingresoEstudiante partialUpdatedDocumentoReingresoEstudiante = new DocumentoReingresoEstudiante();
        partialUpdatedDocumentoReingresoEstudiante.setId(documentoReingresoEstudiante.getId());

        restDocumentoReingresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoReingresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoReingresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        DocumentoReingresoEstudiante testDocumentoReingresoEstudiante = documentoReingresoEstudianteList.get(
            documentoReingresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoReingresoEstudiante.getNombreDocumento()).isEqualTo(DEFAULT_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoReingresoEstudiante.getDescripcionDocumento()).isEqualTo(DEFAULT_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void fullUpdateDocumentoReingresoEstudianteWithPatch() throws Exception {
        // Initialize the database
        documentoReingresoEstudianteRepository.saveAndFlush(documentoReingresoEstudiante);

        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();

        // Update the documentoReingresoEstudiante using partial update
        DocumentoReingresoEstudiante partialUpdatedDocumentoReingresoEstudiante = new DocumentoReingresoEstudiante();
        partialUpdatedDocumentoReingresoEstudiante.setId(documentoReingresoEstudiante.getId());

        partialUpdatedDocumentoReingresoEstudiante
            .nombreDocumento(UPDATED_NOMBRE_DOCUMENTO)
            .descripcionDocumento(UPDATED_DESCRIPCION_DOCUMENTO);

        restDocumentoReingresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoReingresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoReingresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        DocumentoReingresoEstudiante testDocumentoReingresoEstudiante = documentoReingresoEstudianteList.get(
            documentoReingresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoReingresoEstudiante.getNombreDocumento()).isEqualTo(UPDATED_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoReingresoEstudiante.getDescripcionDocumento()).isEqualTo(UPDATED_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();
        documentoReingresoEstudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoReingresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();
        documentoReingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentoReingresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoReingresoEstudianteRepository.findAll().size();
        documentoReingresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoReingresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoReingresoEstudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoReingresoEstudiante in the database
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentoReingresoEstudiante() throws Exception {
        // Initialize the database
        documentoReingresoEstudianteRepository.saveAndFlush(documentoReingresoEstudiante);

        int databaseSizeBeforeDelete = documentoReingresoEstudianteRepository.findAll().size();

        // Delete the documentoReingresoEstudiante
        restDocumentoReingresoEstudianteMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentoReingresoEstudiante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentoReingresoEstudiante> documentoReingresoEstudianteList = documentoReingresoEstudianteRepository.findAll();
        assertThat(documentoReingresoEstudianteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
