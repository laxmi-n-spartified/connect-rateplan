package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.LifecycleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LifecycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lifecycle.class);
        Lifecycle lifecycle1 = getLifecycleSample1();
        Lifecycle lifecycle2 = new Lifecycle();
        assertThat(lifecycle1).isNotEqualTo(lifecycle2);

        lifecycle2.setId(lifecycle1.getId());
        assertThat(lifecycle1).isEqualTo(lifecycle2);

        lifecycle2 = getLifecycleSample2();
        assertThat(lifecycle1).isNotEqualTo(lifecycle2);
    }
}
