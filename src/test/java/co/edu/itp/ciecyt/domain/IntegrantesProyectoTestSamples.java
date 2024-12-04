package co.edu.itp.ciecyt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IntegrantesProyectoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IntegrantesProyecto getIntegrantesProyectoSample1() {
        return new IntegrantesProyecto().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static IntegrantesProyecto getIntegrantesProyectoSample2() {
        return new IntegrantesProyecto().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static IntegrantesProyecto getIntegrantesProyectoRandomSampleGenerator() {
        return new IntegrantesProyecto()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
