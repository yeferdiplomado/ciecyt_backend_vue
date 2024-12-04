package co.edu.itp.ciecyt.service.mapper;

import co.edu.itp.ciecyt.domain.Proyecto;
import co.edu.itp.ciecyt.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proyecto} and its DTO {@link ProyectoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProyectoMapper extends EntityMapper<ProyectoDTO, Proyecto> {}
