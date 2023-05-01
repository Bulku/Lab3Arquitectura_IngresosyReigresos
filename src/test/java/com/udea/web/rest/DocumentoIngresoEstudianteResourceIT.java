package com.udea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.udea.IntegrationTest;
import com.udea.domain.DocumentoIngresoEstudiante;
import com.udea.repository.DocumentoIngresoEstudianteRepository;
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
 * Integration tests for the {@link DocumentoIngresoEstudianteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentoIngresoEstudianteResourceIT {

    private static final String DEFAULT_NOMBRE_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_DOCUMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documento-ingreso-estudiantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentoIngresoEstudianteRepository documentoIngresoEstudianteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoIngresoEstudianteMockMvc;

    private DocumentoIngresoEstudiante documentoIngresoEstudiante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoIngresoEstudiante createEntity(EntityManager em) {
        DocumentoIngresoEstudiante documentoIngresoEstudiante = new DocumentoIngresoEstudiante()
            .nombreDocumento(DEFAULT_NOMBRE_DOCUMENTO)
            .descripcionDocumento(DEFAULT_DESCRIPCION_DOCUMENTO);
        return documentoIngresoEstudiante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoIngresoEstudiante createUpdatedEntity(EntityManager em) {
        DocumentoIngresoEstudiante documentoIngresoEstudiante = new DocumentoIngresoEstudiante()
            .nombreDocumento(UPDATED_NOMBRE_DOCUMENTO)
            .descripcionDocumento(UPDATED_DESCRIPCION_DOCUMENTO);
        return documentoIngresoEstudiante;
    }

    @BeforeEach
    public void initTest() {
        documentoIngresoEstudiante = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeCreate = documentoIngresoEstudianteRepository.findAll().size();
        // Create the DocumentoIngresoEstudiante
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentoIngresoEstudiante testDocumentoIngresoEstudiante = documentoIngresoEstudianteList.get(
            documentoIngresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoIngresoEstudiante.getNombreDocumento()).isEqualTo(DEFAULT_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoIngresoEstudiante.getDescripcionDocumento()).isEqualTo(DEFAULT_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void createDocumentoIngresoEstudianteWithExistingId() throws Exception {
        // Create the DocumentoIngresoEstudiante with an existing ID
        documentoIngresoEstudiante.setId(1L);

        int databaseSizeBeforeCreate = documentoIngresoEstudianteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentoIngresoEstudiantes() throws Exception {
        // Initialize the database
        documentoIngresoEstudianteRepository.saveAndFlush(documentoIngresoEstudiante);

        // Get all the documentoIngresoEstudianteList
        restDocumentoIngresoEstudianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoIngresoEstudiante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreDocumento").value(hasItem(DEFAULT_NOMBRE_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].descripcionDocumento").value(hasItem(DEFAULT_DESCRIPCION_DOCUMENTO)));
    }

    @Test
    @Transactional
    void getDocumentoIngresoEstudiante() throws Exception {
        // Initialize the database
        documentoIngresoEstudianteRepository.saveAndFlush(documentoIngresoEstudiante);

        // Get the documentoIngresoEstudiante
        restDocumentoIngresoEstudianteMockMvc
            .perform(get(ENTITY_API_URL_ID, documentoIngresoEstudiante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentoIngresoEstudiante.getId().intValue()))
            .andExpect(jsonPath("$.nombreDocumento").value(DEFAULT_NOMBRE_DOCUMENTO))
            .andExpect(jsonPath("$.descripcionDocumento").value(DEFAULT_DESCRIPCION_DOCUMENTO));
    }

    @Test
    @Transactional
    void getNonExistingDocumentoIngresoEstudiante() throws Exception {
        // Get the documentoIngresoEstudiante
        restDocumentoIngresoEstudianteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentoIngresoEstudiante() throws Exception {
        // Initialize the database
        documentoIngresoEstudianteRepository.saveAndFlush(documentoIngresoEstudiante);

        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();

        // Update the documentoIngresoEstudiante
        DocumentoIngresoEstudiante updatedDocumentoIngresoEstudiante = documentoIngresoEstudianteRepository
            .findById(documentoIngresoEstudiante.getId())
            .get();
        // Disconnect from session so that the updates on updatedDocumentoIngresoEstudiante are not directly saved in db
        em.detach(updatedDocumentoIngresoEstudiante);
        updatedDocumentoIngresoEstudiante.nombreDocumento(UPDATED_NOMBRE_DOCUMENTO).descripcionDocumento(UPDATED_DESCRIPCION_DOCUMENTO);

        restDocumentoIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentoIngresoEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentoIngresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        DocumentoIngresoEstudiante testDocumentoIngresoEstudiante = documentoIngresoEstudianteList.get(
            documentoIngresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoIngresoEstudiante.getNombreDocumento()).isEqualTo(UPDATED_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoIngresoEstudiante.getDescripcionDocumento()).isEqualTo(UPDATED_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void putNonExistingDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();
        documentoIngresoEstudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoIngresoEstudiante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();
        documentoIngresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();
        documentoIngresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoIngresoEstudianteWithPatch() throws Exception {
        // Initialize the database
        documentoIngresoEstudianteRepository.saveAndFlush(documentoIngresoEstudiante);

        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();

        // Update the documentoIngresoEstudiante using partial update
        DocumentoIngresoEstudiante partialUpdatedDocumentoIngresoEstudiante = new DocumentoIngresoEstudiante();
        partialUpdatedDocumentoIngresoEstudiante.setId(documentoIngresoEstudiante.getId());

        restDocumentoIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoIngresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoIngresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        DocumentoIngresoEstudiante testDocumentoIngresoEstudiante = documentoIngresoEstudianteList.get(
            documentoIngresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoIngresoEstudiante.getNombreDocumento()).isEqualTo(DEFAULT_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoIngresoEstudiante.getDescripcionDocumento()).isEqualTo(DEFAULT_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void fullUpdateDocumentoIngresoEstudianteWithPatch() throws Exception {
        // Initialize the database
        documentoIngresoEstudianteRepository.saveAndFlush(documentoIngresoEstudiante);

        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();

        // Update the documentoIngresoEstudiante using partial update
        DocumentoIngresoEstudiante partialUpdatedDocumentoIngresoEstudiante = new DocumentoIngresoEstudiante();
        partialUpdatedDocumentoIngresoEstudiante.setId(documentoIngresoEstudiante.getId());

        partialUpdatedDocumentoIngresoEstudiante
            .nombreDocumento(UPDATED_NOMBRE_DOCUMENTO)
            .descripcionDocumento(UPDATED_DESCRIPCION_DOCUMENTO);

        restDocumentoIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoIngresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentoIngresoEstudiante))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
        DocumentoIngresoEstudiante testDocumentoIngresoEstudiante = documentoIngresoEstudianteList.get(
            documentoIngresoEstudianteList.size() - 1
        );
        assertThat(testDocumentoIngresoEstudiante.getNombreDocumento()).isEqualTo(UPDATED_NOMBRE_DOCUMENTO);
        assertThat(testDocumentoIngresoEstudiante.getDescripcionDocumento()).isEqualTo(UPDATED_DESCRIPCION_DOCUMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();
        documentoIngresoEstudiante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoIngresoEstudiante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();
        documentoIngresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentoIngresoEstudiante() throws Exception {
        int databaseSizeBeforeUpdate = documentoIngresoEstudianteRepository.findAll().size();
        documentoIngresoEstudiante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIngresoEstudianteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentoIngresoEstudiante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoIngresoEstudiante in the database
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentoIngresoEstudiante() throws Exception {
        // Initialize the database
        documentoIngresoEstudianteRepository.saveAndFlush(documentoIngresoEstudiante);

        int databaseSizeBeforeDelete = documentoIngresoEstudianteRepository.findAll().size();

        // Delete the documentoIngresoEstudiante
        restDocumentoIngresoEstudianteMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentoIngresoEstudiante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentoIngresoEstudiante> documentoIngresoEstudianteList = documentoIngresoEstudianteRepository.findAll();
        assertThat(documentoIngresoEstudianteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
