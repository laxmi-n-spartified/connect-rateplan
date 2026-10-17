package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.TimeIntervalTestSamples.*;
import static com.spartified.connectrateplan.domain.TimeSlotTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSlotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSlot.class);
        TimeSlot timeSlot1 = getTimeSlotSample1();
        TimeSlot timeSlot2 = new TimeSlot();
        assertThat(timeSlot1).isNotEqualTo(timeSlot2);

        timeSlot2.setId(timeSlot1.getId());
        assertThat(timeSlot1).isEqualTo(timeSlot2);

        timeSlot2 = getTimeSlotSample2();
        assertThat(timeSlot1).isNotEqualTo(timeSlot2);
    }

    @Test
    void timeIntervalTest() {
        TimeSlot timeSlot = getTimeSlotRandomSampleGenerator();
        TimeInterval timeIntervalBack = getTimeIntervalRandomSampleGenerator();

        timeSlot.setTimeInterval(timeIntervalBack);
        assertThat(timeSlot.getTimeInterval()).isEqualTo(timeIntervalBack);

        timeSlot.timeInterval(null);
        assertThat(timeSlot.getTimeInterval()).isNull();
    }
}
