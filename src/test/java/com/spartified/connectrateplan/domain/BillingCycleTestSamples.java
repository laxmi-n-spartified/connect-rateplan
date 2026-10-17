package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BillingCycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static BillingCycle getBillingCycleSample1() {
        return new BillingCycle().id(1L).billCycleDate(1).billCycleName("billCycleName1").billCycleDuration("billCycleDuration1");
    }

    public static BillingCycle getBillingCycleSample2() {
        return new BillingCycle().id(2L).billCycleDate(2).billCycleName("billCycleName2").billCycleDuration("billCycleDuration2");
    }

    public static BillingCycle getBillingCycleRandomSampleGenerator() {
        return new BillingCycle()
            .id(longCount.incrementAndGet())
            .billCycleDate(intCount.incrementAndGet())
            .billCycleName(UUID.randomUUID().toString())
            .billCycleDuration(UUID.randomUUID().toString());
    }
}
