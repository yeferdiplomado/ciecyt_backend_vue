package co.edu.itp.ciecyt.domain;

import static co.edu.itp.ciecyt.domain.IntegrantesProyectoTestSamples.*;
import static co.edu.itp.ciecyt.domain.ProyectoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.itp.ciecyt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegrantesProyectoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegrantesProyecto.class);
        IntegrantesProyecto integrantesProyecto1 = getIntegrantesProyectoSample1();
        IntegrantesProyecto integrantesProyecto2 = new IntegrantesProyecto();
        assertThat(integrantesProyecto1).isNotEqualTo(integrantesProyecto2);

        integrantesProyecto2.setId(integrantesProyecto1.getId());
        assertThat(integrantesProyecto1).isEqualTo(integrantesProyecto2);

        integrantesProyecto2 = getIntegrantesProyectoSample2();
        assertThat(integrantesProyecto1).isNotEqualTo(integrantesProyecto2);
    }

    @Test
    void proyectoTest() {
        IntegrantesProyecto integrantesProyecto = getIntegrantesProyectoRandomSampleGenerator();
        Proyecto proyectoBack = getProyectoRandomSampleGenerator();

        integrantesProyecto.setProyecto(proyectoBack);
        assertThat(integrantesProyecto.getProyecto()).isEqualTo(proyectoBack);

        integrantesProyecto.proyecto(null);
        assertThat(integrantesProyecto.getProyecto()).isNull();
    }
}
