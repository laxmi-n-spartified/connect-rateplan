package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.BillingCycleAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.BillingCycle;
import com.spartified.connectrateplan.repository.BillingCycleRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BillingCycleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillingCycleResourceIT {

    private static final Integer DEFAULT_BILL_CYCLE_DATE = 1;
    private static final Integer UPDATED_BILL_CYCLE_DATE = 2;

    private static final String DEFAULT_BILL_CYCLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BILL_CYCLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_CYCLE_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_BILL_CYCLE_DURATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/billing-cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillingCycleRepository billingCycleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillingCycleMockMvc;

    private BillingCycle billingCycle;

    private BillingCycle insertedBillingCycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingCycle createEntity() {
        return new BillingCycle()
            .billCycleDate(DEFAULT_BILL_CYCLE_DATE)
            .billCycleName(DEFAULT_BILL_CYCLE_NAME)
            .billCycleDuration(DEFAULT_BILL_CYCLE_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillingCycle createUpdatedEntity() {
        return new BillingCycle()
            .billCycleDate(UPDATED_BILL_CYCLE_DATE)
            .billCycleName(UPDATED_BILL_CYCLE_NAME)
            .billCycleDuration(UPDATED_BILL_CYCLE_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        billingCycle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBillingCycle != null) {
            billingCycleRepository.delete(insertedBillingCycle);
            insertedBillingCycle = null;
        }
    }

    @Test
    @Transactional
    void createBillingCycle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BillingCycle
        var returnedBillingCycle = om.readValue(
            restBillingCycleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingCycle)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BillingCycle.class
        );

        // Validate the BillingCycle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBillingCycleUpdatableFieldsEquals(returnedBillingCycle, getPersistedBillingCycle(returnedBillingCycle));

        insertedBillingCycle = returnedBillingCycle;
    }

    @Test
    @Transactional
    void createBillingCycleWithExistingId() throws Exception {
        // Create the BillingCycle with an existing ID
        billingCycle.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillingCycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingCycle)))
            .andExpect(status().isBadRequest());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBillingCycles() throws Exception {
        // Initialize the database
        insertedBillingCycle = billingCycleRepository.saveAndFlush(billingCycle);

        // Get all the billingCycleList
        restBillingCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].billCycleDate").value(hasItem(DEFAULT_BILL_CYCLE_DATE)))
            .andExpect(jsonPath("$.[*].billCycleName").value(hasItem(DEFAULT_BILL_CYCLE_NAME)))
            .andExpect(jsonPath("$.[*].billCycleDuration").value(hasItem(DEFAULT_BILL_CYCLE_DURATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBillingCycle() throws Exception {
        // Initialize the database
        insertedBillingCycle = billingCycleRepository.saveAndFlush(billingCycle);

        // Get the billingCycle
        restBillingCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, billingCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(billingCycle.getId().intValue()))
            .andExpect(jsonPath("$.billCycleDate").value(DEFAULT_BILL_CYCLE_DATE))
            .andExpect(jsonPath("$.billCycleName").value(DEFAULT_BILL_CYCLE_NAME))
            .andExpect(jsonPath("$.billCycleDuration").value(DEFAULT_BILL_CYCLE_DURATION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBillingCycle() throws Exception {
        // Get the billingCycle
        restBillingCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBillingCycle() throws Exception {
        // Initialize the database
        insertedBillingCycle = billingCycleRepository.saveAndFlush(billingCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billingCycle
        BillingCycle updatedBillingCycle = billingCycleRepository.findById(billingCycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBillingCycle are not directly saved in db
        em.detach(updatedBillingCycle);
        updatedBillingCycle
            .billCycleDate(UPDATED_BILL_CYCLE_DATE)
            .billCycleName(UPDATED_BILL_CYCLE_NAME)
            .billCycleDuration(UPDATED_BILL_CYCLE_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBillingCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBillingCycle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBillingCycle))
            )
            .andExpect(status().isOk());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillingCycleToMatchAllProperties(updatedBillingCycle);
    }

    @Test
    @Transactional
    void putNonExistingBillingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billingCycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, billingCycle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billingCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBillingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billingCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(billingCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBillingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billingCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingCycleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(billingCycle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillingCycleWithPatch() throws Exception {
        // Initialize the database
        insertedBillingCycle = billingCycleRepository.saveAndFlush(billingCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billingCycle using partial update
        BillingCycle partialUpdatedBillingCycle = new BillingCycle();
        partialUpdatedBillingCycle.setId(billingCycle.getId());

        partialUpdatedBillingCycle.billCycleDate(UPDATED_BILL_CYCLE_DATE).billCycleName(UPDATED_BILL_CYCLE_NAME);

        restBillingCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillingCycle))
            )
            .andExpect(status().isOk());

        // Validate the BillingCycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillingCycleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBillingCycle, billingCycle),
            getPersistedBillingCycle(billingCycle)
        );
    }

    @Test
    @Transactional
    void fullUpdateBillingCycleWithPatch() throws Exception {
        // Initialize the database
        insertedBillingCycle = billingCycleRepository.saveAndFlush(billingCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the billingCycle using partial update
        BillingCycle partialUpdatedBillingCycle = new BillingCycle();
        partialUpdatedBillingCycle.setId(billingCycle.getId());

        partialUpdatedBillingCycle
            .billCycleDate(UPDATED_BILL_CYCLE_DATE)
            .billCycleName(UPDATED_BILL_CYCLE_NAME)
            .billCycleDuration(UPDATED_BILL_CYCLE_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restBillingCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBillingCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBillingCycle))
            )
            .andExpect(status().isOk());

        // Validate the BillingCycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillingCycleUpdatableFieldsEquals(partialUpdatedBillingCycle, getPersistedBillingCycle(partialUpdatedBillingCycle));
    }

    @Test
    @Transactional
    void patchNonExistingBillingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billingCycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillingCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, billingCycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billingCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBillingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billingCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(billingCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBillingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        billingCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillingCycleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(billingCycle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BillingCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBillingCycle() throws Exception {
        // Initialize the database
        insertedBillingCycle = billingCycleRepository.saveAndFlush(billingCycle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the billingCycle
        restBillingCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, billingCycle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billingCycleRepository.count();
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

    protected BillingCycle getPersistedBillingCycle(BillingCycle billingCycle) {
        return billingCycleRepository.findById(billingCycle.getId()).orElseThrow();
    }

    protected void assertPersistedBillingCycleToMatchAllProperties(BillingCycle expectedBillingCycle) {
        assertBillingCycleAllPropertiesEquals(expectedBillingCycle, getPersistedBillingCycle(expectedBillingCycle));
    }

    protected void assertPersistedBillingCycleToMatchUpdatableProperties(BillingCycle expectedBillingCycle) {
        assertBillingCycleAllUpdatablePropertiesEquals(expectedBillingCycle, getPersistedBillingCycle(expectedBillingCycle));
    }
}
