package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelPriorityTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZoneLabelPriorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZoneLabelPriority.class);
        ZoneLabelPriority zoneLabelPriority1 = getZoneLabelPrioritySample1();
        ZoneLabelPriority zoneLabelPriority2 = new ZoneLabelPriority();
        assertThat(zoneLabelPriority1).isNotEqualTo(zoneLabelPriority2);

        zoneLabelPriority2.setId(zoneLabelPriority1.getId());
        assertThat(zoneLabelPriority1).isEqualTo(zoneLabelPriority2);

        zoneLabelPriority2 = getZoneLabelPrioritySample2();
        assertThat(zoneLabelPriority1).isNotEqualTo(zoneLabelPriority2);
    }

    @Test
    void rateplanTest() {
        ZoneLabelPriority zoneLabelPriority = getZoneLabelPriorityRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        zoneLabelPriority.setRateplan(rateplanBack);
        assertThat(zoneLabelPriority.getRateplan()).isEqualTo(rateplanBack);

        zoneLabelPriority.rateplan(null);
        assertThat(zoneLabelPriority.getRateplan()).isNull();
    }

    @Test
    void zoneLabelTest() {
        ZoneLabelPriority zoneLabelPriority = getZoneLabelPriorityRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        zoneLabelPriority.setZoneLabel(zoneLabelBack);
        assertThat(zoneLabelPriority.getZoneLabel()).isEqualTo(zoneLabelBack);

        zoneLabelPriority.zoneLabel(null);
        assertThat(zoneLabelPriority.getZoneLabel()).isNull();
    }
}
