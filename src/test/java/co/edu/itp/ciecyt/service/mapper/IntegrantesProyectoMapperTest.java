package co.edu.itp.ciecyt.service.mapper;

import static co.edu.itp.ciecyt.domain.IntegrantesProyectoAsserts.*;
import static co.edu.itp.ciecyt.domain.IntegrantesProyectoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegrantesProyectoMapperTest {

    private IntegrantesProyectoMapper integrantesProyectoMapper;

    @BeforeEach
    void setUp() {
        integrantesProyectoMapper = new IntegrantesProyectoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIntegrantesProyectoSample1();
        var actual = integrantesProyectoMapper.toEntity(integrantesProyectoMapper.toDto(expected));
        assertIntegrantesProyectoAllPropertiesEquals(expected, actual);
    }
}
