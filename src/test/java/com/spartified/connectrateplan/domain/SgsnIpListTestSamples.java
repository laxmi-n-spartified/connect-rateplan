package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SgsnIpListTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static SgsnIpList getSgsnIpListSample1() {
        return new SgsnIpList().id(1L).sgsnIp("sgsnIp1").tenantId("tenantId1");
    }

    public static SgsnIpList getSgsnIpListSample2() {
        return new SgsnIpList().id(2L).sgsnIp("sgsnIp2").tenantId("tenantId2");
    }

    public static SgsnIpList getSgsnIpListRandomSampleGenerator() {
        return new SgsnIpList().id(longCount.incrementAndGet()).sgsnIp(UUID.randomUUID().toString()).tenantId(UUID.randomUUID().toString());
    }
}
