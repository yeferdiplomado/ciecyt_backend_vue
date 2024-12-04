package co.edu.itp.ciecyt.service.mapper;

import co.edu.itp.ciecyt.domain.Elemento;
import co.edu.itp.ciecyt.domain.Proyecto;
import co.edu.itp.ciecyt.service.dto.ElementoDTO;
import co.edu.itp.ciecyt.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Elemento} and its DTO {@link ElementoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ElementoMapper extends EntityMapper<ElementoDTO, Elemento> {
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    ElementoDTO toDto(Elemento s);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);
}
