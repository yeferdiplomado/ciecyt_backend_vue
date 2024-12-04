package co.edu.itp.ciecyt.web.rest;

import static co.edu.itp.ciecyt.domain.IntegrantesProyectoAsserts.*;
import static co.edu.itp.ciecyt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.itp.ciecyt.IntegrationTest;
import co.edu.itp.ciecyt.domain.IntegrantesProyecto;
import co.edu.itp.ciecyt.repository.IntegrantesProyectoRepository;
import co.edu.itp.ciecyt.service.dto.IntegrantesProyectoDTO;
import co.edu.itp.ciecyt.service.mapper.IntegrantesProyectoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IntegrantesProyectoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntegrantesProyectoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/integrantes-proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IntegrantesProyectoRepository integrantesProyectoRepository;

    @Autowired
    private IntegrantesProyectoMapper integrantesProyectoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntegrantesProyectoMockMvc;

    private IntegrantesProyecto integrantesProyecto;

    private IntegrantesProyecto insertedIntegrantesProyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegrantesProyecto createEntity(EntityManager em) {
        IntegrantesProyecto integrantesProyecto = new IntegrantesProyecto().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return integrantesProyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegrantesProyecto createUpdatedEntity(EntityManager em) {
        IntegrantesProyecto integrantesProyecto = new IntegrantesProyecto().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return integrantesProyecto;
    }

    @BeforeEach
    public void initTest() {
        integrantesProyecto = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedIntegrantesProyecto != null) {
            integrantesProyectoRepository.delete(insertedIntegrantesProyecto);
            insertedIntegrantesProyecto = null;
        }
    }

    @Test
    @Transactional
    void createIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);
        var returnedIntegrantesProyectoDTO = om.readValue(
            restIntegrantesProyectoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integrantesProyectoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IntegrantesProyectoDTO.class
        );

        // Validate the IntegrantesProyecto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIntegrantesProyecto = integrantesProyectoMapper.toEntity(returnedIntegrantesProyectoDTO);
        assertIntegrantesProyectoUpdatableFieldsEquals(
            returnedIntegrantesProyecto,
            getPersistedIntegrantesProyecto(returnedIntegrantesProyecto)
        );

        insertedIntegrantesProyecto = returnedIntegrantesProyecto;
    }

    @Test
    @Transactional
    void createIntegrantesProyectoWithExistingId() throws Exception {
        // Create the IntegrantesProyecto with an existing ID
        integrantesProyecto.setId(1L);
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntegrantesProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integrantesProyectoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integrantesProyecto.setNombre(null);

        // Create the IntegrantesProyecto, which fails.
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        restIntegrantesProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integrantesProyectoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIntegrantesProyectos() throws Exception {
        // Initialize the database
        insertedIntegrantesProyecto = integrantesProyectoRepository.saveAndFlush(integrantesProyecto);

        // Get all the integrantesProyectoList
        restIntegrantesProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integrantesProyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getIntegrantesProyecto() throws Exception {
        // Initialize the database
        insertedIntegrantesProyecto = integrantesProyectoRepository.saveAndFlush(integrantesProyecto);

        // Get the integrantesProyecto
        restIntegrantesProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, integrantesProyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(integrantesProyecto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingIntegrantesProyecto() throws Exception {
        // Get the integrantesProyecto
        restIntegrantesProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntegrantesProyecto() throws Exception {
        // Initialize the database
        insertedIntegrantesProyecto = integrantesProyectoRepository.saveAndFlush(integrantesProyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integrantesProyecto
        IntegrantesProyecto updatedIntegrantesProyecto = integrantesProyectoRepository.findById(integrantesProyecto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIntegrantesProyecto are not directly saved in db
        em.detach(updatedIntegrantesProyecto);
        updatedIntegrantesProyecto.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(updatedIntegrantesProyecto);

        restIntegrantesProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, integrantesProyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(integrantesProyectoDTO))
            )
            .andExpect(status().isOk());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIntegrantesProyectoToMatchAllProperties(updatedIntegrantesProyecto);
    }

    @Test
    @Transactional
    void putNonExistingIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrantesProyecto.setId(longCount.incrementAndGet());

        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntegrantesProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, integrantesProyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(integrantesProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrantesProyecto.setId(longCount.incrementAndGet());

        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegrantesProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(integrantesProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrantesProyecto.setId(longCount.incrementAndGet());

        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegrantesProyectoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integrantesProyectoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntegrantesProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedIntegrantesProyecto = integrantesProyectoRepository.saveAndFlush(integrantesProyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integrantesProyecto using partial update
        IntegrantesProyecto partialUpdatedIntegrantesProyecto = new IntegrantesProyecto();
        partialUpdatedIntegrantesProyecto.setId(integrantesProyecto.getId());

        restIntegrantesProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntegrantesProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntegrantesProyecto))
            )
            .andExpect(status().isOk());

        // Validate the IntegrantesProyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntegrantesProyectoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIntegrantesProyecto, integrantesProyecto),
            getPersistedIntegrantesProyecto(integrantesProyecto)
        );
    }

    @Test
    @Transactional
    void fullUpdateIntegrantesProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedIntegrantesProyecto = integrantesProyectoRepository.saveAndFlush(integrantesProyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integrantesProyecto using partial update
        IntegrantesProyecto partialUpdatedIntegrantesProyecto = new IntegrantesProyecto();
        partialUpdatedIntegrantesProyecto.setId(integrantesProyecto.getId());

        partialUpdatedIntegrantesProyecto.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restIntegrantesProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntegrantesProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntegrantesProyecto))
            )
            .andExpect(status().isOk());

        // Validate the IntegrantesProyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntegrantesProyectoUpdatableFieldsEquals(
            partialUpdatedIntegrantesProyecto,
            getPersistedIntegrantesProyecto(partialUpdatedIntegrantesProyecto)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrantesProyecto.setId(longCount.incrementAndGet());

        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntegrantesProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, integrantesProyectoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(integrantesProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrantesProyecto.setId(longCount.incrementAndGet());

        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegrantesProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(integrantesProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntegrantesProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integrantesProyecto.setId(longCount.incrementAndGet());

        // Create the IntegrantesProyecto
        IntegrantesProyectoDTO integrantesProyectoDTO = integrantesProyectoMapper.toDto(integrantesProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegrantesProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(integrantesProyectoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IntegrantesProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntegrantesProyecto() throws Exception {
        // Initialize the database
        insertedIntegrantesProyecto = integrantesProyectoRepository.saveAndFlush(integrantesProyecto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the integrantesProyecto
        restIntegrantesProyectoMockMvc
            .perform(delete(ENTITY_API_URL_ID, integrantesProyecto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return integrantesProyectoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected IntegrantesProyecto getPersistedIntegrantesProyecto(IntegrantesProyecto integrantesProyecto) {
        return integrantesProyectoRepository.findById(integrantesProyecto.getId()).orElseThrow();
    }

    protected void assertPersistedIntegrantesProyectoToMatchAllProperties(IntegrantesProyecto expectedIntegrantesProyecto) {
        assertIntegrantesProyectoAllPropertiesEquals(
            expectedIntegrantesProyecto,
            getPersistedIntegrantesProyecto(expectedIntegrantesProyecto)
        );
    }

    protected void assertPersistedIntegrantesProyectoToMatchUpdatableProperties(IntegrantesProyecto expectedIntegrantesProyecto) {
        assertIntegrantesProyectoAllUpdatablePropertiesEquals(
            expectedIntegrantesProyecto,
            getPersistedIntegrantesProyecto(expectedIntegrantesProyecto)
        );
    }
}
