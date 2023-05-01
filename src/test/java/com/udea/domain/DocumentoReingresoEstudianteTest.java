package com.udea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.udea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentoReingresoEstudianteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoReingresoEstudiante.class);
        DocumentoReingresoEstudiante documentoReingresoEstudiante1 = new DocumentoReingresoEstudiante();
        documentoReingresoEstudiante1.setId(1L);
        DocumentoReingresoEstudiante documentoReingresoEstudiante2 = new DocumentoReingresoEstudiante();
        documentoReingresoEstudiante2.setId(documentoReingresoEstudiante1.getId());
        assertThat(documentoReingresoEstudiante1).isEqualTo(documentoReingresoEstudiante2);
        documentoReingresoEstudiante2.setId(2L);
        assertThat(documentoReingresoEstudiante1).isNotEqualTo(documentoReingresoEstudiante2);
        documentoReingresoEstudiante1.setId(null);
        assertThat(documentoReingresoEstudiante1).isNotEqualTo(documentoReingresoEstudiante2);
    }
}
