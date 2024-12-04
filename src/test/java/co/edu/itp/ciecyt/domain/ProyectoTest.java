package co.edu.itp.ciecyt.domain;

import static co.edu.itp.ciecyt.domain.ElementoTestSamples.*;
import static co.edu.itp.ciecyt.domain.IntegrantesProyectoTestSamples.*;
import static co.edu.itp.ciecyt.domain.ProyectoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.itp.ciecyt.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProyectoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proyecto.class);
        Proyecto proyecto1 = getProyectoSample1();
        Proyecto proyecto2 = new Proyecto();
        assertThat(proyecto1).isNotEqualTo(proyecto2);

        proyecto2.setId(proyecto1.getId());
        assertThat(proyecto1).isEqualTo(proyecto2);

        proyecto2 = getProyectoSample2();
        assertThat(proyecto1).isNotEqualTo(proyecto2);
    }

    @Test
    void elementosTest() {
        Proyecto proyecto = getProyectoRandomSampleGenerator();
        Elemento elementoBack = getElementoRandomSampleGenerator();

        proyecto.addElementos(elementoBack);
        assertThat(proyecto.getElementos()).containsOnly(elementoBack);
        assertThat(elementoBack.getProyecto()).isEqualTo(proyecto);

        proyecto.removeElementos(elementoBack);
        assertThat(proyecto.getElementos()).doesNotContain(elementoBack);
        assertThat(elementoBack.getProyecto()).isNull();

        proyecto.elementos(new HashSet<>(Set.of(elementoBack)));
        assertThat(proyecto.getElementos()).containsOnly(elementoBack);
        assertThat(elementoBack.getProyecto()).isEqualTo(proyecto);

        proyecto.setElementos(new HashSet<>());
        assertThat(proyecto.getElementos()).doesNotContain(elementoBack);
        assertThat(elementoBack.getProyecto()).isNull();
    }

    @Test
    void integrantesTest() {
        Proyecto proyecto = getProyectoRandomSampleGenerator();
        IntegrantesProyecto integrantesProyectoBack = getIntegrantesProyectoRandomSampleGenerator();

        proyecto.addIntegrantes(integrantesProyectoBack);
        assertThat(proyecto.getIntegrantes()).containsOnly(integrantesProyectoBack);
        assertThat(integrantesProyectoBack.getProyecto()).isEqualTo(proyecto);

        proyecto.removeIntegrantes(integrantesProyectoBack);
        assertThat(proyecto.getIntegrantes()).doesNotContain(integrantesProyectoBack);
        assertThat(integrantesProyectoBack.getProyecto()).isNull();

        proyecto.integrantes(new HashSet<>(Set.of(integrantesProyectoBack)));
        assertThat(proyecto.getIntegrantes()).containsOnly(integrantesProyectoBack);
        assertThat(integrantesProyectoBack.getProyecto()).isEqualTo(proyecto);

        proyecto.setIntegrantes(new HashSet<>());
        assertThat(proyecto.getIntegrantes()).doesNotContain(integrantesProyectoBack);
        assertThat(integrantesProyectoBack.getProyecto()).isNull();
    }
}
