package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RateplanLcStateModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static RateplanLcStateModel getRateplanLcStateModelSample1() {
        return new RateplanLcStateModel().id(1L).rateplanName("rateplanName1").tenantId("tenantId1").currentLcState("currentLcState1");
    }

    public static RateplanLcStateModel getRateplanLcStateModelSample2() {
        return new RateplanLcStateModel().id(2L).rateplanName("rateplanName2").tenantId("tenantId2").currentLcState("currentLcState2");
    }

    public static RateplanLcStateModel getRateplanLcStateModelRandomSampleGenerator() {
        return new RateplanLcStateModel()
            .id(longCount.incrementAndGet())
            .rateplanName(UUID.randomUUID().toString())
            .tenantId(UUID.randomUUID().toString())
            .currentLcState(UUID.randomUUID().toString());
    }
}
