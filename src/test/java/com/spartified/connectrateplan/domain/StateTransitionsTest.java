package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.LifecycleTestSamples.*;
import static com.spartified.connectrateplan.domain.StateTransitionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateTransitionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateTransitions.class);
        StateTransitions stateTransitions1 = getStateTransitionsSample1();
        StateTransitions stateTransitions2 = new StateTransitions();
        assertThat(stateTransitions1).isNotEqualTo(stateTransitions2);

        stateTransitions2.setId(stateTransitions1.getId());
        assertThat(stateTransitions1).isEqualTo(stateTransitions2);

        stateTransitions2 = getStateTransitionsSample2();
        assertThat(stateTransitions1).isNotEqualTo(stateTransitions2);
    }

    @Test
    void lifecycleTest() {
        StateTransitions stateTransitions = getStateTransitionsRandomSampleGenerator();
        Lifecycle lifecycleBack = getLifecycleRandomSampleGenerator();

        stateTransitions.setLifecycle(lifecycleBack);
        assertThat(stateTransitions.getLifecycle()).isEqualTo(lifecycleBack);

        stateTransitions.lifecycle(null);
        assertThat(stateTransitions.getLifecycle()).isNull();
    }
}
