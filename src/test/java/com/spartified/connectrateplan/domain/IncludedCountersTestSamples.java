package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IncludedCountersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static IncludedCounters getIncludedCountersSample1() {
        return new IncludedCounters()
            .id(1L)
            .counterData(1L)
            .counterSmsMo(1L)
            .counterSmsMt(1L)
            .counterVoiceMo(1L)
            .counterVoiceMt(1L)
            .rolloverDataDuration(1)
            .rolloverVoiceMoDuration(1)
            .rolloverSmsMoDuration(1)
            .counterDataUnit("counterDataUnit1")
            .counterSmsMoUnit("counterSmsMoUnit1")
            .counterSmsMtUnit("counterSmsMtUnit1")
            .counterVoiceMoUnit("counterVoiceMoUnit1")
            .counterVoiceMtUnit("counterVoiceMtUnit1");
    }

    public static IncludedCounters getIncludedCountersSample2() {
        return new IncludedCounters()
            .id(2L)
            .counterData(2L)
            .counterSmsMo(2L)
            .counterSmsMt(2L)
            .counterVoiceMo(2L)
            .counterVoiceMt(2L)
            .rolloverDataDuration(2)
            .rolloverVoiceMoDuration(2)
            .rolloverSmsMoDuration(2)
            .counterDataUnit("counterDataUnit2")
            .counterSmsMoUnit("counterSmsMoUnit2")
            .counterSmsMtUnit("counterSmsMtUnit2")
            .counterVoiceMoUnit("counterVoiceMoUnit2")
            .counterVoiceMtUnit("counterVoiceMtUnit2");
    }

    public static IncludedCounters getIncludedCountersRandomSampleGenerator() {
        return new IncludedCounters()
            .id(longCount.incrementAndGet())
            .counterData(longCount.incrementAndGet())
            .counterSmsMo(longCount.incrementAndGet())
            .counterSmsMt(longCount.incrementAndGet())
            .counterVoiceMo(longCount.incrementAndGet())
            .counterVoiceMt(longCount.incrementAndGet())
            .rolloverDataDuration(intCount.incrementAndGet())
            .rolloverVoiceMoDuration(intCount.incrementAndGet())
            .rolloverSmsMoDuration(intCount.incrementAndGet())
            .counterDataUnit(UUID.randomUUID().toString())
            .counterSmsMoUnit(UUID.randomUUID().toString())
            .counterSmsMtUnit(UUID.randomUUID().toString())
            .counterVoiceMoUnit(UUID.randomUUID().toString())
            .counterVoiceMtUnit(UUID.randomUUID().toString());
    }
}
