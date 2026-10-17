package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.LifecycleTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanLcStateModelTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.RatingGroupTestSamples.*;
import static com.spartified.connectrateplan.domain.TimeIntervalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RateplanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rateplan.class);
        Rateplan rateplan1 = getRateplanSample1();
        Rateplan rateplan2 = new Rateplan();
        assertThat(rateplan1).isNotEqualTo(rateplan2);

        rateplan2.setId(rateplan1.getId());
        assertThat(rateplan1).isEqualTo(rateplan2);

        rateplan2 = getRateplanSample2();
        assertThat(rateplan1).isNotEqualTo(rateplan2);
    }

    @Test
    void lifecycleTest() {
        Rateplan rateplan = getRateplanRandomSampleGenerator();
        Lifecycle lifecycleBack = getLifecycleRandomSampleGenerator();

        rateplan.setLifecycle(lifecycleBack);
        assertThat(rateplan.getLifecycle()).isEqualTo(lifecycleBack);

        rateplan.lifecycle(null);
        assertThat(rateplan.getLifecycle()).isNull();
    }

    @Test
    void timeIntervalTest() {
        Rateplan rateplan = getRateplanRandomSampleGenerator();
        TimeInterval timeIntervalBack = getTimeIntervalRandomSampleGenerator();

        rateplan.setTimeInterval(timeIntervalBack);
        assertThat(rateplan.getTimeInterval()).isEqualTo(timeIntervalBack);

        rateplan.timeInterval(null);
        assertThat(rateplan.getTimeInterval()).isNull();
    }

    @Test
    void ratingGroupTest() {
        Rateplan rateplan = getRateplanRandomSampleGenerator();
        RatingGroup ratingGroupBack = getRatingGroupRandomSampleGenerator();

        rateplan.addRatingGroup(ratingGroupBack);
        assertThat(rateplan.getRatingGroups()).containsOnly(ratingGroupBack);

        rateplan.removeRatingGroup(ratingGroupBack);
        assertThat(rateplan.getRatingGroups()).doesNotContain(ratingGroupBack);

        rateplan.ratingGroups(new HashSet<>(Set.of(ratingGroupBack)));
        assertThat(rateplan.getRatingGroups()).containsOnly(ratingGroupBack);

        rateplan.setRatingGroups(new HashSet<>());
        assertThat(rateplan.getRatingGroups()).doesNotContain(ratingGroupBack);
    }

    @Test
    void rateplanLcStateModelTest() {
        Rateplan rateplan = getRateplanRandomSampleGenerator();
        RateplanLcStateModel rateplanLcStateModelBack = getRateplanLcStateModelRandomSampleGenerator();

        rateplan.setRateplanLcStateModel(rateplanLcStateModelBack);
        assertThat(rateplan.getRateplanLcStateModel()).isEqualTo(rateplanLcStateModelBack);
        assertThat(rateplanLcStateModelBack.getRateplan()).isEqualTo(rateplan);

        rateplan.rateplanLcStateModel(null);
        assertThat(rateplan.getRateplanLcStateModel()).isNull();
        assertThat(rateplanLcStateModelBack.getRateplan()).isNull();
    }
}
