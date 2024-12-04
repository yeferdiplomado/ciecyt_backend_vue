package co.edu.itp.ciecyt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ElementoProyectoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ElementoProyecto getElementoProyectoSample1() {
        return new ElementoProyecto().id(1L).descripcion("descripcion1");
    }

    public static ElementoProyecto getElementoProyectoSample2() {
        return new ElementoProyecto().id(2L).descripcion("descripcion2");
    }

    public static ElementoProyecto getElementoProyectoRandomSampleGenerator() {
        return new ElementoProyecto().id(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
    }
}
