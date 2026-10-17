package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CountryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Country getCountrySample1() {
        return new Country().id(1L).countryCode(1).countryName("countryName1").iso3166Code2("iso3166Code21").iso3166Code3("iso3166Code31");
    }

    public static Country getCountrySample2() {
        return new Country().id(2L).countryCode(2).countryName("countryName2").iso3166Code2("iso3166Code22").iso3166Code3("iso3166Code32");
    }

    public static Country getCountryRandomSampleGenerator() {
        return new Country()
            .id(longCount.incrementAndGet())
            .countryCode(intCount.incrementAndGet())
            .countryName(UUID.randomUUID().toString())
            .iso3166Code2(UUID.randomUUID().toString())
            .iso3166Code3(UUID.randomUUID().toString());
    }
}
