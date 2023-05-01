package com.udea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.udea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IngresoEstudianteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IngresoEstudiante.class);
        IngresoEstudiante ingresoEstudiante1 = new IngresoEstudiante();
        ingresoEstudiante1.setId(1L);
        IngresoEstudiante ingresoEstudiante2 = new IngresoEstudiante();
        ingresoEstudiante2.setId(ingresoEstudiante1.getId());
        assertThat(ingresoEstudiante1).isEqualTo(ingresoEstudiante2);
        ingresoEstudiante2.setId(2L);
        assertThat(ingresoEstudiante1).isNotEqualTo(ingresoEstudiante2);
        ingresoEstudiante1.setId(null);
        assertThat(ingresoEstudiante1).isNotEqualTo(ingresoEstudiante2);
    }
}
