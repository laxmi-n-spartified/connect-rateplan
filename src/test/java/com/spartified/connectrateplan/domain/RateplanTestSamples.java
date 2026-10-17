package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RateplanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Rateplan getRateplanSample1() {
        return new Rateplan()
            .id(1L)
            .tenantId("tenantId1")
            .name("name1")
            .organizationName("organizationName1")
            .contractPeriod(1)
            .noticePeriod(1)
            .status("status1")
            .planType("planType1")
            .version(1)
            .subscriptionType("subscriptionType1")
            .includedTestData(1L)
            .includedTestSms(1L)
            .includedTestVoice(1L)
            .trialDuration(1)
            .poolTariff("poolTariff1")
            .roundingForData("roundingForData1")
            .roundingForVoice("roundingForVoice1")
            .includedTestDataUnit("includedTestDataUnit1")
            .includedTestSmsUnit("includedTestSmsUnit1")
            .includedTestVoiceUnit("includedTestVoiceUnit1")
            .tieredVolumeType("tieredVolumeType1")
            .validityPeriod(1)
            .category("category1")
            .quotaProfileId(1L)
            .quotaProfileName("quotaProfileName1")
            .extId("extId1");
    }

    public static Rateplan getRateplanSample2() {
        return new Rateplan()
            .id(2L)
            .tenantId("tenantId2")
            .name("name2")
            .organizationName("organizationName2")
            .contractPeriod(2)
            .noticePeriod(2)
            .status("status2")
            .planType("planType2")
            .version(2)
            .subscriptionType("subscriptionType2")
            .includedTestData(2L)
            .includedTestSms(2L)
            .includedTestVoice(2L)
            .trialDuration(2)
            .poolTariff("poolTariff2")
            .roundingForData("roundingForData2")
            .roundingForVoice("roundingForVoice2")
            .includedTestDataUnit("includedTestDataUnit2")
            .includedTestSmsUnit("includedTestSmsUnit2")
            .includedTestVoiceUnit("includedTestVoiceUnit2")
            .tieredVolumeType("tieredVolumeType2")
            .validityPeriod(2)
            .category("category2")
            .quotaProfileId(2L)
            .quotaProfileName("quotaProfileName2")
            .extId("extId2");
    }

    public static Rateplan getRateplanRandomSampleGenerator() {
        return new Rateplan()
            .id(longCount.incrementAndGet())
            .tenantId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .organizationName(UUID.randomUUID().toString())
            .contractPeriod(intCount.incrementAndGet())
            .noticePeriod(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .planType(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet())
            .subscriptionType(UUID.randomUUID().toString())
            .includedTestData(longCount.incrementAndGet())
            .includedTestSms(longCount.incrementAndGet())
            .includedTestVoice(longCount.incrementAndGet())
            .trialDuration(intCount.incrementAndGet())
            .poolTariff(UUID.randomUUID().toString())
            .roundingForData(UUID.randomUUID().toString())
            .roundingForVoice(UUID.randomUUID().toString())
            .includedTestDataUnit(UUID.randomUUID().toString())
            .includedTestSmsUnit(UUID.randomUUID().toString())
            .includedTestVoiceUnit(UUID.randomUUID().toString())
            .tieredVolumeType(UUID.randomUUID().toString())
            .validityPeriod(intCount.incrementAndGet())
            .category(UUID.randomUUID().toString())
            .quotaProfileId(longCount.incrementAndGet())
            .quotaProfileName(UUID.randomUUID().toString())
            .extId(UUID.randomUUID().toString());
    }
}
