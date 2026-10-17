package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StatesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static States getStatesSample1() {
        return new States().id(1L).lcStateName("lcStateName1");
    }

    public static States getStatesSample2() {
        return new States().id(2L).lcStateName("lcStateName2");
    }

    public static States getStatesRandomSampleGenerator() {
        return new States().id(longCount.incrementAndGet()).lcStateName(UUID.randomUUID().toString());
    }
}
