package co.edu.itp.ciecyt.web.rest;

import static co.edu.itp.ciecyt.domain.ProyectoAsserts.*;
import static co.edu.itp.ciecyt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.itp.ciecyt.IntegrationTest;
import co.edu.itp.ciecyt.domain.Proyecto;
import co.edu.itp.ciecyt.repository.ProyectoRepository;
import co.edu.itp.ciecyt.service.dto.ProyectoDTO;
import co.edu.itp.ciecyt.service.mapper.ProyectoMapper;
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
 * Integration tests for the {@link ProyectoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProyectoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proyectos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private ProyectoMapper proyectoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProyectoMockMvc;

    private Proyecto proyecto;

    private Proyecto insertedProyecto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return proyecto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proyecto createUpdatedEntity(EntityManager em) {
        Proyecto proyecto = new Proyecto().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return proyecto;
    }

    @BeforeEach
    public void initTest() {
        proyecto = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProyecto != null) {
            proyectoRepository.delete(insertedProyecto);
            insertedProyecto = null;
        }
    }

    @Test
    @Transactional
    void createProyecto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);
        var returnedProyectoDTO = om.readValue(
            restProyectoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyectoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProyectoDTO.class
        );

        // Validate the Proyecto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProyecto = proyectoMapper.toEntity(returnedProyectoDTO);
        assertProyectoUpdatableFieldsEquals(returnedProyecto, getPersistedProyecto(returnedProyecto));

        insertedProyecto = returnedProyecto;
    }

    @Test
    @Transactional
    void createProyectoWithExistingId() throws Exception {
        // Create the Proyecto with an existing ID
        proyecto.setId(1L);
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyectoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proyecto.setNombre(null);

        // Create the Proyecto, which fails.
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        restProyectoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyectoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProyectos() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectoList
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proyecto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getProyecto() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        // Get the proyecto
        restProyectoMockMvc
            .perform(get(ENTITY_API_URL_ID, proyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proyecto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingProyecto() throws Exception {
        // Get the proyecto
        restProyectoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProyecto() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proyecto
        Proyecto updatedProyecto = proyectoRepository.findById(proyecto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProyecto are not directly saved in db
        em.detach(updatedProyecto);
        updatedProyecto.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(updatedProyecto);

        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proyectoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProyectoToMatchAllProperties(updatedProyecto);
    }

    @Test
    @Transactional
    void putNonExistingProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proyectoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proyectoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto.descripcion(UPDATED_DESCRIPCION);

        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProyectoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProyecto, proyecto), getPersistedProyecto(proyecto));
    }

    @Test
    @Transactional
    void fullUpdateProyectoWithPatch() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proyecto using partial update
        Proyecto partialUpdatedProyecto = new Proyecto();
        partialUpdatedProyecto.setId(proyecto.getId());

        partialUpdatedProyecto.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProyecto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProyecto))
            )
            .andExpect(status().isOk());

        // Validate the Proyecto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProyectoUpdatableFieldsEquals(partialUpdatedProyecto, getPersistedProyecto(partialUpdatedProyecto));
    }

    @Test
    @Transactional
    void patchNonExistingProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proyectoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proyectoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProyecto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proyecto.setId(longCount.incrementAndGet());

        // Create the Proyecto
        ProyectoDTO proyectoDTO = proyectoMapper.toDto(proyecto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProyectoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proyectoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proyecto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProyecto() throws Exception {
        // Initialize the database
        insertedProyecto = proyectoRepository.saveAndFlush(proyecto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proyecto
        restProyectoMockMvc
            .perform(delete(ENTITY_API_URL_ID, proyecto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proyectoRepository.count();
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

    protected Proyecto getPersistedProyecto(Proyecto proyecto) {
        return proyectoRepository.findById(proyecto.getId()).orElseThrow();
    }

    protected void assertPersistedProyectoToMatchAllProperties(Proyecto expectedProyecto) {
        assertProyectoAllPropertiesEquals(expectedProyecto, getPersistedProyecto(expectedProyecto));
    }

    protected void assertPersistedProyectoToMatchUpdatableProperties(Proyecto expectedProyecto) {
        assertProyectoAllUpdatablePropertiesEquals(expectedProyecto, getPersistedProyecto(expectedProyecto));
    }
}
