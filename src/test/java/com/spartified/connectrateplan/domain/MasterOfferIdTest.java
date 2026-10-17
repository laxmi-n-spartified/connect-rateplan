package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.MasterOfferIdTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MasterOfferIdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterOfferId.class);
        MasterOfferId masterOfferId1 = getMasterOfferIdSample1();
        MasterOfferId masterOfferId2 = new MasterOfferId();
        assertThat(masterOfferId1).isNotEqualTo(masterOfferId2);

        masterOfferId2.setId(masterOfferId1.getId());
        assertThat(masterOfferId1).isEqualTo(masterOfferId2);

        masterOfferId2 = getMasterOfferIdSample2();
        assertThat(masterOfferId1).isNotEqualTo(masterOfferId2);
    }
}
