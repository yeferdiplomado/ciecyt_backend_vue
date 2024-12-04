package co.edu.itp.ciecyt.service;

import co.edu.itp.ciecyt.service.dto.ProyectoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.itp.ciecyt.domain.Proyecto}.
 */
public interface ProyectoService {
    /**
     * Save a proyecto.
     *
     * @param proyectoDTO the entity to save.
     * @return the persisted entity.
     */
    ProyectoDTO save(ProyectoDTO proyectoDTO);

    /**
     * Updates a proyecto.
     *
     * @param proyectoDTO the entity to update.
     * @return the persisted entity.
     */
    ProyectoDTO update(ProyectoDTO proyectoDTO);

    /**
     * Partially updates a proyecto.
     *
     * @param proyectoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProyectoDTO> partialUpdate(ProyectoDTO proyectoDTO);

    /**
     * Get all the proyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProyectoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" proyecto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProyectoDTO> findOne(Long id);

    /**
     * Delete the "id" proyecto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
