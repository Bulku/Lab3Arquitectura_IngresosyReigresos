package com.udea.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.udea.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PensumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pensum.class);
        Pensum pensum1 = new Pensum();
        pensum1.setId(1L);
        Pensum pensum2 = new Pensum();
        pensum2.setId(pensum1.getId());
        assertThat(pensum1).isEqualTo(pensum2);
        pensum2.setId(2L);
        assertThat(pensum1).isNotEqualTo(pensum2);
        pensum1.setId(null);
        assertThat(pensum1).isNotEqualTo(pensum2);
    }
}
