package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TierVolumeDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TierVolumeDetails getTierVolumeDetailsSample1() {
        return new TierVolumeDetails()
            .id(1L)
            .ratingTypeMrc("ratingTypeMrc1")
            .ratingTypeUsage("ratingTypeUsage1")
            .tenantId("tenantId1")
            .tierLimit("tierLimit1");
    }

    public static TierVolumeDetails getTierVolumeDetailsSample2() {
        return new TierVolumeDetails()
            .id(2L)
            .ratingTypeMrc("ratingTypeMrc2")
            .ratingTypeUsage("ratingTypeUsage2")
            .tenantId("tenantId2")
            .tierLimit("tierLimit2");
    }

    public static TierVolumeDetails getTierVolumeDetailsRandomSampleGenerator() {
        return new TierVolumeDetails()
            .id(longCount.incrementAndGet())
            .ratingTypeMrc(UUID.randomUUID().toString())
            .ratingTypeUsage(UUID.randomUUID().toString())
            .tenantId(UUID.randomUUID().toString())
            .tierLimit(UUID.randomUUID().toString());
    }
}
