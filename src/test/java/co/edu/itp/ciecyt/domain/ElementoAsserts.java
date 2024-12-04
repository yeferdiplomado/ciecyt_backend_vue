package co.edu.itp.ciecyt.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertElementoAllPropertiesEquals(Elemento expected, Elemento actual) {
        assertElementoAutoGeneratedPropertiesEquals(expected, actual);
        assertElementoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertElementoAllUpdatablePropertiesEquals(Elemento expected, Elemento actual) {
        assertElementoUpdatableFieldsEquals(expected, actual);
        assertElementoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertElementoAutoGeneratedPropertiesEquals(Elemento expected, Elemento actual) {
        assertThat(expected)
            .as("Verify Elemento auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertElementoUpdatableFieldsEquals(Elemento expected, Elemento actual) {
        assertThat(expected)
            .as("Verify Elemento relevant properties")
            .satisfies(e -> assertThat(e.getElemento()).as("check elemento").isEqualTo(actual.getElemento()))
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertElementoUpdatableRelationshipsEquals(Elemento expected, Elemento actual) {
        assertThat(expected)
            .as("Verify Elemento relationships")
            .satisfies(e -> assertThat(e.getProyecto()).as("check proyecto").isEqualTo(actual.getProyecto()));
    }
}