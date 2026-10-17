package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimeIntervalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TimeInterval getTimeIntervalSample1() {
        return new TimeInterval().id(1L).tenantId("tenantId1").name("name1");
    }

    public static TimeInterval getTimeIntervalSample2() {
        return new TimeInterval().id(2L).tenantId("tenantId2").name("name2");
    }

    public static TimeInterval getTimeIntervalRandomSampleGenerator() {
        return new TimeInterval().id(longCount.incrementAndGet()).tenantId(UUID.randomUUID().toString()).name(UUID.randomUUID().toString());
    }
}
