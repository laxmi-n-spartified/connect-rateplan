package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NetworkIdentityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static NetworkIdentity getNetworkIdentitySample1() {
        return new NetworkIdentity().id(1L).mcc("mcc1").mnc("mnc1").operator("operator1");
    }

    public static NetworkIdentity getNetworkIdentitySample2() {
        return new NetworkIdentity().id(2L).mcc("mcc2").mnc("mnc2").operator("operator2");
    }

    public static NetworkIdentity getNetworkIdentityRandomSampleGenerator() {
        return new NetworkIdentity()
            .id(longCount.incrementAndGet())
            .mcc(UUID.randomUUID().toString())
            .mnc(UUID.randomUUID().toString())
            .operator(UUID.randomUUID().toString());
    }
}
