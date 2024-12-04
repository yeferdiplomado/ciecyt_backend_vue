package co.edu.itp.ciecyt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProyectoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Proyecto getProyectoSample1() {
        return new Proyecto().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Proyecto getProyectoSample2() {
        return new Proyecto().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Proyecto getProyectoRandomSampleGenerator() {
        return new Proyecto()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
