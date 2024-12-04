package co.edu.itp.ciecyt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.itp.ciecyt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ElementoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElementoDTO.class);
        ElementoDTO elementoDTO1 = new ElementoDTO();
        elementoDTO1.setId(1L);
        ElementoDTO elementoDTO2 = new ElementoDTO();
        assertThat(elementoDTO1).isNotEqualTo(elementoDTO2);
        elementoDTO2.setId(elementoDTO1.getId());
        assertThat(elementoDTO1).isEqualTo(elementoDTO2);
        elementoDTO2.setId(2L);
        assertThat(elementoDTO1).isNotEqualTo(elementoDTO2);
        elementoDTO1.setId(null);
        assertThat(elementoDTO1).isNotEqualTo(elementoDTO2);
    }
}
