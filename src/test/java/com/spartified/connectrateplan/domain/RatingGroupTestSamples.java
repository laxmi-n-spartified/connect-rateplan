package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RatingGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static RatingGroup getRatingGroupSample1() {
        return new RatingGroup().id(1L).tenantId("tenantId1").rgValue("rgValue1");
    }

    public static RatingGroup getRatingGroupSample2() {
        return new RatingGroup().id(2L).tenantId("tenantId2").rgValue("rgValue2");
    }

    public static RatingGroup getRatingGroupRandomSampleGenerator() {
        return new RatingGroup()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .rgValue(UUID.randomUUID().toString());
    }
}
