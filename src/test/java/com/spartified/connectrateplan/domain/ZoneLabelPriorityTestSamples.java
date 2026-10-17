package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ZoneLabelPriorityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ZoneLabelPriority getZoneLabelPrioritySample1() {
        return new ZoneLabelPriority().id(1L).labelType("labelType1").priority(1).tenantId("tenantId1");
    }

    public static ZoneLabelPriority getZoneLabelPrioritySample2() {
        return new ZoneLabelPriority().id(2L).labelType("labelType2").priority(2).tenantId("tenantId2");
    }

    public static ZoneLabelPriority getZoneLabelPriorityRandomSampleGenerator() {
        return new ZoneLabelPriority()
            .id(longCount.incrementAndGet())
            .labelType(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString());
    }
}
