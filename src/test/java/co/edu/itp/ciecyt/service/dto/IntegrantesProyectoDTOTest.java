package co.edu.itp.ciecyt.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.itp.ciecyt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegrantesProyectoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegrantesProyectoDTO.class);
        IntegrantesProyectoDTO integrantesProyectoDTO1 = new IntegrantesProyectoDTO();
        integrantesProyectoDTO1.setId(1L);
        IntegrantesProyectoDTO integrantesProyectoDTO2 = new IntegrantesProyectoDTO();
        assertThat(integrantesProyectoDTO1).isNotEqualTo(integrantesProyectoDTO2);
        integrantesProyectoDTO2.setId(integrantesProyectoDTO1.getId());
        assertThat(integrantesProyectoDTO1).isEqualTo(integrantesProyectoDTO2);
        integrantesProyectoDTO2.setId(2L);
        assertThat(integrantesProyectoDTO1).isNotEqualTo(integrantesProyectoDTO2);
        integrantesProyectoDTO1.setId(null);
        assertThat(integrantesProyectoDTO1).isNotEqualTo(integrantesProyectoDTO2);
    }
}
