package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.AccountBillingProfileAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static com.spartified.connectrateplan.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.AccountBillingProfile;
import com.spartified.connectrateplan.domain.BillingCycle;
import com.spartified.connectrateplan.domain.Currency;
import com.spartified.connectrateplan.repository.AccountBillingProfileRepository;
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
 * Integration tests for the {@link AccountBillingProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountBillingProfileResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MRC_CHARGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MRC_CHARGE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RATING_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RATING_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CREDIT_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_LIMIT = new BigDecimal(2);

    private static final String DEFAULT_MRC_RATING_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MRC_RATING_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BILLABLE = false;
    private static final Boolean UPDATED_BILLABLE = true;

    private static final String DEFAULT_FAN_ID = "AAAAAAAAAA";
    private static final String UPDATED_FAN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_PREFIX = "BBBBBBBBBB";

    private static final String DEFAULT_SAART_ID = "AAAAAA";
    private static final String UPDATED_SAART_ID = "BBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/account-billing-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountBillingProfileRepository accountBillingProfileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountBillingProfileMockMvc;

    private AccountBillingProfile accountBillingProfile;

    private AccountBillingProfile insertedAccountBillingProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBillingProfile createEntity(EntityManager em) {
        AccountBillingProfile accountBillingProfile = new AccountBillingProfile()
            .tenantId(DEFAULT_TENANT_ID)
            .mrcChargeType(DEFAULT_MRC_CHARGE_TYPE)
            .ratingType(DEFAULT_RATING_TYPE)
            .creditLimit(DEFAULT_CREDIT_LIMIT)
            .mrcRatingType(DEFAULT_MRC_RATING_TYPE)
            .billable(DEFAULT_BILLABLE)
            .fanId(DEFAULT_FAN_ID)
            .extId(DEFAULT_EXT_ID)
            .accountPrefix(DEFAULT_ACCOUNT_PREFIX)
            .saartId(DEFAULT_SAART_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        // Add required entity
        BillingCycle billingCycle;
        if (TestUtil.findAll(em, BillingCycle.class).isEmpty()) {
            billingCycle = BillingCycleResourceIT.createEntity();
            em.persist(billingCycle);
            em.flush();
        } else {
            billingCycle = TestUtil.findAll(em, BillingCycle.class).getFirst();
        }
        accountBillingProfile.setBillCycle(billingCycle);
        // Add required entity
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createEntity();
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).getFirst();
        }
        accountBillingProfile.setCurrency(currency);
        return accountBillingProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBillingProfile createUpdatedEntity(EntityManager em) {
        AccountBillingProfile updatedAccountBillingProfile = new AccountBillingProfile()
            .tenantId(UPDATED_TENANT_ID)
            .mrcChargeType(UPDATED_MRC_CHARGE_TYPE)
            .ratingType(UPDATED_RATING_TYPE)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .mrcRatingType(UPDATED_MRC_RATING_TYPE)
            .billable(UPDATED_BILLABLE)
            .fanId(UPDATED_FAN_ID)
            .extId(UPDATED_EXT_ID)
            .accountPrefix(UPDATED_ACCOUNT_PREFIX)
            .saartId(UPDATED_SAART_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        // Add required entity
        BillingCycle billingCycle;
        if (TestUtil.findAll(em, BillingCycle.class).isEmpty()) {
            billingCycle = BillingCycleResourceIT.createUpdatedEntity();
            em.persist(billingCycle);
            em.flush();
        } else {
            billingCycle = TestUtil.findAll(em, BillingCycle.class).getFirst();
        }
        updatedAccountBillingProfile.setBillCycle(billingCycle);
        // Add required entity
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createUpdatedEntity();
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).getFirst();
        }
        updatedAccountBillingProfile.setCurrency(currency);
        return updatedAccountBillingProfile;
    }

    @BeforeEach
    void initTest() {
        accountBillingProfile = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAccountBillingProfile != null) {
            accountBillingProfileRepository.delete(insertedAccountBillingProfile);
            insertedAccountBillingProfile = null;
        }
    }

    @Test
    @Transactional
    void createAccountBillingProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccountBillingProfile
        var returnedAccountBillingProfile = om.readValue(
            restAccountBillingProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBillingProfile)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountBillingProfile.class
        );

        // Validate the AccountBillingProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccountBillingProfileUpdatableFieldsEquals(
            returnedAccountBillingProfile,
            getPersistedAccountBillingProfile(returnedAccountBillingProfile)
        );

        insertedAccountBillingProfile = returnedAccountBillingProfile;
    }

    @Test
    @Transactional
    void createAccountBillingProfileWithExistingId() throws Exception {
        // Create the AccountBillingProfile with an existing ID
        accountBillingProfile.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountBillingProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBillingProfile)))
            .andExpect(status().isBadRequest());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenantIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accountBillingProfile.setTenantId(null);

        // Create the AccountBillingProfile, which fails.

        restAccountBillingProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBillingProfile)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountBillingProfiles() throws Exception {
        // Initialize the database
        insertedAccountBillingProfile = accountBillingProfileRepository.saveAndFlush(accountBillingProfile);

        // Get all the accountBillingProfileList
        restAccountBillingProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBillingProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].mrcChargeType").value(hasItem(DEFAULT_MRC_CHARGE_TYPE)))
            .andExpect(jsonPath("$.[*].ratingType").value(hasItem(DEFAULT_RATING_TYPE)))
            .andExpect(jsonPath("$.[*].creditLimit").value(hasItem(sameNumber(DEFAULT_CREDIT_LIMIT))))
            .andExpect(jsonPath("$.[*].mrcRatingType").value(hasItem(DEFAULT_MRC_RATING_TYPE)))
            .andExpect(jsonPath("$.[*].billable").value(hasItem(DEFAULT_BILLABLE)))
            .andExpect(jsonPath("$.[*].fanId").value(hasItem(DEFAULT_FAN_ID)))
            .andExpect(jsonPath("$.[*].extId").value(hasItem(DEFAULT_EXT_ID)))
            .andExpect(jsonPath("$.[*].accountPrefix").value(hasItem(DEFAULT_ACCOUNT_PREFIX)))
            .andExpect(jsonPath("$.[*].saartId").value(hasItem(DEFAULT_SAART_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAccountBillingProfile() throws Exception {
        // Initialize the database
        insertedAccountBillingProfile = accountBillingProfileRepository.saveAndFlush(accountBillingProfile);

        // Get the accountBillingProfile
        restAccountBillingProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, accountBillingProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountBillingProfile.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.mrcChargeType").value(DEFAULT_MRC_CHARGE_TYPE))
            .andExpect(jsonPath("$.ratingType").value(DEFAULT_RATING_TYPE))
            .andExpect(jsonPath("$.creditLimit").value(sameNumber(DEFAULT_CREDIT_LIMIT)))
            .andExpect(jsonPath("$.mrcRatingType").value(DEFAULT_MRC_RATING_TYPE))
            .andExpect(jsonPath("$.billable").value(DEFAULT_BILLABLE))
            .andExpect(jsonPath("$.fanId").value(DEFAULT_FAN_ID))
            .andExpect(jsonPath("$.extId").value(DEFAULT_EXT_ID))
            .andExpect(jsonPath("$.accountPrefix").value(DEFAULT_ACCOUNT_PREFIX))
            .andExpect(jsonPath("$.saartId").value(DEFAULT_SAART_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccountBillingProfile() throws Exception {
        // Get the accountBillingProfile
        restAccountBillingProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountBillingProfile() throws Exception {
        // Initialize the database
        insertedAccountBillingProfile = accountBillingProfileRepository.saveAndFlush(accountBillingProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountBillingProfile
        AccountBillingProfile updatedAccountBillingProfile = accountBillingProfileRepository
            .findById(accountBillingProfile.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAccountBillingProfile are not directly saved in db
        em.detach(updatedAccountBillingProfile);
        updatedAccountBillingProfile
            .tenantId(UPDATED_TENANT_ID)
            .mrcChargeType(UPDATED_MRC_CHARGE_TYPE)
            .ratingType(UPDATED_RATING_TYPE)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .mrcRatingType(UPDATED_MRC_RATING_TYPE)
            .billable(UPDATED_BILLABLE)
            .fanId(UPDATED_FAN_ID)
            .extId(UPDATED_EXT_ID)
            .accountPrefix(UPDATED_ACCOUNT_PREFIX)
            .saartId(UPDATED_SAART_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restAccountBillingProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountBillingProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccountBillingProfile))
            )
            .andExpect(status().isOk());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountBillingProfileToMatchAllProperties(updatedAccountBillingProfile);
    }

    @Test
    @Transactional
    void putNonExistingAccountBillingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBillingProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBillingProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBillingProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountBillingProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountBillingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBillingProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBillingProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountBillingProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountBillingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBillingProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBillingProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountBillingProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountBillingProfileWithPatch() throws Exception {
        // Initialize the database
        insertedAccountBillingProfile = accountBillingProfileRepository.saveAndFlush(accountBillingProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountBillingProfile using partial update
        AccountBillingProfile partialUpdatedAccountBillingProfile = new AccountBillingProfile();
        partialUpdatedAccountBillingProfile.setId(accountBillingProfile.getId());

        partialUpdatedAccountBillingProfile
            .billable(UPDATED_BILLABLE)
            .extId(UPDATED_EXT_ID)
            .accountPrefix(UPDATED_ACCOUNT_PREFIX)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restAccountBillingProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBillingProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountBillingProfile))
            )
            .andExpect(status().isOk());

        // Validate the AccountBillingProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountBillingProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccountBillingProfile, accountBillingProfile),
            getPersistedAccountBillingProfile(accountBillingProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccountBillingProfileWithPatch() throws Exception {
        // Initialize the database
        insertedAccountBillingProfile = accountBillingProfileRepository.saveAndFlush(accountBillingProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accountBillingProfile using partial update
        AccountBillingProfile partialUpdatedAccountBillingProfile = new AccountBillingProfile();
        partialUpdatedAccountBillingProfile.setId(accountBillingProfile.getId());

        partialUpdatedAccountBillingProfile
            .tenantId(UPDATED_TENANT_ID)
            .mrcChargeType(UPDATED_MRC_CHARGE_TYPE)
            .ratingType(UPDATED_RATING_TYPE)
            .creditLimit(UPDATED_CREDIT_LIMIT)
            .mrcRatingType(UPDATED_MRC_RATING_TYPE)
            .billable(UPDATED_BILLABLE)
            .fanId(UPDATED_FAN_ID)
            .extId(UPDATED_EXT_ID)
            .accountPrefix(UPDATED_ACCOUNT_PREFIX)
            .saartId(UPDATED_SAART_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restAccountBillingProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBillingProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccountBillingProfile))
            )
            .andExpect(status().isOk());

        // Validate the AccountBillingProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountBillingProfileUpdatableFieldsEquals(
            partialUpdatedAccountBillingProfile,
            getPersistedAccountBillingProfile(partialUpdatedAccountBillingProfile)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccountBillingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBillingProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBillingProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountBillingProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountBillingProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountBillingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBillingProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBillingProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountBillingProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountBillingProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accountBillingProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBillingProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountBillingProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBillingProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountBillingProfile() throws Exception {
        // Initialize the database
        insertedAccountBillingProfile = accountBillingProfileRepository.saveAndFlush(accountBillingProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accountBillingProfile
        restAccountBillingProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountBillingProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accountBillingProfileRepository.count();
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

    protected AccountBillingProfile getPersistedAccountBillingProfile(AccountBillingProfile accountBillingProfile) {
        return accountBillingProfileRepository.findById(accountBillingProfile.getId()).orElseThrow();
    }

    protected void assertPersistedAccountBillingProfileToMatchAllProperties(AccountBillingProfile expectedAccountBillingProfile) {
        assertAccountBillingProfileAllPropertiesEquals(
            expectedAccountBillingProfile,
            getPersistedAccountBillingProfile(expectedAccountBillingProfile)
        );
    }

    protected void assertPersistedAccountBillingProfileToMatchUpdatableProperties(AccountBillingProfile expectedAccountBillingProfile) {
        assertAccountBillingProfileAllUpdatablePropertiesEquals(
            expectedAccountBillingProfile,
            getPersistedAccountBillingProfile(expectedAccountBillingProfile)
        );
    }
}
