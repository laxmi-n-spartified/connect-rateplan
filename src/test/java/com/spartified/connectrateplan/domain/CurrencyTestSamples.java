package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CurrencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Currency getCurrencySample1() {
        return new Currency().id(1L).currencyName("currencyName1").currencyCode("currencyCode1");
    }

    public static Currency getCurrencySample2() {
        return new Currency().id(2L).currencyName("currencyName2").currencyCode("currencyCode2");
    }

    public static Currency getCurrencyRandomSampleGenerator() {
        return new Currency()
            .id(longCount.incrementAndGet())
            .currencyName(UUID.randomUUID().toString())
            .currencyCode(UUID.randomUUID().toString());
    }
}
