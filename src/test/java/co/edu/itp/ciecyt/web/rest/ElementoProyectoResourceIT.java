package co.edu.itp.ciecyt.web.rest;

import static co.edu.itp.ciecyt.domain.ElementoProyectoAsserts.*;
import static co.edu.itp.ciecyt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.itp.ciecyt.IntegrationTest;
import co.edu.itp.ciecyt.domain.ElementoProyecto;
import co.edu.itp.ciecyt.repository.ElementoProyectoRepository;
import co.edu.itp.ciecyt.service.dto.ElementoProyectoDTO;
import co.edu.itp.ciecyt.service.mapper.ElementoProyectoMapper;
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
 * Integration tests for the {@link ElementoProyectoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ElementoProyectoResourceIT {

    private static final String DEFAULT_DATO = "AAAAAAAAAA";
    private static final String UPDATED_DATO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/elemento-proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ElementoProyectoRepository elementoProyectoRepository;

    @Autowired
    private ElementoProyectoMapper elementoProyectoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restElementoProyectoMockMvc;

    private ElementoProyecto elementoProyecto;

    private ElementoProyecto insertedElementoProyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElementoProyecto createEntity(EntityManager em) {
        ElementoProyecto elementoProyecto = new ElementoProyecto().dato(DEFAULT_DATO).descripcion(DEFAULT_DESCRIPCION);
        return elementoProyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElementoProyecto createUpdatedEntity(EntityManager em) {
        ElementoProyecto elementoProyecto = new ElementoProyecto().dato(UPDATED_DATO).descripcion(UPDATED_DESCRIPCION);
        return elementoProyecto;
    }

    @BeforeEach
    public void initTest() {
        elementoProyecto = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedElementoProyecto != null) {
            elementoProyectoRepository.delete(insertedElementoProyecto);
            insertedElementoProyecto = null;
        }
    }

    @Test
    @Transactional
    void createElementoProyecto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);
        var returnedElementoProyectoDTO = om.readValue(
            restElementoProyectoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoProyectoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ElementoProyectoDTO.class
        );

        // Validate the ElementoProyecto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedElementoProyecto = elementoProyectoMapper.toEntity(returnedElementoProyectoDTO);
        assertElementoProyectoUpdatableFieldsEquals(returnedElementoProyecto, getPersistedElementoProyecto(returnedElementoProyecto));

        insertedElementoProyecto = returnedElementoProyecto;
    }

    @Test
    @Transactional
    void createElementoProyectoWithExistingId() throws Exception {
        // Create the ElementoProyecto with an existing ID
        elementoProyecto.setId(1L);
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementoProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoProyectoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllElementoProyectos() throws Exception {
        // Initialize the database
        insertedElementoProyecto = elementoProyectoRepository.saveAndFlush(elementoProyecto);

        // Get all the elementoProyectoList
        restElementoProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elementoProyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].dato").value(hasItem(DEFAULT_DATO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getElementoProyecto() throws Exception {
        // Initialize the database
        insertedElementoProyecto = elementoProyectoRepository.saveAndFlush(elementoProyecto);

        // Get the elementoProyecto
        restElementoProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, elementoProyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(elementoProyecto.getId().intValue()))
            .andExpect(jsonPath("$.dato").value(DEFAULT_DATO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingElementoProyecto() throws Exception {
        // Get the elementoProyecto
        restElementoProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingElementoProyecto() throws Exception {
        // Initialize the database
        insertedElementoProyecto = elementoProyectoRepository.saveAndFlush(elementoProyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the elementoProyecto
        ElementoProyecto updatedElementoProyecto = elementoProyectoRepository.findById(elementoProyecto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedElementoProyecto are not directly saved in db
        em.detach(updatedElementoProyecto);
        updatedElementoProyecto.dato(UPDATED_DATO).descripcion(UPDATED_DESCRIPCION);
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(updatedElementoProyecto);

        restElementoProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elementoProyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(elementoProyectoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedElementoProyectoToMatchAllProperties(updatedElementoProyecto);
    }

    @Test
    @Transactional
    void putNonExistingElementoProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elementoProyecto.setId(longCount.incrementAndGet());

        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementoProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elementoProyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(elementoProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchElementoProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elementoProyecto.setId(longCount.incrementAndGet());

        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(elementoProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamElementoProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elementoProyecto.setId(longCount.incrementAndGet());

        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoProyectoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoProyectoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateElementoProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedElementoProyecto = elementoProyectoRepository.saveAndFlush(elementoProyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the elementoProyecto using partial update
        ElementoProyecto partialUpdatedElementoProyecto = new ElementoProyecto();
        partialUpdatedElementoProyecto.setId(elementoProyecto.getId());

        partialUpdatedElementoProyecto.dato(UPDATED_DATO);

        restElementoProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElementoProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedElementoProyecto))
            )
            .andExpect(status().isOk());

        // Validate the ElementoProyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertElementoProyectoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedElementoProyecto, elementoProyecto),
            getPersistedElementoProyecto(elementoProyecto)
        );
    }

    @Test
    @Transactional
    void fullUpdateElementoProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedElementoProyecto = elementoProyectoRepository.saveAndFlush(elementoProyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the elementoProyecto using partial update
        ElementoProyecto partialUpdatedElementoProyecto = new ElementoProyecto();
        partialUpdatedElementoProyecto.setId(elementoProyecto.getId());

        partialUpdatedElementoProyecto.dato(UPDATED_DATO).descripcion(UPDATED_DESCRIPCION);

        restElementoProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElementoProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedElementoProyecto))
            )
            .andExpect(status().isOk());

        // Validate the ElementoProyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertElementoProyectoUpdatableFieldsEquals(
            partialUpdatedElementoProyecto,
            getPersistedElementoProyecto(partialUpdatedElementoProyecto)
        );
    }

    @Test
    @Transactional
    void patchNonExistingElementoProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elementoProyecto.setId(longCount.incrementAndGet());

        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementoProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, elementoProyectoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(elementoProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchElementoProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elementoProyecto.setId(longCount.incrementAndGet());

        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(elementoProyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamElementoProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elementoProyecto.setId(longCount.incrementAndGet());

        // Create the ElementoProyecto
        ElementoProyectoDTO elementoProyectoDTO = elementoProyectoMapper.toDto(elementoProyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoProyectoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(elementoProyectoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ElementoProyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteElementoProyecto() throws Exception {
        // Initialize the database
        insertedElementoProyecto = elementoProyectoRepository.saveAndFlush(elementoProyecto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the elementoProyecto
        restElementoProyectoMockMvc
            .perform(delete(ENTITY_API_URL_ID, elementoProyecto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return elementoProyectoRepository.count();
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

    protected ElementoProyecto getPersistedElementoProyecto(ElementoProyecto elementoProyecto) {
        return elementoProyectoRepository.findById(elementoProyecto.getId()).orElseThrow();
    }

    protected void assertPersistedElementoProyectoToMatchAllProperties(ElementoProyecto expectedElementoProyecto) {
        assertElementoProyectoAllPropertiesEquals(expectedElementoProyecto, getPersistedElementoProyecto(expectedElementoProyecto));
    }

    protected void assertPersistedElementoProyectoToMatchUpdatableProperties(ElementoProyecto expectedElementoProyecto) {
        assertElementoProyectoAllUpdatablePropertiesEquals(
            expectedElementoProyecto,
            getPersistedElementoProyecto(expectedElementoProyecto)
        );
    }
}
