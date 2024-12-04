package co.edu.itp.ciecyt.service;

import co.edu.itp.ciecyt.service.dto.ElementoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.itp.ciecyt.domain.Elemento}.
 */
public interface ElementoService {
    /**
     * Save a elemento.
     *
     * @param elementoDTO the entity to save.
     * @return the persisted entity.
     */
    ElementoDTO save(ElementoDTO elementoDTO);

    /**
     * Updates a elemento.
     *
     * @param elementoDTO the entity to update.
     * @return the persisted entity.
     */
    ElementoDTO update(ElementoDTO elementoDTO);

    /**
     * Partially updates a elemento.
     *
     * @param elementoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ElementoDTO> partialUpdate(ElementoDTO elementoDTO);

    /**
     * Get all the elementos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ElementoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" elemento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ElementoDTO> findOne(Long id);

    /**
     * Delete the "id" elemento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
