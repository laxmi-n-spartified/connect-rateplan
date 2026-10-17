package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.RatingGroupAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.RatingGroup;
import com.spartified.connectrateplan.repository.RatingGroupRepository;
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
 * Integration tests for the {@link RatingGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RatingGroupResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_RG_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/rating-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RatingGroupRepository ratingGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRatingGroupMockMvc;

    private RatingGroup ratingGroup;

    private RatingGroup insertedRatingGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingGroup createEntity() {
        return new RatingGroup()
            .tenantId(DEFAULT_TENANT_ID)
            .rgValue(DEFAULT_RG_VALUE)
            .isDefault(DEFAULT_IS_DEFAULT)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingGroup createUpdatedEntity() {
        return new RatingGroup()
            .tenantId(UPDATED_TENANT_ID)
            .rgValue(UPDATED_RG_VALUE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        ratingGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRatingGroup != null) {
            ratingGroupRepository.delete(insertedRatingGroup);
            insertedRatingGroup = null;
        }
    }

    @Test
    @Transactional
    void createRatingGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RatingGroup
        var returnedRatingGroup = om.readValue(
            restRatingGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingGroup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RatingGroup.class
        );

        // Validate the RatingGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRatingGroupUpdatableFieldsEquals(returnedRatingGroup, getPersistedRatingGroup(returnedRatingGroup));

        insertedRatingGroup = returnedRatingGroup;
    }

    @Test
    @Transactional
    void createRatingGroupWithExistingId() throws Exception {
        // Create the RatingGroup with an existing ID
        ratingGroup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingGroup)))
            .andExpect(status().isBadRequest());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRatingGroups() throws Exception {
        // Initialize the database
        insertedRatingGroup = ratingGroupRepository.saveAndFlush(ratingGroup);

        // Get all the ratingGroupList
        restRatingGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ratingGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].rgValue").value(hasItem(DEFAULT_RG_VALUE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRatingGroup() throws Exception {
        // Initialize the database
        insertedRatingGroup = ratingGroupRepository.saveAndFlush(ratingGroup);

        // Get the ratingGroup
        restRatingGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, ratingGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ratingGroup.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.rgValue").value(DEFAULT_RG_VALUE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRatingGroup() throws Exception {
        // Get the ratingGroup
        restRatingGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRatingGroup() throws Exception {
        // Initialize the database
        insertedRatingGroup = ratingGroupRepository.saveAndFlush(ratingGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ratingGroup
        RatingGroup updatedRatingGroup = ratingGroupRepository.findById(ratingGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRatingGroup are not directly saved in db
        em.detach(updatedRatingGroup);
        updatedRatingGroup
            .tenantId(UPDATED_TENANT_ID)
            .rgValue(UPDATED_RG_VALUE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRatingGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRatingGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRatingGroup))
            )
            .andExpect(status().isOk());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRatingGroupToMatchAllProperties(updatedRatingGroup);
    }

    @Test
    @Transactional
    void putNonExistingRatingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ratingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRatingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ratingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRatingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRatingGroupWithPatch() throws Exception {
        // Initialize the database
        insertedRatingGroup = ratingGroupRepository.saveAndFlush(ratingGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ratingGroup using partial update
        RatingGroup partialUpdatedRatingGroup = new RatingGroup();
        partialUpdatedRatingGroup.setId(ratingGroup.getId());

        partialUpdatedRatingGroup.isDefault(UPDATED_IS_DEFAULT).modifiedDate(UPDATED_MODIFIED_DATE);

        restRatingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRatingGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRatingGroup))
            )
            .andExpect(status().isOk());

        // Validate the RatingGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRatingGroup, ratingGroup),
            getPersistedRatingGroup(ratingGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateRatingGroupWithPatch() throws Exception {
        // Initialize the database
        insertedRatingGroup = ratingGroupRepository.saveAndFlush(ratingGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ratingGroup using partial update
        RatingGroup partialUpdatedRatingGroup = new RatingGroup();
        partialUpdatedRatingGroup.setId(ratingGroup.getId());

        partialUpdatedRatingGroup
            .tenantId(UPDATED_TENANT_ID)
            .rgValue(UPDATED_RG_VALUE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRatingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRatingGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRatingGroup))
            )
            .andExpect(status().isOk());

        // Validate the RatingGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingGroupUpdatableFieldsEquals(partialUpdatedRatingGroup, getPersistedRatingGroup(partialUpdatedRatingGroup));
    }

    @Test
    @Transactional
    void patchNonExistingRatingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ratingGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRatingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRatingGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ratingGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RatingGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRatingGroup() throws Exception {
        // Initialize the database
        insertedRatingGroup = ratingGroupRepository.saveAndFlush(ratingGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ratingGroup
        restRatingGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, ratingGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ratingGroupRepository.count();
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

    protected RatingGroup getPersistedRatingGroup(RatingGroup ratingGroup) {
        return ratingGroupRepository.findById(ratingGroup.getId()).orElseThrow();
    }

    protected void assertPersistedRatingGroupToMatchAllProperties(RatingGroup expectedRatingGroup) {
        assertRatingGroupAllPropertiesEquals(expectedRatingGroup, getPersistedRatingGroup(expectedRatingGroup));
    }

    protected void assertPersistedRatingGroupToMatchUpdatableProperties(RatingGroup expectedRatingGroup) {
        assertRatingGroupAllUpdatablePropertiesEquals(expectedRatingGroup, getPersistedRatingGroup(expectedRatingGroup));
    }
}
