package co.edu.itp.ciecyt.service.mapper;

import co.edu.itp.ciecyt.domain.IntegrantesProyecto;
import co.edu.itp.ciecyt.domain.Proyecto;
import co.edu.itp.ciecyt.service.dto.IntegrantesProyectoDTO;
import co.edu.itp.ciecyt.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IntegrantesProyecto} and its DTO {@link IntegrantesProyectoDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntegrantesProyectoMapper extends EntityMapper<IntegrantesProyectoDTO, IntegrantesProyecto> {
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    IntegrantesProyectoDTO toDto(IntegrantesProyecto s);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);
}
