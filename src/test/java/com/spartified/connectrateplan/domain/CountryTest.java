package com.spartified.connectrateplan.domain;

import static com.spartified.connectrateplan.domain.CountryTestSamples.*;
import static com.spartified.connectrateplan.domain.CurrencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.spartified.connectrateplan.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void currencyTest() {
        Country country = getCountryRandomSampleGenerator();
        Currency currencyBack = getCurrencyRandomSampleGenerator();

        country.setCurrency(currencyBack);
        assertThat(country.getCurrency()).isEqualTo(currencyBack);

        country.currency(null);
        assertThat(country.getCurrency()).isNull();
    }
}
