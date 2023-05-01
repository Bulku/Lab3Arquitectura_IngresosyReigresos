package com.udea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.udea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoIngresoEstudianteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoIngresoEstudiante.class);
        DocumentoIngresoEstudiante documentoIngresoEstudiante1 = new DocumentoIngresoEstudiante();
        documentoIngresoEstudiante1.setId(1L);
        DocumentoIngresoEstudiante documentoIngresoEstudiante2 = new DocumentoIngresoEstudiante();
        documentoIngresoEstudiante2.setId(documentoIngresoEstudiante1.getId());
        assertThat(documentoIngresoEstudiante1).isEqualTo(documentoIngresoEstudiante2);
        documentoIngresoEstudiante2.setId(2L);
        assertThat(documentoIngresoEstudiante1).isNotEqualTo(documentoIngresoEstudiante2);
        documentoIngresoEstudiante1.setId(null);
        assertThat(documentoIngresoEstudiante1).isNotEqualTo(documentoIngresoEstudiante2);
    }
}
