package co.edu.itp.ciecyt.service.mapper;

import static co.edu.itp.ciecyt.domain.ElementoProyectoAsserts.*;
import static co.edu.itp.ciecyt.domain.ElementoProyectoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElementoProyectoMapperTest {

    private ElementoProyectoMapper elementoProyectoMapper;

    @BeforeEach
    void setUp() {
        elementoProyectoMapper = new ElementoProyectoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getElementoProyectoSample1();
        var actual = elementoProyectoMapper.toEntity(elementoProyectoMapper.toDto(expected));
        assertElementoProyectoAllPropertiesEquals(expected, actual);
    }
}
