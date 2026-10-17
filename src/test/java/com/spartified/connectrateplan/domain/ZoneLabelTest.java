package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.NetworkIdentityTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ZoneLabelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZoneLabel.class);
        ZoneLabel zoneLabel1 = getZoneLabelSample1();
        ZoneLabel zoneLabel2 = new ZoneLabel();
        assertThat(zoneLabel1).isNotEqualTo(zoneLabel2);

        zoneLabel2.setId(zoneLabel1.getId());
        assertThat(zoneLabel1).isEqualTo(zoneLabel2);

        zoneLabel2 = getZoneLabelSample2();
        assertThat(zoneLabel1).isNotEqualTo(zoneLabel2);
    }

    @Test
    void networkIdentityTest() {
        ZoneLabel zoneLabel = getZoneLabelRandomSampleGenerator();
        NetworkIdentity networkIdentityBack = getNetworkIdentityRandomSampleGenerator();

        zoneLabel.addNetworkIdentity(networkIdentityBack);
        assertThat(zoneLabel.getNetworkIdentities()).containsOnly(networkIdentityBack);

        zoneLabel.removeNetworkIdentity(networkIdentityBack);
        assertThat(zoneLabel.getNetworkIdentities()).doesNotContain(networkIdentityBack);

        zoneLabel.networkIdentities(new HashSet<>(Set.of(networkIdentityBack)));
        assertThat(zoneLabel.getNetworkIdentities()).containsOnly(networkIdentityBack);

        zoneLabel.setNetworkIdentities(new HashSet<>());
        assertThat(zoneLabel.getNetworkIdentities()).doesNotContain(networkIdentityBack);
    }
}
