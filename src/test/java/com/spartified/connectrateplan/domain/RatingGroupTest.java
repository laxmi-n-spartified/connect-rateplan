package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.RateplanTestSamples.*;
import static com.spartified.connectrateplan.domain.RatingGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RatingGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingGroup.class);
        RatingGroup ratingGroup1 = getRatingGroupSample1();
        RatingGroup ratingGroup2 = new RatingGroup();
        assertThat(ratingGroup1).isNotEqualTo(ratingGroup2);

        ratingGroup2.setId(ratingGroup1.getId());
        assertThat(ratingGroup1).isEqualTo(ratingGroup2);

        ratingGroup2 = getRatingGroupSample2();
        assertThat(ratingGroup1).isNotEqualTo(ratingGroup2);
    }

    @Test
    void rateplanTest() {
        RatingGroup ratingGroup = getRatingGroupRandomSampleGenerator();
        Rateplan rateplanBack = getRateplanRandomSampleGenerator();

        ratingGroup.addRateplan(rateplanBack);
        assertThat(ratingGroup.getRateplans()).containsOnly(rateplanBack);
        assertThat(rateplanBack.getRatingGroups()).containsOnly(ratingGroup);

        ratingGroup.removeRateplan(rateplanBack);
        assertThat(ratingGroup.getRateplans()).doesNotContain(rateplanBack);
        assertThat(rateplanBack.getRatingGroups()).doesNotContain(ratingGroup);

        ratingGroup.rateplans(new HashSet<>(Set.of(rateplanBack)));
        assertThat(ratingGroup.getRateplans()).containsOnly(rateplanBack);
        assertThat(rateplanBack.getRatingGroups()).containsOnly(ratingGroup);

        ratingGroup.setRateplans(new HashSet<>());
        assertThat(ratingGroup.getRateplans()).doesNotContain(rateplanBack);
        assertThat(rateplanBack.getRatingGroups()).doesNotContain(ratingGroup);
    }
}
