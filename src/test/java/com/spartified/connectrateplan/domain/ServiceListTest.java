package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.ServiceListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceList.class);
        ServiceList serviceList1 = getServiceListSample1();
        ServiceList serviceList2 = new ServiceList();
        assertThat(serviceList1).isNotEqualTo(serviceList2);

        serviceList2.setId(serviceList1.getId());
        assertThat(serviceList1).isEqualTo(serviceList2);

        serviceList2 = getServiceListSample2();
        assertThat(serviceList1).isNotEqualTo(serviceList2);
    }

    @Test
    void rateplanTest() {
        ServiceList serviceList = getServiceListRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        serviceList.setRateplan(rateplanBack);
        assertThat(serviceList.getRateplan()).isEqualTo(rateplanBack);

        serviceList.rateplan(null);
        assertThat(serviceList.getRateplan()).isNull();
    }
}
