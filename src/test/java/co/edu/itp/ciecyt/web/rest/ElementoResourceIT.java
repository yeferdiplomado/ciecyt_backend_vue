package co.edu.itp.ciecyt.web.rest;

import static co.edu.itp.ciecyt.domain.ElementoAsserts.*;
import static co.edu.itp.ciecyt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.itp.ciecyt.IntegrationTest;
import co.edu.itp.ciecyt.domain.Elemento;
import co.edu.itp.ciecyt.repository.ElementoRepository;
import co.edu.itp.ciecyt.service.dto.ElementoDTO;
import co.edu.itp.ciecyt.service.mapper.ElementoMapper;
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
 * Integration tests for the {@link ElementoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ElementoResourceIT {

    private static final String DEFAULT_ELEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_ELEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/elementos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ElementoRepository elementoRepository;

    @Autowired
    private ElementoMapper elementoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restElementoMockMvc;

    private Elemento elemento;

    private Elemento insertedElemento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Elemento createEntity(EntityManager em) {
        Elemento elemento = new Elemento().elemento(DEFAULT_ELEMENTO).descripcion(DEFAULT_DESCRIPCION);
        return elemento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Elemento createUpdatedEntity(EntityManager em) {
        Elemento elemento = new Elemento().elemento(UPDATED_ELEMENTO).descripcion(UPDATED_DESCRIPCION);
        return elemento;
    }

    @BeforeEach
    public void initTest() {
        elemento = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedElemento != null) {
            elementoRepository.delete(insertedElemento);
            insertedElemento = null;
        }
    }

    @Test
    @Transactional
    void createElemento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);
        var returnedElementoDTO = om.readValue(
            restElementoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ElementoDTO.class
        );

        // Validate the Elemento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedElemento = elementoMapper.toEntity(returnedElementoDTO);
        assertElementoUpdatableFieldsEquals(returnedElemento, getPersistedElemento(returnedElemento));

        insertedElemento = returnedElemento;
    }

    @Test
    @Transactional
    void createElementoWithExistingId() throws Exception {
        // Create the Elemento with an existing ID
        elemento.setId(1L);
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkElementoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        elemento.setElemento(null);

        // Create the Elemento, which fails.
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        restElementoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllElementos() throws Exception {
        // Initialize the database
        insertedElemento = elementoRepository.saveAndFlush(elemento);

        // Get all the elementoList
        restElementoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elemento.getId().intValue())))
            .andExpect(jsonPath("$.[*].elemento").value(hasItem(DEFAULT_ELEMENTO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getElemento() throws Exception {
        // Initialize the database
        insertedElemento = elementoRepository.saveAndFlush(elemento);

        // Get the elemento
        restElementoMockMvc
            .perform(get(ENTITY_API_URL_ID, elemento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(elemento.getId().intValue()))
            .andExpect(jsonPath("$.elemento").value(DEFAULT_ELEMENTO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingElemento() throws Exception {
        // Get the elemento
        restElementoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingElemento() throws Exception {
        // Initialize the database
        insertedElemento = elementoRepository.saveAndFlush(elemento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the elemento
        Elemento updatedElemento = elementoRepository.findById(elemento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedElemento are not directly saved in db
        em.detach(updatedElemento);
        updatedElemento.elemento(UPDATED_ELEMENTO).descripcion(UPDATED_DESCRIPCION);
        ElementoDTO elementoDTO = elementoMapper.toDto(updatedElemento);

        restElementoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elementoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(elementoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedElementoToMatchAllProperties(updatedElemento);
    }

    @Test
    @Transactional
    void putNonExistingElemento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elemento.setId(longCount.incrementAndGet());

        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, elementoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(elementoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchElemento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elemento.setId(longCount.incrementAndGet());

        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(elementoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamElemento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elemento.setId(longCount.incrementAndGet());

        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(elementoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateElementoWithPatch() throws Exception {
        // Initialize the database
        insertedElemento = elementoRepository.saveAndFlush(elemento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the elemento using partial update
        Elemento partialUpdatedElemento = new Elemento();
        partialUpdatedElemento.setId(elemento.getId());

        restElementoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElemento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedElemento))
            )
            .andExpect(status().isOk());

        // Validate the Elemento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertElementoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedElemento, elemento), getPersistedElemento(elemento));
    }

    @Test
    @Transactional
    void fullUpdateElementoWithPatch() throws Exception {
        // Initialize the database
        insertedElemento = elementoRepository.saveAndFlush(elemento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the elemento using partial update
        Elemento partialUpdatedElemento = new Elemento();
        partialUpdatedElemento.setId(elemento.getId());

        partialUpdatedElemento.elemento(UPDATED_ELEMENTO).descripcion(UPDATED_DESCRIPCION);

        restElementoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElemento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedElemento))
            )
            .andExpect(status().isOk());

        // Validate the Elemento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertElementoUpdatableFieldsEquals(partialUpdatedElemento, getPersistedElemento(partialUpdatedElemento));
    }

    @Test
    @Transactional
    void patchNonExistingElemento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elemento.setId(longCount.incrementAndGet());

        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, elementoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(elementoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchElemento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elemento.setId(longCount.incrementAndGet());

        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(elementoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamElemento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        elemento.setId(longCount.incrementAndGet());

        // Create the Elemento
        ElementoDTO elementoDTO = elementoMapper.toDto(elemento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElementoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(elementoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Elemento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteElemento() throws Exception {
        // Initialize the database
        insertedElemento = elementoRepository.saveAndFlush(elemento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the elemento
        restElementoMockMvc
            .perform(delete(ENTITY_API_URL_ID, elemento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return elementoRepository.count();
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

    protected Elemento getPersistedElemento(Elemento elemento) {
        return elementoRepository.findById(elemento.getId()).orElseThrow();
    }

    protected void assertPersistedElementoToMatchAllProperties(Elemento expectedElemento) {
        assertElementoAllPropertiesEquals(expectedElemento, getPersistedElemento(expectedElemento));
    }

    protected void assertPersistedElementoToMatchUpdatableProperties(Elemento expectedElemento) {
        assertElementoAllUpdatablePropertiesEquals(expectedElemento, getPersistedElemento(expectedElemento));
    }
}
