package com.udea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.udea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SolicitudReingresoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolicitudReingreso.class);
        SolicitudReingreso solicitudReingreso1 = new SolicitudReingreso();
        solicitudReingreso1.setId(1L);
        SolicitudReingreso solicitudReingreso2 = new SolicitudReingreso();
        solicitudReingreso2.setId(solicitudReingreso1.getId());
        assertThat(solicitudReingreso1).isEqualTo(solicitudReingreso2);
        solicitudReingreso2.setId(2L);
        assertThat(solicitudReingreso1).isNotEqualTo(solicitudReingreso2);
        solicitudReingreso1.setId(null);
        assertThat(solicitudReingreso1).isNotEqualTo(solicitudReingreso2);
    }
}
