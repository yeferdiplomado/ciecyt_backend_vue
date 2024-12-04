package co.edu.itp.ciecyt.repository;

import co.edu.itp.ciecyt.domain.ElementoProyecto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ElementoProyecto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElementoProyectoRepository extends JpaRepository<ElementoProyecto, Long> {}
