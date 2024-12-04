package co.edu.itp.ciecyt.service.mapper;

import co.edu.itp.ciecyt.domain.Elemento;
import co.edu.itp.ciecyt.domain.ElementoProyecto;
import co.edu.itp.ciecyt.service.dto.ElementoDTO;
import co.edu.itp.ciecyt.service.dto.ElementoProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ElementoProyecto} and its DTO {@link ElementoProyectoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ElementoProyectoMapper extends EntityMapper<ElementoProyectoDTO, ElementoProyecto> {
    @Mapping(target = "elemento", source = "elemento", qualifiedByName = "elementoId")
    ElementoProyectoDTO toDto(ElementoProyecto s);

    @Named("elementoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ElementoDTO toDtoElementoId(Elemento elemento);
}
