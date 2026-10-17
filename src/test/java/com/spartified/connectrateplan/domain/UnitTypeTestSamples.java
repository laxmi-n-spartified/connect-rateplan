package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UnitTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static UnitType getUnitTypeSample1() {
        return new UnitType().id(1L).name("name1").conversionFactor(1).lowestUnit("lowestUnit1");
    }

    public static UnitType getUnitTypeSample2() {
        return new UnitType().id(2L).name("name2").conversionFactor(2).lowestUnit("lowestUnit2");
    }

    public static UnitType getUnitTypeRandomSampleGenerator() {
        return new UnitType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .conversionFactor(intCount.incrementAndGet())
            .lowestUnit(UUID.randomUUID().toString());
    }
}
