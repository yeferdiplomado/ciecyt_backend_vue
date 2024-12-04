package co.edu.itp.ciecyt.service;

import co.edu.itp.ciecyt.service.dto.ElementoProyectoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.itp.ciecyt.domain.ElementoProyecto}.
 */
public interface ElementoProyectoService {
    /**
     * Save a elementoProyecto.
     *
     * @param elementoProyectoDTO the entity to save.
     * @return the persisted entity.
     */
    ElementoProyectoDTO save(ElementoProyectoDTO elementoProyectoDTO);

    /**
     * Updates a elementoProyecto.
     *
     * @param elementoProyectoDTO the entity to update.
     * @return the persisted entity.
     */
    ElementoProyectoDTO update(ElementoProyectoDTO elementoProyectoDTO);

    /**
     * Partially updates a elementoProyecto.
     *
     * @param elementoProyectoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ElementoProyectoDTO> partialUpdate(ElementoProyectoDTO elementoProyectoDTO);

    /**
     * Get all the elementoProyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ElementoProyectoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" elementoProyecto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ElementoProyectoDTO> findOne(Long id);

    /**
     * Delete the "id" elementoProyecto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
