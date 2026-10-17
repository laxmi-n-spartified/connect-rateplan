package com.spartified.connectrateplan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StateTransitionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static StateTransitions getStateTransitionsSample1() {
        return new StateTransitions().id(1L).name("name1").fromState("fromState1").toState("toState1");
    }

    public static StateTransitions getStateTransitionsSample2() {
        return new StateTransitions().id(2L).name("name2").fromState("fromState2").toState("toState2");
    }

    public static StateTransitions getStateTransitionsRandomSampleGenerator() {
        return new StateTransitions()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .fromState(UUID.randomUUID().toString())
            .toState(UUID.randomUUID().toString());
    }
}
