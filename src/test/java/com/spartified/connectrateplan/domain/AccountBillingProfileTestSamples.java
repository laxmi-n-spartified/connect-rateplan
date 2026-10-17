package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountBillingProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static AccountBillingProfile getAccountBillingProfileSample1() {
        return new AccountBillingProfile()
            .id(1L)
            .tenantId("tenantId1")
            .mrcChargeType("mrcChargeType1")
            .ratingType("ratingType1")
            .mrcRatingType("mrcRatingType1")
            .fanId("fanId1")
            .extId("extId1")
            .accountPrefix("accountPrefix1")
            .saartId("saartId1");
    }

    public static AccountBillingProfile getAccountBillingProfileSample2() {
        return new AccountBillingProfile()
            .id(2L)
            .tenantId("tenantId2")
            .mrcChargeType("mrcChargeType2")
            .ratingType("ratingType2")
            .mrcRatingType("mrcRatingType2")
            .fanId("fanId2")
            .extId("extId2")
            .accountPrefix("accountPrefix2")
            .saartId("saartId2");
    }

    public static AccountBillingProfile getAccountBillingProfileRandomSampleGenerator() {
        return new AccountBillingProfile()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .mrcChargeType(UUID.randomUUID().toString())
            .ratingType(UUID.randomUUID().toString())
            .mrcRatingType(UUID.randomUUID().toString())
            .fanId(UUID.randomUUID().toString())
            .extId(UUID.randomUUID().toString())
            .accountPrefix(UUID.randomUUID().toString())
            .saartId(UUID.randomUUID().toString());
    }
}
