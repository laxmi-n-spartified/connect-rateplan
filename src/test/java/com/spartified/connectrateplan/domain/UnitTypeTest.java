package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.UnitTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitType.class);
        UnitType unitType1 = getUnitTypeSample1();
        UnitType unitType2 = new UnitType();
        assertThat(unitType1).isNotEqualTo(unitType2);

        unitType2.setId(unitType1.getId());
        assertThat(unitType1).isEqualTo(unitType2);

        unitType2 = getUnitTypeSample2();
        assertThat(unitType1).isNotEqualTo(unitType2);
    }
}
