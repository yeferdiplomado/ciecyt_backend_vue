package co.edu.itp.ciecyt.domain;

import static co.edu.itp.ciecyt.domain.ElementoProyectoTestSamples.*;
import static co.edu.itp.ciecyt.domain.ElementoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.itp.ciecyt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ElementoProyectoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElementoProyecto.class);
        ElementoProyecto elementoProyecto1 = getElementoProyectoSample1();
        ElementoProyecto elementoProyecto2 = new ElementoProyecto();
        assertThat(elementoProyecto1).isNotEqualTo(elementoProyecto2);

        elementoProyecto2.setId(elementoProyecto1.getId());
        assertThat(elementoProyecto1).isEqualTo(elementoProyecto2);

        elementoProyecto2 = getElementoProyectoSample2();
        assertThat(elementoProyecto1).isNotEqualTo(elementoProyecto2);
    }

    @Test
    void elementoTest() {
        ElementoProyecto elementoProyecto = getElementoProyectoRandomSampleGenerator();
        Elemento elementoBack = getElementoRandomSampleGenerator();

        elementoProyecto.setElemento(elementoBack);
        assertThat(elementoProyecto.getElemento()).isEqualTo(elementoBack);

        elementoProyecto.elemento(null);
        assertThat(elementoProyecto.getElemento()).isNull();
    }
}
