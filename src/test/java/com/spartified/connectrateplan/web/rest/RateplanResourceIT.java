package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.RateplanAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static com.spartified.connectrateplan.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.Rateplan;
import com.spartified.connectrateplan.repository.RateplanRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link RateplanResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RateplanResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ACTIVATION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACTIVATION_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NETWORK_ACCESS_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NETWORK_ACCESS_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUBSCRIPTION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBSCRIPTION_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CANCELLATION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CANCELLATION_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ADDITIONAL_FEES = new BigDecimal(1);
    private static final BigDecimal UPDATED_ADDITIONAL_FEES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RE_ACTIVATION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RE_ACTIVATION_FEE = new BigDecimal(2);

    private static final Integer DEFAULT_CONTRACT_PERIOD = 1;
    private static final Integer UPDATED_CONTRACT_PERIOD = 2;

    private static final Integer DEFAULT_NOTICE_PERIOD = 1;
    private static final Integer UPDATED_NOTICE_PERIOD = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-10-17");

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.parse("2026-10-17");

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PLAN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String DEFAULT_SUBSCRIPTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_INCLUDED_TEST_DATA = 1L;
    private static final Long UPDATED_INCLUDED_TEST_DATA = 2L;

    private static final Long DEFAULT_INCLUDED_TEST_SMS = 1L;
    private static final Long UPDATED_INCLUDED_TEST_SMS = 2L;

    private static final Long DEFAULT_INCLUDED_TEST_VOICE = 1L;
    private static final Long UPDATED_INCLUDED_TEST_VOICE = 2L;

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.parse("2026-10-17");

    private static final Integer DEFAULT_TRIAL_DURATION = 1;
    private static final Integer UPDATED_TRIAL_DURATION = 2;

    private static final String DEFAULT_POOL_TARIFF = "AAAAAAAAAA";
    private static final String UPDATED_POOL_TARIFF = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_POOL_ACTIVATION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_POOL_ACTIVATION_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_POOL_SUBSCRIPTION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_POOL_SUBSCRIPTION_FEE = new BigDecimal(2);

    private static final String DEFAULT_ROUNDING_FOR_DATA = "AAAAAAAAAA";
    private static final String UPDATED_ROUNDING_FOR_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_ROUNDING_FOR_VOICE = "AAAAAAAAAA";
    private static final String UPDATED_ROUNDING_FOR_VOICE = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUDED_TEST_DATA_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_INCLUDED_TEST_DATA_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUDED_TEST_SMS_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_INCLUDED_TEST_SMS_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUDED_TEST_VOICE_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_INCLUDED_TEST_VOICE_UNIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RENEW_ON_EXPIRY = false;
    private static final Boolean UPDATED_RENEW_ON_EXPIRY = true;

    private static final String DEFAULT_TIERED_VOLUME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TIERED_VOLUME_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALIDITY_PERIOD = 1;
    private static final Integer UPDATED_VALIDITY_PERIOD = 2;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Long DEFAULT_QUOTA_PROFILE_ID = 1L;
    private static final Long UPDATED_QUOTA_PROFILE_ID = 2L;

    private static final String DEFAULT_QUOTA_PROFILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_QUOTA_PROFILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXT_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FIXED_ACTIVE_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_FIXED_ACTIVE_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUSPEND_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUSPEND_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUSPENDED_SIM_MRC = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUSPENDED_SIM_MRC = new BigDecimal(2);

    private static final BigDecimal DEFAULT_UNSUSPEND_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNSUSPEND_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DEACTIVATION_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEACTIVATION_FEE = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/rateplans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RateplanRepository rateplanRepository;

    @Mock
    private RateplanRepository rateplanRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRateplanMockMvc;

    private Rateplan rateplan;

    private Rateplan insertedRateplan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rateplan createEntity() {
        return new Rateplan()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .activationFee(DEFAULT_ACTIVATION_FEE)
            .networkAccessFee(DEFAULT_NETWORK_ACCESS_FEE)
            .subscriptionFee(DEFAULT_SUBSCRIPTION_FEE)
            .cancellationFee(DEFAULT_CANCELLATION_FEE)
            .additionalFees(DEFAULT_ADDITIONAL_FEES)
            .reActivationFee(DEFAULT_RE_ACTIVATION_FEE)
            .contractPeriod(DEFAULT_CONTRACT_PERIOD)
            .noticePeriod(DEFAULT_NOTICE_PERIOD)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .planType(DEFAULT_PLAN_TYPE)
            .version(DEFAULT_VERSION)
            .subscriptionType(DEFAULT_SUBSCRIPTION_TYPE)
            .includedTestData(DEFAULT_INCLUDED_TEST_DATA)
            .includedTestSms(DEFAULT_INCLUDED_TEST_SMS)
            .includedTestVoice(DEFAULT_INCLUDED_TEST_VOICE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .trialDuration(DEFAULT_TRIAL_DURATION)
            .poolTariff(DEFAULT_POOL_TARIFF)
            .poolActivationFee(DEFAULT_POOL_ACTIVATION_FEE)
            .poolSubscriptionFee(DEFAULT_POOL_SUBSCRIPTION_FEE)
            .roundingForData(DEFAULT_ROUNDING_FOR_DATA)
            .roundingForVoice(DEFAULT_ROUNDING_FOR_VOICE)
            .includedTestDataUnit(DEFAULT_INCLUDED_TEST_DATA_UNIT)
            .includedTestSmsUnit(DEFAULT_INCLUDED_TEST_SMS_UNIT)
            .includedTestVoiceUnit(DEFAULT_INCLUDED_TEST_VOICE_UNIT)
            .renewOnExpiry(DEFAULT_RENEW_ON_EXPIRY)
            .tieredVolumeType(DEFAULT_TIERED_VOLUME_TYPE)
            .validityPeriod(DEFAULT_VALIDITY_PERIOD)
            .category(DEFAULT_CATEGORY)
            .quotaProfileId(DEFAULT_QUOTA_PROFILE_ID)
            .quotaProfileName(DEFAULT_QUOTA_PROFILE_NAME)
            .extId(DEFAULT_EXT_ID)
            .fixedActiveFee(DEFAULT_FIXED_ACTIVE_FEE)
            .suspendFee(DEFAULT_SUSPEND_FEE)
            .suspendedSimMrc(DEFAULT_SUSPENDED_SIM_MRC)
            .unsuspendFee(DEFAULT_UNSUSPEND_FEE)
            .deactivationFee(DEFAULT_DEACTIVATION_FEE)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rateplan createUpdatedEntity() {
        return new Rateplan()
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .activationFee(UPDATED_ACTIVATION_FEE)
            .networkAccessFee(UPDATED_NETWORK_ACCESS_FEE)
            .subscriptionFee(UPDATED_SUBSCRIPTION_FEE)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .additionalFees(UPDATED_ADDITIONAL_FEES)
            .reActivationFee(UPDATED_RE_ACTIVATION_FEE)
            .contractPeriod(UPDATED_CONTRACT_PERIOD)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .planType(UPDATED_PLAN_TYPE)
            .version(UPDATED_VERSION)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .includedTestData(UPDATED_INCLUDED_TEST_DATA)
            .includedTestSms(UPDATED_INCLUDED_TEST_SMS)
            .includedTestVoice(UPDATED_INCLUDED_TEST_VOICE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .trialDuration(UPDATED_TRIAL_DURATION)
            .poolTariff(UPDATED_POOL_TARIFF)
            .poolActivationFee(UPDATED_POOL_ACTIVATION_FEE)
            .poolSubscriptionFee(UPDATED_POOL_SUBSCRIPTION_FEE)
            .roundingForData(UPDATED_ROUNDING_FOR_DATA)
            .roundingForVoice(UPDATED_ROUNDING_FOR_VOICE)
            .includedTestDataUnit(UPDATED_INCLUDED_TEST_DATA_UNIT)
            .includedTestSmsUnit(UPDATED_INCLUDED_TEST_SMS_UNIT)
            .includedTestVoiceUnit(UPDATED_INCLUDED_TEST_VOICE_UNIT)
            .renewOnExpiry(UPDATED_RENEW_ON_EXPIRY)
            .tieredVolumeType(UPDATED_TIERED_VOLUME_TYPE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .category(UPDATED_CATEGORY)
            .quotaProfileId(UPDATED_QUOTA_PROFILE_ID)
            .quotaProfileName(UPDATED_QUOTA_PROFILE_NAME)
            .extId(UPDATED_EXT_ID)
            .fixedActiveFee(UPDATED_FIXED_ACTIVE_FEE)
            .suspendFee(UPDATED_SUSPEND_FEE)
            .suspendedSimMrc(UPDATED_SUSPENDED_SIM_MRC)
            .unsuspendFee(UPDATED_UNSUSPEND_FEE)
            .deactivationFee(UPDATED_DEACTIVATION_FEE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        rateplan = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRateplan != null) {
            rateplanRepository.delete(insertedRateplan);
            insertedRateplan = null;
        }
    }

    @Test
    @Transactional
    void createRateplan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rateplan
        var returnedRateplan = om.readValue(
            restRateplanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplan)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Rateplan.class
        );

        // Validate the Rateplan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRateplanUpdatableFieldsEquals(returnedRateplan, getPersistedRateplan(returnedRateplan));

        insertedRateplan = returnedRateplan;
    }

    @Test
    @Transactional
    void createRateplanWithExistingId() throws Exception {
        // Create the Rateplan with an existing ID
        rateplan.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRateplanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplan)))
            .andExpect(status().isBadRequest());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rateplan.setName(null);

        // Create the Rateplan, which fails.

        restRateplanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplan)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRateplans() throws Exception {
        // Initialize the database
        insertedRateplan = rateplanRepository.saveAndFlush(rateplan);

        // Get all the rateplanList
        restRateplanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rateplan.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].activationFee").value(hasItem(sameNumber(DEFAULT_ACTIVATION_FEE))))
            .andExpect(jsonPath("$.[*].networkAccessFee").value(hasItem(sameNumber(DEFAULT_NETWORK_ACCESS_FEE))))
            .andExpect(jsonPath("$.[*].subscriptionFee").value(hasItem(sameNumber(DEFAULT_SUBSCRIPTION_FEE))))
            .andExpect(jsonPath("$.[*].cancellationFee").value(hasItem(sameNumber(DEFAULT_CANCELLATION_FEE))))
            .andExpect(jsonPath("$.[*].additionalFees").value(hasItem(sameNumber(DEFAULT_ADDITIONAL_FEES))))
            .andExpect(jsonPath("$.[*].reActivationFee").value(hasItem(sameNumber(DEFAULT_RE_ACTIVATION_FEE))))
            .andExpect(jsonPath("$.[*].contractPeriod").value(hasItem(DEFAULT_CONTRACT_PERIOD)))
            .andExpect(jsonPath("$.[*].noticePeriod").value(hasItem(DEFAULT_NOTICE_PERIOD)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].planType").value(hasItem(DEFAULT_PLAN_TYPE)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].subscriptionType").value(hasItem(DEFAULT_SUBSCRIPTION_TYPE)))
            .andExpect(jsonPath("$.[*].includedTestData").value(hasItem(DEFAULT_INCLUDED_TEST_DATA.intValue())))
            .andExpect(jsonPath("$.[*].includedTestSms").value(hasItem(DEFAULT_INCLUDED_TEST_SMS.intValue())))
            .andExpect(jsonPath("$.[*].includedTestVoice").value(hasItem(DEFAULT_INCLUDED_TEST_VOICE.intValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].trialDuration").value(hasItem(DEFAULT_TRIAL_DURATION)))
            .andExpect(jsonPath("$.[*].poolTariff").value(hasItem(DEFAULT_POOL_TARIFF)))
            .andExpect(jsonPath("$.[*].poolActivationFee").value(hasItem(sameNumber(DEFAULT_POOL_ACTIVATION_FEE))))
            .andExpect(jsonPath("$.[*].poolSubscriptionFee").value(hasItem(sameNumber(DEFAULT_POOL_SUBSCRIPTION_FEE))))
            .andExpect(jsonPath("$.[*].roundingForData").value(hasItem(DEFAULT_ROUNDING_FOR_DATA)))
            .andExpect(jsonPath("$.[*].roundingForVoice").value(hasItem(DEFAULT_ROUNDING_FOR_VOICE)))
            .andExpect(jsonPath("$.[*].includedTestDataUnit").value(hasItem(DEFAULT_INCLUDED_TEST_DATA_UNIT)))
            .andExpect(jsonPath("$.[*].includedTestSmsUnit").value(hasItem(DEFAULT_INCLUDED_TEST_SMS_UNIT)))
            .andExpect(jsonPath("$.[*].includedTestVoiceUnit").value(hasItem(DEFAULT_INCLUDED_TEST_VOICE_UNIT)))
            .andExpect(jsonPath("$.[*].renewOnExpiry").value(hasItem(DEFAULT_RENEW_ON_EXPIRY)))
            .andExpect(jsonPath("$.[*].tieredVolumeType").value(hasItem(DEFAULT_TIERED_VOLUME_TYPE)))
            .andExpect(jsonPath("$.[*].validityPeriod").value(hasItem(DEFAULT_VALIDITY_PERIOD)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].quotaProfileId").value(hasItem(DEFAULT_QUOTA_PROFILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].quotaProfileName").value(hasItem(DEFAULT_QUOTA_PROFILE_NAME)))
            .andExpect(jsonPath("$.[*].extId").value(hasItem(DEFAULT_EXT_ID)))
            .andExpect(jsonPath("$.[*].fixedActiveFee").value(hasItem(sameNumber(DEFAULT_FIXED_ACTIVE_FEE))))
            .andExpect(jsonPath("$.[*].suspendFee").value(hasItem(sameNumber(DEFAULT_SUSPEND_FEE))))
            .andExpect(jsonPath("$.[*].suspendedSimMrc").value(hasItem(sameNumber(DEFAULT_SUSPENDED_SIM_MRC))))
            .andExpect(jsonPath("$.[*].unsuspendFee").value(hasItem(sameNumber(DEFAULT_UNSUSPEND_FEE))))
            .andExpect(jsonPath("$.[*].deactivationFee").value(hasItem(sameNumber(DEFAULT_DEACTIVATION_FEE))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRateplansWithEagerRelationshipsIsEnabled() throws Exception {
        when(rateplanRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restRateplanMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rateplanRepositoryMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRateplansWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rateplanRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restRateplanMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(rateplanRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRateplan() throws Exception {
        // Initialize the database
        insertedRateplan = rateplanRepository.saveAndFlush(rateplan);

        // Get the rateplan
        restRateplanMockMvc
            .perform(get(ENTITY_API_URL_ID, rateplan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rateplan.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
            .andExpect(jsonPath("$.activationFee").value(sameNumber(DEFAULT_ACTIVATION_FEE)))
            .andExpect(jsonPath("$.networkAccessFee").value(sameNumber(DEFAULT_NETWORK_ACCESS_FEE)))
            .andExpect(jsonPath("$.subscriptionFee").value(sameNumber(DEFAULT_SUBSCRIPTION_FEE)))
            .andExpect(jsonPath("$.cancellationFee").value(sameNumber(DEFAULT_CANCELLATION_FEE)))
            .andExpect(jsonPath("$.additionalFees").value(sameNumber(DEFAULT_ADDITIONAL_FEES)))
            .andExpect(jsonPath("$.reActivationFee").value(sameNumber(DEFAULT_RE_ACTIVATION_FEE)))
            .andExpect(jsonPath("$.contractPeriod").value(DEFAULT_CONTRACT_PERIOD))
            .andExpect(jsonPath("$.noticePeriod").value(DEFAULT_NOTICE_PERIOD))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.planType").value(DEFAULT_PLAN_TYPE))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.subscriptionType").value(DEFAULT_SUBSCRIPTION_TYPE))
            .andExpect(jsonPath("$.includedTestData").value(DEFAULT_INCLUDED_TEST_DATA.intValue()))
            .andExpect(jsonPath("$.includedTestSms").value(DEFAULT_INCLUDED_TEST_SMS.intValue()))
            .andExpect(jsonPath("$.includedTestVoice").value(DEFAULT_INCLUDED_TEST_VOICE.intValue()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.trialDuration").value(DEFAULT_TRIAL_DURATION))
            .andExpect(jsonPath("$.poolTariff").value(DEFAULT_POOL_TARIFF))
            .andExpect(jsonPath("$.poolActivationFee").value(sameNumber(DEFAULT_POOL_ACTIVATION_FEE)))
            .andExpect(jsonPath("$.poolSubscriptionFee").value(sameNumber(DEFAULT_POOL_SUBSCRIPTION_FEE)))
            .andExpect(jsonPath("$.roundingForData").value(DEFAULT_ROUNDING_FOR_DATA))
            .andExpect(jsonPath("$.roundingForVoice").value(DEFAULT_ROUNDING_FOR_VOICE))
            .andExpect(jsonPath("$.includedTestDataUnit").value(DEFAULT_INCLUDED_TEST_DATA_UNIT))
            .andExpect(jsonPath("$.includedTestSmsUnit").value(DEFAULT_INCLUDED_TEST_SMS_UNIT))
            .andExpect(jsonPath("$.includedTestVoiceUnit").value(DEFAULT_INCLUDED_TEST_VOICE_UNIT))
            .andExpect(jsonPath("$.renewOnExpiry").value(DEFAULT_RENEW_ON_EXPIRY))
            .andExpect(jsonPath("$.tieredVolumeType").value(DEFAULT_TIERED_VOLUME_TYPE))
            .andExpect(jsonPath("$.validityPeriod").value(DEFAULT_VALIDITY_PERIOD))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.quotaProfileId").value(DEFAULT_QUOTA_PROFILE_ID.intValue()))
            .andExpect(jsonPath("$.quotaProfileName").value(DEFAULT_QUOTA_PROFILE_NAME))
            .andExpect(jsonPath("$.extId").value(DEFAULT_EXT_ID))
            .andExpect(jsonPath("$.fixedActiveFee").value(sameNumber(DEFAULT_FIXED_ACTIVE_FEE)))
            .andExpect(jsonPath("$.suspendFee").value(sameNumber(DEFAULT_SUSPEND_FEE)))
            .andExpect(jsonPath("$.suspendedSimMrc").value(sameNumber(DEFAULT_SUSPENDED_SIM_MRC)))
            .andExpect(jsonPath("$.unsuspendFee").value(sameNumber(DEFAULT_UNSUSPEND_FEE)))
            .andExpect(jsonPath("$.deactivationFee").value(sameNumber(DEFAULT_DEACTIVATION_FEE)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRateplan() throws Exception {
        // Get the rateplan
        restRateplanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRateplan() throws Exception {
        // Initialize the database
        insertedRateplan = rateplanRepository.saveAndFlush(rateplan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateplan
        Rateplan updatedRateplan = rateplanRepository.findById(rateplan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRateplan are not directly saved in db
        em.detach(updatedRateplan);
        updatedRateplan
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .activationFee(UPDATED_ACTIVATION_FEE)
            .networkAccessFee(UPDATED_NETWORK_ACCESS_FEE)
            .subscriptionFee(UPDATED_SUBSCRIPTION_FEE)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .additionalFees(UPDATED_ADDITIONAL_FEES)
            .reActivationFee(UPDATED_RE_ACTIVATION_FEE)
            .contractPeriod(UPDATED_CONTRACT_PERIOD)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .planType(UPDATED_PLAN_TYPE)
            .version(UPDATED_VERSION)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .includedTestData(UPDATED_INCLUDED_TEST_DATA)
            .includedTestSms(UPDATED_INCLUDED_TEST_SMS)
            .includedTestVoice(UPDATED_INCLUDED_TEST_VOICE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .trialDuration(UPDATED_TRIAL_DURATION)
            .poolTariff(UPDATED_POOL_TARIFF)
            .poolActivationFee(UPDATED_POOL_ACTIVATION_FEE)
            .poolSubscriptionFee(UPDATED_POOL_SUBSCRIPTION_FEE)
            .roundingForData(UPDATED_ROUNDING_FOR_DATA)
            .roundingForVoice(UPDATED_ROUNDING_FOR_VOICE)
            .includedTestDataUnit(UPDATED_INCLUDED_TEST_DATA_UNIT)
            .includedTestSmsUnit(UPDATED_INCLUDED_TEST_SMS_UNIT)
            .includedTestVoiceUnit(UPDATED_INCLUDED_TEST_VOICE_UNIT)
            .renewOnExpiry(UPDATED_RENEW_ON_EXPIRY)
            .tieredVolumeType(UPDATED_TIERED_VOLUME_TYPE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .category(UPDATED_CATEGORY)
            .quotaProfileId(UPDATED_QUOTA_PROFILE_ID)
            .quotaProfileName(UPDATED_QUOTA_PROFILE_NAME)
            .extId(UPDATED_EXT_ID)
            .fixedActiveFee(UPDATED_FIXED_ACTIVE_FEE)
            .suspendFee(UPDATED_SUSPEND_FEE)
            .suspendedSimMrc(UPDATED_SUSPENDED_SIM_MRC)
            .unsuspendFee(UPDATED_UNSUSPEND_FEE)
            .deactivationFee(UPDATED_DEACTIVATION_FEE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRateplanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRateplan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRateplan))
            )
            .andExpect(status().isOk());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRateplanToMatchAllProperties(updatedRateplan);
    }

    @Test
    @Transactional
    void putNonExistingRateplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplan.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateplanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rateplan.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRateplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rateplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRateplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRateplanWithPatch() throws Exception {
        // Initialize the database
        insertedRateplan = rateplanRepository.saveAndFlush(rateplan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateplan using partial update
        Rateplan partialUpdatedRateplan = new Rateplan();
        partialUpdatedRateplan.setId(rateplan.getId());

        partialUpdatedRateplan
            .name(UPDATED_NAME)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .activationFee(UPDATED_ACTIVATION_FEE)
            .additionalFees(UPDATED_ADDITIONAL_FEES)
            .reActivationFee(UPDATED_RE_ACTIVATION_FEE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .planType(UPDATED_PLAN_TYPE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .includedTestData(UPDATED_INCLUDED_TEST_DATA)
            .includedTestVoice(UPDATED_INCLUDED_TEST_VOICE)
            .trialDuration(UPDATED_TRIAL_DURATION)
            .poolActivationFee(UPDATED_POOL_ACTIVATION_FEE)
            .roundingForVoice(UPDATED_ROUNDING_FOR_VOICE)
            .includedTestDataUnit(UPDATED_INCLUDED_TEST_DATA_UNIT)
            .includedTestVoiceUnit(UPDATED_INCLUDED_TEST_VOICE_UNIT)
            .tieredVolumeType(UPDATED_TIERED_VOLUME_TYPE)
            .quotaProfileId(UPDATED_QUOTA_PROFILE_ID)
            .extId(UPDATED_EXT_ID)
            .fixedActiveFee(UPDATED_FIXED_ACTIVE_FEE)
            .suspendedSimMrc(UPDATED_SUSPENDED_SIM_MRC)
            .unsuspendFee(UPDATED_UNSUSPEND_FEE)
            .createdDate(UPDATED_CREATED_DATE);

        restRateplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRateplan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRateplan))
            )
            .andExpect(status().isOk());

        // Validate the Rateplan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRateplanUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRateplan, rateplan), getPersistedRateplan(rateplan));
    }

    @Test
    @Transactional
    void fullUpdateRateplanWithPatch() throws Exception {
        // Initialize the database
        insertedRateplan = rateplanRepository.saveAndFlush(rateplan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateplan using partial update
        Rateplan partialUpdatedRateplan = new Rateplan();
        partialUpdatedRateplan.setId(rateplan.getId());

        partialUpdatedRateplan
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .activationFee(UPDATED_ACTIVATION_FEE)
            .networkAccessFee(UPDATED_NETWORK_ACCESS_FEE)
            .subscriptionFee(UPDATED_SUBSCRIPTION_FEE)
            .cancellationFee(UPDATED_CANCELLATION_FEE)
            .additionalFees(UPDATED_ADDITIONAL_FEES)
            .reActivationFee(UPDATED_RE_ACTIVATION_FEE)
            .contractPeriod(UPDATED_CONTRACT_PERIOD)
            .noticePeriod(UPDATED_NOTICE_PERIOD)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .planType(UPDATED_PLAN_TYPE)
            .version(UPDATED_VERSION)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .includedTestData(UPDATED_INCLUDED_TEST_DATA)
            .includedTestSms(UPDATED_INCLUDED_TEST_SMS)
            .includedTestVoice(UPDATED_INCLUDED_TEST_VOICE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .trialDuration(UPDATED_TRIAL_DURATION)
            .poolTariff(UPDATED_POOL_TARIFF)
            .poolActivationFee(UPDATED_POOL_ACTIVATION_FEE)
            .poolSubscriptionFee(UPDATED_POOL_SUBSCRIPTION_FEE)
            .roundingForData(UPDATED_ROUNDING_FOR_DATA)
            .roundingForVoice(UPDATED_ROUNDING_FOR_VOICE)
            .includedTestDataUnit(UPDATED_INCLUDED_TEST_DATA_UNIT)
            .includedTestSmsUnit(UPDATED_INCLUDED_TEST_SMS_UNIT)
            .includedTestVoiceUnit(UPDATED_INCLUDED_TEST_VOICE_UNIT)
            .renewOnExpiry(UPDATED_RENEW_ON_EXPIRY)
            .tieredVolumeType(UPDATED_TIERED_VOLUME_TYPE)
            .validityPeriod(UPDATED_VALIDITY_PERIOD)
            .category(UPDATED_CATEGORY)
            .quotaProfileId(UPDATED_QUOTA_PROFILE_ID)
            .quotaProfileName(UPDATED_QUOTA_PROFILE_NAME)
            .extId(UPDATED_EXT_ID)
            .fixedActiveFee(UPDATED_FIXED_ACTIVE_FEE)
            .suspendFee(UPDATED_SUSPEND_FEE)
            .suspendedSimMrc(UPDATED_SUSPENDED_SIM_MRC)
            .unsuspendFee(UPDATED_UNSUSPEND_FEE)
            .deactivationFee(UPDATED_DEACTIVATION_FEE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRateplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRateplan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRateplan))
            )
            .andExpect(status().isOk());

        // Validate the Rateplan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRateplanUpdatableFieldsEquals(partialUpdatedRateplan, getPersistedRateplan(partialUpdatedRateplan));
    }

    @Test
    @Transactional
    void patchNonExistingRateplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplan.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rateplan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rateplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRateplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rateplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRateplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rateplan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rateplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRateplan() throws Exception {
        // Initialize the database
        insertedRateplan = rateplanRepository.saveAndFlush(rateplan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rateplan
        restRateplanMockMvc
            .perform(delete(ENTITY_API_URL_ID, rateplan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rateplanRepository.count();
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

    protected Rateplan getPersistedRateplan(Rateplan rateplan) {
        return rateplanRepository.findById(rateplan.getId()).orElseThrow();
    }

    protected void assertPersistedRateplanToMatchAllProperties(Rateplan expectedRateplan) {
        assertRateplanAllPropertiesEquals(expectedRateplan, getPersistedRateplan(expectedRateplan));
    }

    protected void assertPersistedRateplanToMatchUpdatableProperties(Rateplan expectedRateplan) {
        assertRateplanAllUpdatablePropertiesEquals(expectedRateplan, getPersistedRateplan(expectedRateplan));
    }
}
