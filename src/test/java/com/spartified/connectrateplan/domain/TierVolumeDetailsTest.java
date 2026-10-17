package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.RateDetailsTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.RatingGroupTestSamples.*;
import static com.spartified.connectrateplan.domain.ServiceListTestSamples.*;
import static com.spartified.connectrateplan.domain.TierVolumeDetailsTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TierVolumeDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TierVolumeDetails.class);
        TierVolumeDetails tierVolumeDetails1 = getTierVolumeDetailsSample1();
        TierVolumeDetails tierVolumeDetails2 = new TierVolumeDetails();
        assertThat(tierVolumeDetails1).isNotEqualTo(tierVolumeDetails2);

        tierVolumeDetails2.setId(tierVolumeDetails1.getId());
        assertThat(tierVolumeDetails1).isEqualTo(tierVolumeDetails2);

        tierVolumeDetails2 = getTierVolumeDetailsSample2();
        assertThat(tierVolumeDetails1).isNotEqualTo(tierVolumeDetails2);
    }

    @Test
    void origZoneLabelTest() {
        TierVolumeDetails tierVolumeDetails = getTierVolumeDetailsRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        tierVolumeDetails.setOrigZoneLabel(zoneLabelBack);
        assertThat(tierVolumeDetails.getOrigZoneLabel()).isEqualTo(zoneLabelBack);

        tierVolumeDetails.origZoneLabel(null);
        assertThat(tierVolumeDetails.getOrigZoneLabel()).isNull();
    }

    @Test
    void destZoneLabelTest() {
        TierVolumeDetails tierVolumeDetails = getTierVolumeDetailsRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        tierVolumeDetails.setDestZoneLabel(zoneLabelBack);
        assertThat(tierVolumeDetails.getDestZoneLabel()).isEqualTo(zoneLabelBack);

        tierVolumeDetails.destZoneLabel(null);
        assertThat(tierVolumeDetails.getDestZoneLabel()).isNull();
    }

    @Test
    void rateDetailTest() {
        TierVolumeDetails tierVolumeDetails = getTierVolumeDetailsRandomSampleGenerator();
        RateDetails rateDetailsBack = getRateDetailsRandomSampleGenerator();

        tierVolumeDetails.setRateDetail(rateDetailsBack);
        assertThat(tierVolumeDetails.getRateDetail()).isEqualTo(rateDetailsBack);

        tierVolumeDetails.rateDetail(null);
        assertThat(tierVolumeDetails.getRateDetail()).isNull();
    }

    @Test
    void rateplanTest() {
        TierVolumeDetails tierVolumeDetails = getTierVolumeDetailsRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        tierVolumeDetails.setRateplan(rateplanBack);
        assertThat(tierVolumeDetails.getRateplan()).isEqualTo(rateplanBack);

        tierVolumeDetails.rateplan(null);
        assertThat(tierVolumeDetails.getRateplan()).isNull();
    }

    @Test
    void ratingGroupTest() {
        TierVolumeDetails tierVolumeDetails = getTierVolumeDetailsRandomSampleGenerator();
        RatingGroup ratingGroupBack = getRatingGroupRandomSampleGenerator();

        tierVolumeDetails.setRatingGroup(ratingGroupBack);
        assertThat(tierVolumeDetails.getRatingGroup()).isEqualTo(ratingGroupBack);

        tierVolumeDetails.ratingGroup(null);
        assertThat(tierVolumeDetails.getRatingGroup()).isNull();
    }

    @Test
    void serviceListTest() {
        TierVolumeDetails tierVolumeDetails = getTierVolumeDetailsRandomSampleGenerator();
        ServiceList serviceListBack = getServiceListRandomSampleGenerator();

        tierVolumeDetails.setServiceList(serviceListBack);
        assertThat(tierVolumeDetails.getServiceList()).isEqualTo(serviceListBack);

        tierVolumeDetails.serviceList(null);
        assertThat(tierVolumeDetails.getServiceList()).isNull();
    }
}
