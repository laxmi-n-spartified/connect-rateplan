package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ZoneLabelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ZoneLabel getZoneLabelSample1() {
        return new ZoneLabel().id(1L).tenantId("tenantId1").name("name1").description("description1").status("status1");
    }

    public static ZoneLabel getZoneLabelSample2() {
        return new ZoneLabel().id(2L).tenantId("tenantId2").name("name2").description("description2").status("status2");
    }

    public static ZoneLabel getZoneLabelRandomSampleGenerator() {
        return new ZoneLabel()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
