package co.edu.itp.ciecyt.service.mapper;

import static co.edu.itp.ciecyt.domain.ElementoAsserts.*;
import static co.edu.itp.ciecyt.domain.ElementoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElementoMapperTest {

    private ElementoMapper elementoMapper;

    @BeforeEach
    void setUp() {
        elementoMapper = new ElementoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getElementoSample1();
        var actual = elementoMapper.toEntity(elementoMapper.toDto(expected));
        assertElementoAllPropertiesEquals(expected, actual);
    }
}
