package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialNumberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static SpecialNumber getSpecialNumberSample1() {
        return new SpecialNumber().id(1L).tenantId("tenantId1").inputNum(1L).translatedNum(1L);
    }

    public static SpecialNumber getSpecialNumberSample2() {
        return new SpecialNumber().id(2L).tenantId("tenantId2").inputNum(2L).translatedNum(2L);
    }

    public static SpecialNumber getSpecialNumberRandomSampleGenerator() {
        return new SpecialNumber()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .inputNum(longCount.incrementAndGet())
            .translatedNum(longCount.incrementAndGet());
    }
}
