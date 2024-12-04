package co.edu.itp.ciecyt.domain;

import static co.edu.itp.ciecyt.domain.ElementoProyectoTestSamples.*;
import static co.edu.itp.ciecyt.domain.ElementoTestSamples.*;
import static co.edu.itp.ciecyt.domain.ProyectoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.itp.ciecyt.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ElementoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Elemento.class);
        Elemento elemento1 = getElementoSample1();
        Elemento elemento2 = new Elemento();
        assertThat(elemento1).isNotEqualTo(elemento2);

        elemento2.setId(elemento1.getId());
        assertThat(elemento1).isEqualTo(elemento2);

        elemento2 = getElementoSample2();
        assertThat(elemento1).isNotEqualTo(elemento2);
    }

    @Test
    void elementosProyectosTest() {
        Elemento elemento = getElementoRandomSampleGenerator();
        ElementoProyecto elementoProyectoBack = getElementoProyectoRandomSampleGenerator();

        elemento.addElementosProyectos(elementoProyectoBack);
        assertThat(elemento.getElementosProyectos()).containsOnly(elementoProyectoBack);
        assertThat(elementoProyectoBack.getElemento()).isEqualTo(elemento);

        elemento.removeElementosProyectos(elementoProyectoBack);
        assertThat(elemento.getElementosProyectos()).doesNotContain(elementoProyectoBack);
        assertThat(elementoProyectoBack.getElemento()).isNull();

        elemento.elementosProyectos(new HashSet<>(Set.of(elementoProyectoBack)));
        assertThat(elemento.getElementosProyectos()).containsOnly(elementoProyectoBack);
        assertThat(elementoProyectoBack.getElemento()).isEqualTo(elemento);

        elemento.setElementosProyectos(new HashSet<>());
        assertThat(elemento.getElementosProyectos()).doesNotContain(elementoProyectoBack);
        assertThat(elementoProyectoBack.getElemento()).isNull();
    }

    @Test
    void proyectoTest() {
        Elemento elemento = getElementoRandomSampleGenerator();
        Proyecto proyectoBack = getProyectoRandomSampleGenerator();

        elemento.setProyecto(proyectoBack);
        assertThat(elemento.getProyecto()).isEqualTo(proyectoBack);

        elemento.proyecto(null);
        assertThat(elemento.getProyecto()).isNull();
    }
}
