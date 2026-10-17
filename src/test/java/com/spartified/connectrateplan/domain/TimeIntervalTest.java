package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.TimeIntervalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeIntervalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeInterval.class);
        TimeInterval timeInterval1 = getTimeIntervalSample1();
        TimeInterval timeInterval2 = new TimeInterval();
        assertThat(timeInterval1).isNotEqualTo(timeInterval2);

        timeInterval2.setId(timeInterval1.getId());
        assertThat(timeInterval1).isEqualTo(timeInterval2);

        timeInterval2 = getTimeIntervalSample2();
        assertThat(timeInterval1).isNotEqualTo(timeInterval2);
    }
}
