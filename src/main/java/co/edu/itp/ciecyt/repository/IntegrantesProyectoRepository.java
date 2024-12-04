package co.edu.itp.ciecyt.repository;

import co.edu.itp.ciecyt.domain.IntegrantesProyecto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IntegrantesProyecto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegrantesProyectoRepository extends JpaRepository<IntegrantesProyecto, Long> {}
