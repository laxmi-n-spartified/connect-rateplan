package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceListTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ServiceList getServiceListSample1() {
        return new ServiceList().id(1L).tenantId("tenantId1").servicetype("servicetype1");
    }

    public static ServiceList getServiceListSample2() {
        return new ServiceList().id(2L).tenantId("tenantId2").servicetype("servicetype2");
    }

    public static ServiceList getServiceListRandomSampleGenerator() {
        return new ServiceList()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .servicetype(UUID.randomUUID().toString());
    }
}
