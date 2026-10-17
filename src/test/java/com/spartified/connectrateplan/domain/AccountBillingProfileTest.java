package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.AccountBillingProfileTestSamples.*;
import static com.spartified.connectrateplan.domain.BillingCycleTestSamples.*;
import static com.spartified.connectrateplan.domain.CurrencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountBillingProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBillingProfile.class);
        AccountBillingProfile accountBillingProfile1 = getAccountBillingProfileSample1();
        AccountBillingProfile accountBillingProfile2 = new AccountBillingProfile();
        assertThat(accountBillingProfile1).isNotEqualTo(accountBillingProfile2);

        accountBillingProfile2.setId(accountBillingProfile1.getId());
        assertThat(accountBillingProfile1).isEqualTo(accountBillingProfile2);

        accountBillingProfile2 = getAccountBillingProfileSample2();
        assertThat(accountBillingProfile1).isNotEqualTo(accountBillingProfile2);
    }

    @Test
    void billCycleTest() {
        AccountBillingProfile accountBillingProfile = getAccountBillingProfileRandomSampleGenerator();
        BillingCycle billingCycleBack = getBillingCycleRandomSampleGenerator();

        accountBillingProfile.setBillCycle(billingCycleBack);
        assertThat(accountBillingProfile.getBillCycle()).isEqualTo(billingCycleBack);

        accountBillingProfile.billCycle(null);
        assertThat(accountBillingProfile.getBillCycle()).isNull();
    }

    @Test
    void currencyTest() {
        AccountBillingProfile accountBillingProfile = getAccountBillingProfileRandomSampleGenerator();
        Currency currencyBack = getCurrencyRandomSampleGenerator();

        accountBillingProfile.setCurrency(currencyBack);
        assertThat(accountBillingProfile.getCurrency()).isEqualTo(currencyBack);

        accountBillingProfile.currency(null);
        assertThat(accountBillingProfile.getCurrency()).isNull();
    }
}
