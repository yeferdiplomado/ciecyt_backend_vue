package co.edu.itp.ciecyt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ElementoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Elemento getElementoSample1() {
        return new Elemento().id(1L).elemento("elemento1").descripcion("descripcion1");
    }

    public static Elemento getElementoSample2() {
        return new Elemento().id(2L).elemento("elemento2").descripcion("descripcion2");
    }

    public static Elemento getElementoRandomSampleGenerator() {
        return new Elemento()
            .id(longCount.incrementAndGet())
            .elemento(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
