package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.StatesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(States.class);
        States states1 = getStatesSample1();
        States states2 = new States();
        assertThat(states1).isNotEqualTo(states2);

        states2.setId(states1.getId());
        assertThat(states1).isEqualTo(states2);

        states2 = getStatesSample2();
        assertThat(states1).isNotEqualTo(states2);
    }
}
