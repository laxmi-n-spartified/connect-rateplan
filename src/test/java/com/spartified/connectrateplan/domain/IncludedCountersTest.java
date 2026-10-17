package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.IncludedCountersTestSamples.*;
import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.RatingGroupTestSamples.*;
import static com.spartified.connectrateplan.domain.ServiceListTestSamples.*;
import static com.spartified.connectrateplan.domain.TimeSlotTestSamples.*;
import static com.spartified.connectrateplan.domain.ZoneLabelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncludedCountersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncludedCounters.class);
        IncludedCounters includedCounters1 = getIncludedCountersSample1();
        IncludedCounters includedCounters2 = new IncludedCounters();
        assertThat(includedCounters1).isNotEqualTo(includedCounters2);

        includedCounters2.setId(includedCounters1.getId());
        assertThat(includedCounters1).isEqualTo(includedCounters2);

        includedCounters2 = getIncludedCountersSample2();
        assertThat(includedCounters1).isNotEqualTo(includedCounters2);
    }

    @Test
    void origZoneLabelTest() {
        IncludedCounters includedCounters = getIncludedCountersRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        includedCounters.setOrigZoneLabel(zoneLabelBack);
        assertThat(includedCounters.getOrigZoneLabel()).isEqualTo(zoneLabelBack);

        includedCounters.origZoneLabel(null);
        assertThat(includedCounters.getOrigZoneLabel()).isNull();
    }

    @Test
    void destZoneLabelTest() {
        IncludedCounters includedCounters = getIncludedCountersRandomSampleGenerator();
        ZoneLabel zoneLabelBack = getZoneLabelRandomSampleGenerator();

        includedCounters.setDestZoneLabel(zoneLabelBack);
        assertThat(includedCounters.getDestZoneLabel()).isEqualTo(zoneLabelBack);

        includedCounters.destZoneLabel(null);
        assertThat(includedCounters.getDestZoneLabel()).isNull();
    }

    @Test
    void ratingGroupTest() {
        IncludedCounters includedCounters = getIncludedCountersRandomSampleGenerator();
        RatingGroup ratingGroupBack = getRatingGroupRandomSampleGenerator();

        includedCounters.setRatingGroup(ratingGroupBack);
        assertThat(includedCounters.getRatingGroup()).isEqualTo(ratingGroupBack);

        includedCounters.ratingGroup(null);
        assertThat(includedCounters.getRatingGroup()).isNull();
    }

    @Test
    void rateplanTest() {
        IncludedCounters includedCounters = getIncludedCountersRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        includedCounters.setRateplan(rateplanBack);
        assertThat(includedCounters.getRateplan()).isEqualTo(rateplanBack);

        includedCounters.rateplan(null);
        assertThat(includedCounters.getRateplan()).isNull();
    }

    @Test
    void serviceListTest() {
        IncludedCounters includedCounters = getIncludedCountersRandomSampleGenerator();
        ServiceList serviceListBack = getServiceListRandomSampleGenerator();

        includedCounters.setServiceList(serviceListBack);
        assertThat(includedCounters.getServiceList()).isEqualTo(serviceListBack);

        includedCounters.serviceList(null);
        assertThat(includedCounters.getServiceList()).isNull();
    }

    @Test
    void timeSlotTest() {
        IncludedCounters includedCounters = getIncludedCountersRandomSampleGenerator();
        TimeSlot timeSlotBack = getTimeSlotRandomSampleGenerator();

        includedCounters.setTimeSlot(timeSlotBack);
        assertThat(includedCounters.getTimeSlot()).isEqualTo(timeSlotBack);

        includedCounters.timeSlot(null);
        assertThat(includedCounters.getTimeSlot()).isNull();
    }
}
