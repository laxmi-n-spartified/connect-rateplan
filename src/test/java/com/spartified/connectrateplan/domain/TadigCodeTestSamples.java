package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TadigCodeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TadigCode getTadigCodeSample1() {
        return new TadigCode().id(1L).tadig("tadig1");
    }

    public static TadigCode getTadigCodeSample2() {
        return new TadigCode().id(2L).tadig("tadig2");
    }

    public static TadigCode getTadigCodeRandomSampleGenerator() {
        return new TadigCode().id(longCount.incrementAndGet()).tadig(UUID.randomUUID().toString());
    }
}
