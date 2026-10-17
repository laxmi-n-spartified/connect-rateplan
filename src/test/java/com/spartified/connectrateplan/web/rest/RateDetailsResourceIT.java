package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.RateDetailsAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static com.spartified.connectrateplan.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.RateDetails;
import com.spartified.connectrateplan.repository.RateDetailsRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link RateDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RateDetailsResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DATA_OVERAGE_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DATA_OVERAGE_RATE = new BigDecimal(2);

    private static final String DEFAULT_DATA_OVERAGE_RATE_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_DATA_OVERAGE_RATE_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SMS_MO_OVERAGE_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SMS_MO_OVERAGE_RATE = new BigDecimal(2);

    private static final String DEFAULT_SMS_MO_OVERAGE_RATE_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_SMS_MO_OVERAGE_RATE_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SMS_MT_OVERAGE_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SMS_MT_OVERAGE_RATE = new BigDecimal(2);

    private static final String DEFAULT_SMS_MT_OVERAGE_RATE_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_SMS_MT_OVERAGE_RATE_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VOICE_MO_OVERAGE_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOICE_MO_OVERAGE_RATE = new BigDecimal(2);

    private static final String DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_VOICE_MO_OVERAGE_RATE_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VOICE_MT_OVERAGE_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOICE_MT_OVERAGE_RATE = new BigDecimal(2);

    private static final String DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_VOICE_MT_OVERAGE_RATE_UNIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final BigDecimal DEFAULT_DATA_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_DATA_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SMS_MO_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_SMS_MO_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SMS_MT_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_SMS_MT_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/rate-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RateDetailsRepository rateDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRateDetailsMockMvc;

    private RateDetails rateDetails;

    private RateDetails insertedRateDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RateDetails createEntity() {
        return new RateDetails()
            .tenantId(DEFAULT_TENANT_ID)
            .dataOverageRate(DEFAULT_DATA_OVERAGE_RATE)
            .dataOverageRateUnit(DEFAULT_DATA_OVERAGE_RATE_UNIT)
            .smsMoOverageRate(DEFAULT_SMS_MO_OVERAGE_RATE)
            .smsMoOverageRateUnit(DEFAULT_SMS_MO_OVERAGE_RATE_UNIT)
            .smsMtOverageRate(DEFAULT_SMS_MT_OVERAGE_RATE)
            .smsMtOverageRateUnit(DEFAULT_SMS_MT_OVERAGE_RATE_UNIT)
            .voiceMoOverageRate(DEFAULT_VOICE_MO_OVERAGE_RATE)
            .voiceMoOverageRateUnit(DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT)
            .voiceMtOverageRate(DEFAULT_VOICE_MT_OVERAGE_RATE)
            .voiceMtOverageRateUnit(DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT)
            .isDefault(DEFAULT_IS_DEFAULT)
            .dataOverageRateUnitFactor(DEFAULT_DATA_OVERAGE_RATE_UNIT_FACTOR)
            .smsMoOverageRateUnitFactor(DEFAULT_SMS_MO_OVERAGE_RATE_UNIT_FACTOR)
            .smsMtOverageRateUnitFactor(DEFAULT_SMS_MT_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMoOverageRateUnitFactor(DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMtOverageRateUnitFactor(DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RateDetails createUpdatedEntity() {
        return new RateDetails()
            .tenantId(UPDATED_TENANT_ID)
            .dataOverageRate(UPDATED_DATA_OVERAGE_RATE)
            .dataOverageRateUnit(UPDATED_DATA_OVERAGE_RATE_UNIT)
            .smsMoOverageRate(UPDATED_SMS_MO_OVERAGE_RATE)
            .smsMoOverageRateUnit(UPDATED_SMS_MO_OVERAGE_RATE_UNIT)
            .smsMtOverageRate(UPDATED_SMS_MT_OVERAGE_RATE)
            .smsMtOverageRateUnit(UPDATED_SMS_MT_OVERAGE_RATE_UNIT)
            .voiceMoOverageRate(UPDATED_VOICE_MO_OVERAGE_RATE)
            .voiceMoOverageRateUnit(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT)
            .voiceMtOverageRate(UPDATED_VOICE_MT_OVERAGE_RATE)
            .voiceMtOverageRateUnit(UPDATED_VOICE_MT_OVERAGE_RATE_UNIT)
            .isDefault(UPDATED_IS_DEFAULT)
            .dataOverageRateUnitFactor(UPDATED_DATA_OVERAGE_RATE_UNIT_FACTOR)
            .smsMoOverageRateUnitFactor(UPDATED_SMS_MO_OVERAGE_RATE_UNIT_FACTOR)
            .smsMtOverageRateUnitFactor(UPDATED_SMS_MT_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMoOverageRateUnitFactor(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMtOverageRateUnitFactor(UPDATED_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        rateDetails = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRateDetails != null) {
            rateDetailsRepository.delete(insertedRateDetails);
            insertedRateDetails = null;
        }
    }

    @Test
    @Transactional
    void createRateDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RateDetails
        var returnedRateDetails = om.readValue(
            restRateDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateDetails)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RateDetails.class
        );

        // Validate the RateDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRateDetailsUpdatableFieldsEquals(returnedRateDetails, getPersistedRateDetails(returnedRateDetails));

        insertedRateDetails = returnedRateDetails;
    }

    @Test
    @Transactional
    void createRateDetailsWithExistingId() throws Exception {
        // Create the RateDetails with an existing ID
        rateDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRateDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateDetails)))
            .andExpect(status().isBadRequest());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRateDetailses() throws Exception {
        // Initialize the database
        insertedRateDetails = rateDetailsRepository.saveAndFlush(rateDetails);

        // Get all the rateDetailsList
        restRateDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rateDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].dataOverageRate").value(hasItem(sameNumber(DEFAULT_DATA_OVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].dataOverageRateUnit").value(hasItem(DEFAULT_DATA_OVERAGE_RATE_UNIT)))
            .andExpect(jsonPath("$.[*].smsMoOverageRate").value(hasItem(sameNumber(DEFAULT_SMS_MO_OVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].smsMoOverageRateUnit").value(hasItem(DEFAULT_SMS_MO_OVERAGE_RATE_UNIT)))
            .andExpect(jsonPath("$.[*].smsMtOverageRate").value(hasItem(sameNumber(DEFAULT_SMS_MT_OVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].smsMtOverageRateUnit").value(hasItem(DEFAULT_SMS_MT_OVERAGE_RATE_UNIT)))
            .andExpect(jsonPath("$.[*].voiceMoOverageRate").value(hasItem(sameNumber(DEFAULT_VOICE_MO_OVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].voiceMoOverageRateUnit").value(hasItem(DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT)))
            .andExpect(jsonPath("$.[*].voiceMtOverageRate").value(hasItem(sameNumber(DEFAULT_VOICE_MT_OVERAGE_RATE))))
            .andExpect(jsonPath("$.[*].voiceMtOverageRateUnit").value(hasItem(DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].dataOverageRateUnitFactor").value(hasItem(sameNumber(DEFAULT_DATA_OVERAGE_RATE_UNIT_FACTOR))))
            .andExpect(jsonPath("$.[*].smsMoOverageRateUnitFactor").value(hasItem(sameNumber(DEFAULT_SMS_MO_OVERAGE_RATE_UNIT_FACTOR))))
            .andExpect(jsonPath("$.[*].smsMtOverageRateUnitFactor").value(hasItem(sameNumber(DEFAULT_SMS_MT_OVERAGE_RATE_UNIT_FACTOR))))
            .andExpect(jsonPath("$.[*].voiceMoOverageRateUnitFactor").value(hasItem(sameNumber(DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR))))
            .andExpect(jsonPath("$.[*].voiceMtOverageRateUnitFactor").value(hasItem(sameNumber(DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRateDetails() throws Exception {
        // Initialize the database
        insertedRateDetails = rateDetailsRepository.saveAndFlush(rateDetails);

        // Get the rateDetails
        restRateDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, rateDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rateDetails.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.dataOverageRate").value(sameNumber(DEFAULT_DATA_OVERAGE_RATE)))
            .andExpect(jsonPath("$.dataOverageRateUnit").value(DEFAULT_DATA_OVERAGE_RATE_UNIT))
            .andExpect(jsonPath("$.smsMoOverageRate").value(sameNumber(DEFAULT_SMS_MO_OVERAGE_RATE)))
            .andExpect(jsonPath("$.smsMoOverageRateUnit").value(DEFAULT_SMS_MO_OVERAGE_RATE_UNIT))
            .andExpect(jsonPath("$.smsMtOverageRate").value(sameNumber(DEFAULT_SMS_MT_OVERAGE_RATE)))
            .andExpect(jsonPath("$.smsMtOverageRateUnit").value(DEFAULT_SMS_MT_OVERAGE_RATE_UNIT))
            .andExpect(jsonPath("$.voiceMoOverageRate").value(sameNumber(DEFAULT_VOICE_MO_OVERAGE_RATE)))
            .andExpect(jsonPath("$.voiceMoOverageRateUnit").value(DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT))
            .andExpect(jsonPath("$.voiceMtOverageRate").value(sameNumber(DEFAULT_VOICE_MT_OVERAGE_RATE)))
            .andExpect(jsonPath("$.voiceMtOverageRateUnit").value(DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.dataOverageRateUnitFactor").value(sameNumber(DEFAULT_DATA_OVERAGE_RATE_UNIT_FACTOR)))
            .andExpect(jsonPath("$.smsMoOverageRateUnitFactor").value(sameNumber(DEFAULT_SMS_MO_OVERAGE_RATE_UNIT_FACTOR)))
            .andExpect(jsonPath("$.smsMtOverageRateUnitFactor").value(sameNumber(DEFAULT_SMS_MT_OVERAGE_RATE_UNIT_FACTOR)))
            .andExpect(jsonPath("$.voiceMoOverageRateUnitFactor").value(sameNumber(DEFAULT_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR)))
            .andExpect(jsonPath("$.voiceMtOverageRateUnitFactor").value(sameNumber(DEFAULT_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRateDetails() throws Exception {
        // Get the rateDetails
        restRateDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRateDetails() throws Exception {
        // Initialize the database
        insertedRateDetails = rateDetailsRepository.saveAndFlush(rateDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateDetails
        RateDetails updatedRateDetails = rateDetailsRepository.findById(rateDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRateDetails are not directly saved in db
        em.detach(updatedRateDetails);
        updatedRateDetails
            .tenantId(UPDATED_TENANT_ID)
            .dataOverageRate(UPDATED_DATA_OVERAGE_RATE)
            .dataOverageRateUnit(UPDATED_DATA_OVERAGE_RATE_UNIT)
            .smsMoOverageRate(UPDATED_SMS_MO_OVERAGE_RATE)
            .smsMoOverageRateUnit(UPDATED_SMS_MO_OVERAGE_RATE_UNIT)
            .smsMtOverageRate(UPDATED_SMS_MT_OVERAGE_RATE)
            .smsMtOverageRateUnit(UPDATED_SMS_MT_OVERAGE_RATE_UNIT)
            .voiceMoOverageRate(UPDATED_VOICE_MO_OVERAGE_RATE)
            .voiceMoOverageRateUnit(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT)
            .voiceMtOverageRate(UPDATED_VOICE_MT_OVERAGE_RATE)
            .voiceMtOverageRateUnit(UPDATED_VOICE_MT_OVERAGE_RATE_UNIT)
            .isDefault(UPDATED_IS_DEFAULT)
            .dataOverageRateUnitFactor(UPDATED_DATA_OVERAGE_RATE_UNIT_FACTOR)
            .smsMoOverageRateUnitFactor(UPDATED_SMS_MO_OVERAGE_RATE_UNIT_FACTOR)
            .smsMtOverageRateUnitFactor(UPDATED_SMS_MT_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMoOverageRateUnitFactor(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMtOverageRateUnitFactor(UPDATED_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRateDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRateDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRateDetails))
            )
            .andExpect(status().isOk());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRateDetailsToMatchAllProperties(updatedRateDetails);
    }

    @Test
    @Transactional
    void putNonExistingRateDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rateDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rateDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRateDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rateDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRateDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRateDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedRateDetails = rateDetailsRepository.saveAndFlush(rateDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateDetails using partial update
        RateDetails partialUpdatedRateDetails = new RateDetails();
        partialUpdatedRateDetails.setId(rateDetails.getId());

        partialUpdatedRateDetails
            .dataOverageRateUnit(UPDATED_DATA_OVERAGE_RATE_UNIT)
            .smsMoOverageRateUnit(UPDATED_SMS_MO_OVERAGE_RATE_UNIT)
            .smsMtOverageRateUnit(UPDATED_SMS_MT_OVERAGE_RATE_UNIT)
            .voiceMoOverageRateUnit(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT)
            .voiceMtOverageRate(UPDATED_VOICE_MT_OVERAGE_RATE)
            .smsMtOverageRateUnitFactor(UPDATED_SMS_MT_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMoOverageRateUnitFactor(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR)
            .createdDate(UPDATED_CREATED_DATE);

        restRateDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRateDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRateDetails))
            )
            .andExpect(status().isOk());

        // Validate the RateDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRateDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRateDetails, rateDetails),
            getPersistedRateDetails(rateDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateRateDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedRateDetails = rateDetailsRepository.saveAndFlush(rateDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateDetails using partial update
        RateDetails partialUpdatedRateDetails = new RateDetails();
        partialUpdatedRateDetails.setId(rateDetails.getId());

        partialUpdatedRateDetails
            .tenantId(UPDATED_TENANT_ID)
            .dataOverageRate(UPDATED_DATA_OVERAGE_RATE)
            .dataOverageRateUnit(UPDATED_DATA_OVERAGE_RATE_UNIT)
            .smsMoOverageRate(UPDATED_SMS_MO_OVERAGE_RATE)
            .smsMoOverageRateUnit(UPDATED_SMS_MO_OVERAGE_RATE_UNIT)
            .smsMtOverageRate(UPDATED_SMS_MT_OVERAGE_RATE)
            .smsMtOverageRateUnit(UPDATED_SMS_MT_OVERAGE_RATE_UNIT)
            .voiceMoOverageRate(UPDATED_VOICE_MO_OVERAGE_RATE)
            .voiceMoOverageRateUnit(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT)
            .voiceMtOverageRate(UPDATED_VOICE_MT_OVERAGE_RATE)
            .voiceMtOverageRateUnit(UPDATED_VOICE_MT_OVERAGE_RATE_UNIT)
            .isDefault(UPDATED_IS_DEFAULT)
            .dataOverageRateUnitFactor(UPDATED_DATA_OVERAGE_RATE_UNIT_FACTOR)
            .smsMoOverageRateUnitFactor(UPDATED_SMS_MO_OVERAGE_RATE_UNIT_FACTOR)
            .smsMtOverageRateUnitFactor(UPDATED_SMS_MT_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMoOverageRateUnitFactor(UPDATED_VOICE_MO_OVERAGE_RATE_UNIT_FACTOR)
            .voiceMtOverageRateUnitFactor(UPDATED_VOICE_MT_OVERAGE_RATE_UNIT_FACTOR)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRateDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRateDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRateDetails))
            )
            .andExpect(status().isOk());

        // Validate the RateDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRateDetailsUpdatableFieldsEquals(partialUpdatedRateDetails, getPersistedRateDetails(partialUpdatedRateDetails));
    }

    @Test
    @Transactional
    void patchNonExistingRateDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rateDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rateDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRateDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rateDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRateDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rateDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RateDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRateDetails() throws Exception {
        // Initialize the database
        insertedRateDetails = rateDetailsRepository.saveAndFlush(rateDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rateDetails
        restRateDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, rateDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rateDetailsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected RateDetails getPersistedRateDetails(RateDetails rateDetails) {
        return rateDetailsRepository.findById(rateDetails.getId()).orElseThrow();
    }

    protected void assertPersistedRateDetailsToMatchAllProperties(RateDetails expectedRateDetails) {
        assertRateDetailsAllPropertiesEquals(expectedRateDetails, getPersistedRateDetails(expectedRateDetails));
    }

    protected void assertPersistedRateDetailsToMatchUpdatableProperties(RateDetails expectedRateDetails) {
        assertRateDetailsAllUpdatablePropertiesEquals(expectedRateDetails, getPersistedRateDetails(expectedRateDetails));
    }
}
