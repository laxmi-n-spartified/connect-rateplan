package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.BillingCycleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillingCycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillingCycle.class);
        BillingCycle billingCycle1 = getBillingCycleSample1();
        BillingCycle billingCycle2 = new BillingCycle();
        assertThat(billingCycle1).isNotEqualTo(billingCycle2);

        billingCycle2.setId(billingCycle1.getId());
        assertThat(billingCycle1).isEqualTo(billingCycle2);

        billingCycle2 = getBillingCycleSample2();
        assertThat(billingCycle1).isNotEqualTo(billingCycle2);
    }
}
