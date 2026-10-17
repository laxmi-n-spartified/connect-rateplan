package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.NetworkIdentityTestSamples.*;
import static com.spartified.connectrateplan.domain.SgsnIpListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SgsnIpListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SgsnIpList.class);
        SgsnIpList sgsnIpList1 = getSgsnIpListSample1();
        SgsnIpList sgsnIpList2 = new SgsnIpList();
        assertThat(sgsnIpList1).isNotEqualTo(sgsnIpList2);

        sgsnIpList2.setId(sgsnIpList1.getId());
        assertThat(sgsnIpList1).isEqualTo(sgsnIpList2);

        sgsnIpList2 = getSgsnIpListSample2();
        assertThat(sgsnIpList1).isNotEqualTo(sgsnIpList2);
    }

    @Test
    void networkIdentityTest() {
        SgsnIpList sgsnIpList = getSgsnIpListRandomSampleGenerator();
        NetworkIdentity networkIdentityBack = getNetworkIdentityRandomSampleGenerator();

        sgsnIpList.setNetworkIdentity(networkIdentityBack);
        assertThat(sgsnIpList.getNetworkIdentity()).isEqualTo(networkIdentityBack);

        sgsnIpList.networkIdentity(null);
        assertThat(sgsnIpList.getNetworkIdentity()).isNull();
    }
}
