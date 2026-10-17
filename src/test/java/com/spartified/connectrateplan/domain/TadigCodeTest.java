package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.TadigCodeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TadigCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TadigCode.class);
        TadigCode tadigCode1 = getTadigCodeSample1();
        TadigCode tadigCode2 = new TadigCode();
        assertThat(tadigCode1).isNotEqualTo(tadigCode2);

        tadigCode2.setId(tadigCode1.getId());
        assertThat(tadigCode1).isEqualTo(tadigCode2);

        tadigCode2 = getTadigCodeSample2();
        assertThat(tadigCode1).isNotEqualTo(tadigCode2);
    }
}
