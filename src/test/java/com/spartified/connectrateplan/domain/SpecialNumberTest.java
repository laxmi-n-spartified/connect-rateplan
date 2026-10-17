package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.SpecialNumberTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialNumberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialNumber.class);
        SpecialNumber specialNumber1 = getSpecialNumberSample1();
        SpecialNumber specialNumber2 = new SpecialNumber();
        assertThat(specialNumber1).isNotEqualTo(specialNumber2);

        specialNumber2.setId(specialNumber1.getId());
        assertThat(specialNumber1).isEqualTo(specialNumber2);

        specialNumber2 = getSpecialNumberSample2();
        assertThat(specialNumber1).isNotEqualTo(specialNumber2);
    }

    @Test
    void zoneLabelTest() {
        SpecialNumber specialNumber = getSpecialNumberRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        specialNumber.setZoneLabel(zoneLabelBack);
        assertThat(specialNumber.getZoneLabel()).isEqualTo(zoneLabelBack);

        specialNumber.zoneLabel(null);
        assertThat(specialNumber.getZoneLabel()).isNull();
    }
}
