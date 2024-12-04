package co.edu.itp.ciecyt.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrantesProyectoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIntegrantesProyectoAllPropertiesEquals(IntegrantesProyecto expected, IntegrantesProyecto actual) {
        assertIntegrantesProyectoAutoGeneratedPropertiesEquals(expected, actual);
        assertIntegrantesProyectoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIntegrantesProyectoAllUpdatablePropertiesEquals(IntegrantesProyecto expected, IntegrantesProyecto actual) {
        assertIntegrantesProyectoUpdatableFieldsEquals(expected, actual);
        assertIntegrantesProyectoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIntegrantesProyectoAutoGeneratedPropertiesEquals(IntegrantesProyecto expected, IntegrantesProyecto actual) {
        assertThat(expected)
            .as("Verify IntegrantesProyecto auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIntegrantesProyectoUpdatableFieldsEquals(IntegrantesProyecto expected, IntegrantesProyecto actual) {
        assertThat(expected)
            .as("Verify IntegrantesProyecto relevant properties")
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertIntegrantesProyectoUpdatableRelationshipsEquals(IntegrantesProyecto expected, IntegrantesProyecto actual) {
        assertThat(expected)
            .as("Verify IntegrantesProyecto relationships")
            .satisfies(e -> assertThat(e.getProyecto()).as("check proyecto").isEqualTo(actual.getProyecto()));
    }
}