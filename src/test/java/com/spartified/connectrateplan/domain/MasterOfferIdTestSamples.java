package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MasterOfferIdTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static MasterOfferId getMasterOfferIdSample1() {
        return new MasterOfferId().id(1L).offerId("offerId1").offerName("offerName1");
    }

    public static MasterOfferId getMasterOfferIdSample2() {
        return new MasterOfferId().id(2L).offerId("offerId2").offerName("offerName2");
    }

    public static MasterOfferId getMasterOfferIdRandomSampleGenerator() {
        return new MasterOfferId()
            .id(longCount.incrementAndGet())
            .offerId(UUID.randomUUID().toString())
            .offerName(UUID.randomUUID().toString());
    }
}
