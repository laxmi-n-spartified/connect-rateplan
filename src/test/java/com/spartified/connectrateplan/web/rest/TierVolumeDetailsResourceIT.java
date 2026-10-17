package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.TierVolumeDetailsAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static com.spartified.connectrateplan.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.TierVolumeDetails;
import com.spartified.connectrateplan.repository.TierVolumeDetailsRepository;
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
 * Integration tests for the {@link TierVolumeDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TierVolumeDetailsResourceIT {

    private static final BigDecimal DEFAULT_MRC = new BigDecimal(1);
    private static final BigDecimal UPDATED_MRC = new BigDecimal(2);

    private static final String DEFAULT_RATING_TYPE_MRC = "AAAAAAAAAA";
    private static final String UPDATED_RATING_TYPE_MRC = "BBBBBBBBBB";

    private static final String DEFAULT_RATING_TYPE_USAGE = "AAAAAAAAAA";
    private static final String UPDATED_RATING_TYPE_USAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TIER_LIMIT = "AAAAAAAAAA";
    private static final String UPDATED_TIER_LIMIT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/tier-volume-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TierVolumeDetailsRepository tierVolumeDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTierVolumeDetailsMockMvc;

    private TierVolumeDetails tierVolumeDetails;

    private TierVolumeDetails insertedTierVolumeDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TierVolumeDetails createEntity() {
        return new TierVolumeDetails()
            .mrc(DEFAULT_MRC)
            .ratingTypeMrc(DEFAULT_RATING_TYPE_MRC)
            .ratingTypeUsage(DEFAULT_RATING_TYPE_USAGE)
            .tenantId(DEFAULT_TENANT_ID)
            .tierLimit(DEFAULT_TIER_LIMIT)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TierVolumeDetails createUpdatedEntity() {
        return new TierVolumeDetails()
            .mrc(UPDATED_MRC)
            .ratingTypeMrc(UPDATED_RATING_TYPE_MRC)
            .ratingTypeUsage(UPDATED_RATING_TYPE_USAGE)
            .tenantId(UPDATED_TENANT_ID)
            .tierLimit(UPDATED_TIER_LIMIT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        tierVolumeDetails = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTierVolumeDetails != null) {
            tierVolumeDetailsRepository.delete(insertedTierVolumeDetails);
            insertedTierVolumeDetails = null;
        }
    }

    @Test
    @Transactional
    void createTierVolumeDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TierVolumeDetails
        var returnedTierVolumeDetails = om.readValue(
            restTierVolumeDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tierVolumeDetails)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TierVolumeDetails.class
        );

        // Validate the TierVolumeDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTierVolumeDetailsUpdatableFieldsEquals(returnedTierVolumeDetails, getPersistedTierVolumeDetails(returnedTierVolumeDetails));

        insertedTierVolumeDetails = returnedTierVolumeDetails;
    }

    @Test
    @Transactional
    void createTierVolumeDetailsWithExistingId() throws Exception {
        // Create the TierVolumeDetails with an existing ID
        tierVolumeDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTierVolumeDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tierVolumeDetails)))
            .andExpect(status().isBadRequest());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTierVolumeDetailses() throws Exception {
        // Initialize the database
        insertedTierVolumeDetails = tierVolumeDetailsRepository.saveAndFlush(tierVolumeDetails);

        // Get all the tierVolumeDetailsList
        restTierVolumeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tierVolumeDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].mrc").value(hasItem(sameNumber(DEFAULT_MRC))))
            .andExpect(jsonPath("$.[*].ratingTypeMrc").value(hasItem(DEFAULT_RATING_TYPE_MRC)))
            .andExpect(jsonPath("$.[*].ratingTypeUsage").value(hasItem(DEFAULT_RATING_TYPE_USAGE)))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].tierLimit").value(hasItem(DEFAULT_TIER_LIMIT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTierVolumeDetails() throws Exception {
        // Initialize the database
        insertedTierVolumeDetails = tierVolumeDetailsRepository.saveAndFlush(tierVolumeDetails);

        // Get the tierVolumeDetails
        restTierVolumeDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, tierVolumeDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tierVolumeDetails.getId().intValue()))
            .andExpect(jsonPath("$.mrc").value(sameNumber(DEFAULT_MRC)))
            .andExpect(jsonPath("$.ratingTypeMrc").value(DEFAULT_RATING_TYPE_MRC))
            .andExpect(jsonPath("$.ratingTypeUsage").value(DEFAULT_RATING_TYPE_USAGE))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.tierLimit").value(DEFAULT_TIER_LIMIT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTierVolumeDetails() throws Exception {
        // Get the tierVolumeDetails
        restTierVolumeDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTierVolumeDetails() throws Exception {
        // Initialize the database
        insertedTierVolumeDetails = tierVolumeDetailsRepository.saveAndFlush(tierVolumeDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tierVolumeDetails
        TierVolumeDetails updatedTierVolumeDetails = tierVolumeDetailsRepository.findById(tierVolumeDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTierVolumeDetails are not directly saved in db
        em.detach(updatedTierVolumeDetails);
        updatedTierVolumeDetails
            .mrc(UPDATED_MRC)
            .ratingTypeMrc(UPDATED_RATING_TYPE_MRC)
            .ratingTypeUsage(UPDATED_RATING_TYPE_USAGE)
            .tenantId(UPDATED_TENANT_ID)
            .tierLimit(UPDATED_TIER_LIMIT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTierVolumeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTierVolumeDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTierVolumeDetails))
            )
            .andExpect(status().isOk());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTierVolumeDetailsToMatchAllProperties(updatedTierVolumeDetails);
    }

    @Test
    @Transactional
    void putNonExistingTierVolumeDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tierVolumeDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTierVolumeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tierVolumeDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tierVolumeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTierVolumeDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tierVolumeDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierVolumeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tierVolumeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTierVolumeDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tierVolumeDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierVolumeDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tierVolumeDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTierVolumeDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedTierVolumeDetails = tierVolumeDetailsRepository.saveAndFlush(tierVolumeDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tierVolumeDetails using partial update
        TierVolumeDetails partialUpdatedTierVolumeDetails = new TierVolumeDetails();
        partialUpdatedTierVolumeDetails.setId(tierVolumeDetails.getId());

        partialUpdatedTierVolumeDetails
            .ratingTypeMrc(UPDATED_RATING_TYPE_MRC)
            .ratingTypeUsage(UPDATED_RATING_TYPE_USAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restTierVolumeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTierVolumeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTierVolumeDetails))
            )
            .andExpect(status().isOk());

        // Validate the TierVolumeDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTierVolumeDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTierVolumeDetails, tierVolumeDetails),
            getPersistedTierVolumeDetails(tierVolumeDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateTierVolumeDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedTierVolumeDetails = tierVolumeDetailsRepository.saveAndFlush(tierVolumeDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tierVolumeDetails using partial update
        TierVolumeDetails partialUpdatedTierVolumeDetails = new TierVolumeDetails();
        partialUpdatedTierVolumeDetails.setId(tierVolumeDetails.getId());

        partialUpdatedTierVolumeDetails
            .mrc(UPDATED_MRC)
            .ratingTypeMrc(UPDATED_RATING_TYPE_MRC)
            .ratingTypeUsage(UPDATED_RATING_TYPE_USAGE)
            .tenantId(UPDATED_TENANT_ID)
            .tierLimit(UPDATED_TIER_LIMIT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTierVolumeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTierVolumeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTierVolumeDetails))
            )
            .andExpect(status().isOk());

        // Validate the TierVolumeDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTierVolumeDetailsUpdatableFieldsEquals(
            partialUpdatedTierVolumeDetails,
            getPersistedTierVolumeDetails(partialUpdatedTierVolumeDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTierVolumeDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tierVolumeDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTierVolumeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tierVolumeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tierVolumeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTierVolumeDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tierVolumeDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierVolumeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tierVolumeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTierVolumeDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tierVolumeDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierVolumeDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tierVolumeDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TierVolumeDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTierVolumeDetails() throws Exception {
        // Initialize the database
        insertedTierVolumeDetails = tierVolumeDetailsRepository.saveAndFlush(tierVolumeDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tierVolumeDetails
        restTierVolumeDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tierVolumeDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tierVolumeDetailsRepository.count();
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

    protected TierVolumeDetails getPersistedTierVolumeDetails(TierVolumeDetails tierVolumeDetails) {
        return tierVolumeDetailsRepository.findById(tierVolumeDetails.getId()).orElseThrow();
    }

    protected void assertPersistedTierVolumeDetailsToMatchAllProperties(TierVolumeDetails expectedTierVolumeDetails) {
        assertTierVolumeDetailsAllPropertiesEquals(expectedTierVolumeDetails, getPersistedTierVolumeDetails(expectedTierVolumeDetails));
    }

    protected void assertPersistedTierVolumeDetailsToMatchUpdatableProperties(TierVolumeDetails expectedTierVolumeDetails) {
        assertTierVolumeDetailsAllUpdatablePropertiesEquals(
            expectedTierVolumeDetails,
            getPersistedTierVolumeDetails(expectedTierVolumeDetails)
        );
    }
}
