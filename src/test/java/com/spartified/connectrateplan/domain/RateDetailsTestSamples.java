package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RateDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static RateDetails getRateDetailsSample1() {
        return new RateDetails()
            .id(1L)
            .tenantId("tenantId1")
            .dataOverageRateUnit("dataOverageRateUnit1")
            .smsMoOverageRateUnit("smsMoOverageRateUnit1")
            .smsMtOverageRateUnit("smsMtOverageRateUnit1")
            .voiceMoOverageRateUnit("voiceMoOverageRateUnit1")
            .voiceMtOverageRateUnit("voiceMtOverageRateUnit1");
    }

    public static RateDetails getRateDetailsSample2() {
        return new RateDetails()
            .id(2L)
            .tenantId("tenantId2")
            .dataOverageRateUnit("dataOverageRateUnit2")
            .smsMoOverageRateUnit("smsMoOverageRateUnit2")
            .smsMtOverageRateUnit("smsMtOverageRateUnit2")
            .voiceMoOverageRateUnit("voiceMoOverageRateUnit2")
            .voiceMtOverageRateUnit("voiceMtOverageRateUnit2");
    }

    public static RateDetails getRateDetailsRandomSampleGenerator() {
        return new RateDetails()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .dataOverageRateUnit(UUID.randomUUID().toString())
            .smsMoOverageRateUnit(UUID.randomUUID().toString())
            .smsMtOverageRateUnit(UUID.randomUUID().toString())
            .voiceMoOverageRateUnit(UUID.randomUUID().toString())
            .voiceMtOverageRateUnit(UUID.randomUUID().toString());
    }
}
