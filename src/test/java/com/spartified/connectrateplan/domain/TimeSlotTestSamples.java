package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimeSlotTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TimeSlot getTimeSlotSample1() {
        return new TimeSlot().id(1L).name("name1").timeSlotStart("timeSlotStart1").timeSlotEnd("timeSlotEnd1");
    }

    public static TimeSlot getTimeSlotSample2() {
        return new TimeSlot().id(2L).name("name2").timeSlotStart("timeSlotStart2").timeSlotEnd("timeSlotEnd2");
    }

    public static TimeSlot getTimeSlotRandomSampleGenerator() {
        return new TimeSlot()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .timeSlotStart(UUID.randomUUID().toString())
            .timeSlotEnd(UUID.randomUUID().toString());
    }
}
