package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.CountryTestSamples.*;
import static com.spartified.connectrateplan.domain.NetworkIdentityTestSamples.*;
import static com.spartified.connectrateplan.domain.TadigCodeTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NetworkIdentityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NetworkIdentity.class);
        NetworkIdentity networkIdentity1 = getNetworkIdentitySample1();
        NetworkIdentity networkIdentity2 = new NetworkIdentity();
        assertThat(networkIdentity1).isNotEqualTo(networkIdentity2);

        networkIdentity2.setId(networkIdentity1.getId());
        assertThat(networkIdentity1).isEqualTo(networkIdentity2);

        networkIdentity2 = getNetworkIdentitySample2();
        assertThat(networkIdentity1).isNotEqualTo(networkIdentity2);
    }

    @Test
    void countryTest() {
        NetworkIdentity networkIdentity = getNetworkIdentityRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        networkIdentity.setCountry(countryBack);
        assertThat(networkIdentity.getCountry()).isEqualTo(countryBack);

        networkIdentity.country(null);
        assertThat(networkIdentity.getCountry()).isNull();
    }

    @Test
    void tadigCodeTest() {
        NetworkIdentity networkIdentity = getNetworkIdentityRandomSampleGenerator();
        TadigCode tadigCodeBack = getTadigCodeRandomSampleGenerator();

        networkIdentity.setTadigCode(tadigCodeBack);
        assertThat(networkIdentity.getTadigCode()).isEqualTo(tadigCodeBack);

        networkIdentity.tadigCode(null);
        assertThat(networkIdentity.getTadigCode()).isNull();
    }

    @Test
    void zoneLabelTest() {
        NetworkIdentity networkIdentity = getNetworkIdentityRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        networkIdentity.addZoneLabel(zoneLabelBack);
        assertThat(networkIdentity.getZoneLabels()).containsOnly(zoneLabelBack);
        assertThat(zoneLabelBack.getNetworkIdentities()).containsOnly(networkIdentity);

        networkIdentity.removeZoneLabel(zoneLabelBack);
        assertThat(networkIdentity.getZoneLabels()).doesNotContain(zoneLabelBack);
        assertThat(zoneLabelBack.getNetworkIdentities()).doesNotContain(networkIdentity);

        networkIdentity.zoneLabels(new HashSet<>(Set.of(zoneLabelBack)));
        assertThat(networkIdentity.getZoneLabels()).containsOnly(zoneLabelBack);
        assertThat(zoneLabelBack.getNetworkIdentities()).containsOnly(networkIdentity);

        networkIdentity.setZoneLabels(new HashSet<>());
        assertThat(networkIdentity.getZoneLabels()).doesNotContain(zoneLabelBack);
        assertThat(zoneLabelBack.getNetworkIdentities()).doesNotContain(networkIdentity);
    }
}
