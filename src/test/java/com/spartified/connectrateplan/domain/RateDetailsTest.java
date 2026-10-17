package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.RateDetailsTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.RatingGroupTestSamples.*;
import static com.spartified.connectrateplan.domain.ServiceListTestSamples.*;
import static com.spartified.connectrateplan.domain.TimeSlotTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RateDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RateDetails.class);
        RateDetails rateDetails1 = getRateDetailsSample1();
        RateDetails rateDetails2 = new RateDetails();
        assertThat(rateDetails1).isNotEqualTo(rateDetails2);

        rateDetails2.setId(rateDetails1.getId());
        assertThat(rateDetails1).isEqualTo(rateDetails2);

        rateDetails2 = getRateDetailsSample2();
        assertThat(rateDetails1).isNotEqualTo(rateDetails2);
    }

    @Test
    void origZoneLabelTest() {
        RateDetails rateDetails = getRateDetailsRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        rateDetails.setOrigZoneLabel(zoneLabelBack);
        assertThat(rateDetails.getOrigZoneLabel()).isEqualTo(zoneLabelBack);

        rateDetails.origZoneLabel(null);
        assertThat(rateDetails.getOrigZoneLabel()).isNull();
    }

    @Test
    void destZoneLabelTest() {
        RateDetails rateDetails = getRateDetailsRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        rateDetails.setDestZoneLabel(zoneLabelBack);
        assertThat(rateDetails.getDestZoneLabel()).isEqualTo(zoneLabelBack);

        rateDetails.destZoneLabel(null);
        assertThat(rateDetails.getDestZoneLabel()).isNull();
    }

    @Test
    void ratingGroupTest() {
        RateDetails rateDetails = getRateDetailsRandomSampleGenerator();
        RatingGroup ratingGroupBack = getRatingGroupRandomSampleGenerator();

        rateDetails.setRatingGroup(ratingGroupBack);
        assertThat(rateDetails.getRatingGroup()).isEqualTo(ratingGroupBack);

        rateDetails.ratingGroup(null);
        assertThat(rateDetails.getRatingGroup()).isNull();
    }

    @Test
    void timeSlotTest() {
        RateDetails rateDetails = getRateDetailsRandomSampleGenerator();
        TimeSlot timeSlotBack = getTimeSlotRandomSampleGenerator();

        rateDetails.setTimeSlot(timeSlotBack);
        assertThat(rateDetails.getTimeSlot()).isEqualTo(timeSlotBack);

        rateDetails.timeSlot(null);
        assertThat(rateDetails.getTimeSlot()).isNull();
    }

    @Test
    void rateplanTest() {
        RateDetails rateDetails = getRateDetailsRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        rateDetails.setRateplan(rateplanBack);
        assertThat(rateDetails.getRateplan()).isEqualTo(rateplanBack);

        rateDetails.rateplan(null);
        assertThat(rateDetails.getRateplan()).isNull();
    }

    @Test
    void serviceListTest() {
        RateDetails rateDetails = getRateDetailsRandomSampleGenerator();
        ServiceList serviceListBack = getServiceListRandomSampleGenerator();

        rateDetails.setServiceList(serviceListBack);
        assertThat(rateDetails.getServiceList()).isEqualTo(serviceListBack);

        rateDetails.serviceList(null);
        assertThat(rateDetails.getServiceList()).isNull();
    }
}
