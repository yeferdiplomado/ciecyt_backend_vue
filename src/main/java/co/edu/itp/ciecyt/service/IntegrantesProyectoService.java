package co.edu.itp.ciecyt.service;

import co.edu.itp.ciecyt.service.dto.IntegrantesProyectoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.itp.ciecyt.domain.IntegrantesProyecto}.
 */
public interface IntegrantesProyectoService {
    /**
     * Save a integrantesProyecto.
     *
     * @param integrantesProyectoDTO the entity to save.
     * @return the persisted entity.
     */
    IntegrantesProyectoDTO save(IntegrantesProyectoDTO integrantesProyectoDTO);

    /**
     * Updates a integrantesProyecto.
     *
     * @param integrantesProyectoDTO the entity to update.
     * @return the persisted entity.
     */
    IntegrantesProyectoDTO update(IntegrantesProyectoDTO integrantesProyectoDTO);

    /**
     * Partially updates a integrantesProyecto.
     *
     * @param integrantesProyectoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IntegrantesProyectoDTO> partialUpdate(IntegrantesProyectoDTO integrantesProyectoDTO);

    /**
     * Get all the integrantesProyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IntegrantesProyectoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" integrantesProyecto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IntegrantesProyectoDTO> findOne(Long id);

    /**
     * Delete the "id" integrantesProyecto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
