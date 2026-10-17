package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.LifecycleTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanLcStateModelTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RateplanLcStateModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RateplanLcStateModel.class);
        RateplanLcStateModel rateplanLcStateModel1 = getRateplanLcStateModelSample1();
        RateplanLcStateModel rateplanLcStateModel2 = new RateplanLcStateModel();
        assertThat(rateplanLcStateModel1).isNotEqualTo(rateplanLcStateModel2);

        rateplanLcStateModel2.setId(rateplanLcStateModel1.getId());
        assertThat(rateplanLcStateModel1).isEqualTo(rateplanLcStateModel2);

        rateplanLcStateModel2 = getRateplanLcStateModelSample2();
        assertThat(rateplanLcStateModel1).isNotEqualTo(rateplanLcStateModel2);
    }

    @Test
    void rateplanTest() {
        RateplanLcStateModel rateplanLcStateModel = getRateplanLcStateModelRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        rateplanLcStateModel.setRateplan(rateplanBack);
        assertThat(rateplanLcStateModel.getRateplan()).isEqualTo(rateplanBack);

        rateplanLcStateModel.rateplan(null);
        assertThat(rateplanLcStateModel.getRateplan()).isNull();
    }

    @Test
    void lifecycleTest() {
        RateplanLcStateModel rateplanLcStateModel = getRateplanLcStateModelRandomSampleGenerator();
        Lifecycle lifecycleBack = getLifecycleRandomSampleGenerator();

        rateplanLcStateModel.setLifecycle(lifecycleBack);
        assertThat(rateplanLcStateModel.getLifecycle()).isEqualTo(lifecycleBack);

        rateplanLcStateModel.lifecycle(null);
        assertThat(rateplanLcStateModel.getLifecycle()).isNull();
    }
}
