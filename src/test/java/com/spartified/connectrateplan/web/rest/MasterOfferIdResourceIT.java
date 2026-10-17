package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.MasterOfferIdAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.MasterOfferId;
import com.spartified.connectrateplan.repository.MasterOfferIdRepository;
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
 * Integration tests for the {@link MasterOfferIdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MasterOfferIdResourceIT {

    private static final String DEFAULT_OFFER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OFFER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OFFER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OFFER_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/master-offer-ids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MasterOfferIdRepository masterOfferIdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMasterOfferIdMockMvc;

    private MasterOfferId masterOfferId;

    private MasterOfferId insertedMasterOfferId;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterOfferId createEntity() {
        return new MasterOfferId()
            .offerId(DEFAULT_OFFER_ID)
            .offerName(DEFAULT_OFFER_NAME)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MasterOfferId createUpdatedEntity() {
        return new MasterOfferId()
            .offerId(UPDATED_OFFER_ID)
            .offerName(UPDATED_OFFER_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        masterOfferId = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMasterOfferId != null) {
            masterOfferIdRepository.delete(insertedMasterOfferId);
            insertedMasterOfferId = null;
        }
    }

    @Test
    @Transactional
    void createMasterOfferId() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MasterOfferId
        var returnedMasterOfferId = om.readValue(
            restMasterOfferIdMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterOfferId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MasterOfferId.class
        );

        // Validate the MasterOfferId in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMasterOfferIdUpdatableFieldsEquals(returnedMasterOfferId, getPersistedMasterOfferId(returnedMasterOfferId));

        insertedMasterOfferId = returnedMasterOfferId;
    }

    @Test
    @Transactional
    void createMasterOfferIdWithExistingId() throws Exception {
        // Create the MasterOfferId with an existing ID
        masterOfferId.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterOfferIdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterOfferId)))
            .andExpect(status().isBadRequest());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMasterOfferIds() throws Exception {
        // Initialize the database
        insertedMasterOfferId = masterOfferIdRepository.saveAndFlush(masterOfferId);

        // Get all the masterOfferIdList
        restMasterOfferIdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterOfferId.getId().intValue())))
            .andExpect(jsonPath("$.[*].offerId").value(hasItem(DEFAULT_OFFER_ID)))
            .andExpect(jsonPath("$.[*].offerName").value(hasItem(DEFAULT_OFFER_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMasterOfferId() throws Exception {
        // Initialize the database
        insertedMasterOfferId = masterOfferIdRepository.saveAndFlush(masterOfferId);

        // Get the masterOfferId
        restMasterOfferIdMockMvc
            .perform(get(ENTITY_API_URL_ID, masterOfferId.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(masterOfferId.getId().intValue()))
            .andExpect(jsonPath("$.offerId").value(DEFAULT_OFFER_ID))
            .andExpect(jsonPath("$.offerName").value(DEFAULT_OFFER_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMasterOfferId() throws Exception {
        // Get the masterOfferId
        restMasterOfferIdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMasterOfferId() throws Exception {
        // Initialize the database
        insertedMasterOfferId = masterOfferIdRepository.saveAndFlush(masterOfferId);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterOfferId
        MasterOfferId updatedMasterOfferId = masterOfferIdRepository.findById(masterOfferId.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMasterOfferId are not directly saved in db
        em.detach(updatedMasterOfferId);
        updatedMasterOfferId
            .offerId(UPDATED_OFFER_ID)
            .offerName(UPDATED_OFFER_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restMasterOfferIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMasterOfferId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMasterOfferId))
            )
            .andExpect(status().isOk());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMasterOfferIdToMatchAllProperties(updatedMasterOfferId);
    }

    @Test
    @Transactional
    void putNonExistingMasterOfferId() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterOfferId.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterOfferIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, masterOfferId.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(masterOfferId))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMasterOfferId() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterOfferId.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterOfferIdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(masterOfferId))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMasterOfferId() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterOfferId.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterOfferIdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(masterOfferId)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMasterOfferIdWithPatch() throws Exception {
        // Initialize the database
        insertedMasterOfferId = masterOfferIdRepository.saveAndFlush(masterOfferId);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterOfferId using partial update
        MasterOfferId partialUpdatedMasterOfferId = new MasterOfferId();
        partialUpdatedMasterOfferId.setId(masterOfferId.getId());

        partialUpdatedMasterOfferId.offerId(UPDATED_OFFER_ID).createdDate(UPDATED_CREATED_DATE);

        restMasterOfferIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterOfferId.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMasterOfferId))
            )
            .andExpect(status().isOk());

        // Validate the MasterOfferId in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMasterOfferIdUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMasterOfferId, masterOfferId),
            getPersistedMasterOfferId(masterOfferId)
        );
    }

    @Test
    @Transactional
    void fullUpdateMasterOfferIdWithPatch() throws Exception {
        // Initialize the database
        insertedMasterOfferId = masterOfferIdRepository.saveAndFlush(masterOfferId);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the masterOfferId using partial update
        MasterOfferId partialUpdatedMasterOfferId = new MasterOfferId();
        partialUpdatedMasterOfferId.setId(masterOfferId.getId());

        partialUpdatedMasterOfferId
            .offerId(UPDATED_OFFER_ID)
            .offerName(UPDATED_OFFER_NAME)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restMasterOfferIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMasterOfferId.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMasterOfferId))
            )
            .andExpect(status().isOk());

        // Validate the MasterOfferId in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMasterOfferIdUpdatableFieldsEquals(partialUpdatedMasterOfferId, getPersistedMasterOfferId(partialUpdatedMasterOfferId));
    }

    @Test
    @Transactional
    void patchNonExistingMasterOfferId() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterOfferId.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMasterOfferIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, masterOfferId.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(masterOfferId))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMasterOfferId() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterOfferId.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterOfferIdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(masterOfferId))
            )
            .andExpect(status().isBadRequest());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMasterOfferId() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        masterOfferId.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMasterOfferIdMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(masterOfferId)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MasterOfferId in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMasterOfferId() throws Exception {
        // Initialize the database
        insertedMasterOfferId = masterOfferIdRepository.saveAndFlush(masterOfferId);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the masterOfferId
        restMasterOfferIdMockMvc
            .perform(delete(ENTITY_API_URL_ID, masterOfferId.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return masterOfferIdRepository.count();
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

    protected MasterOfferId getPersistedMasterOfferId(MasterOfferId masterOfferId) {
        return masterOfferIdRepository.findById(masterOfferId.getId()).orElseThrow();
    }

    protected void assertPersistedMasterOfferIdToMatchAllProperties(MasterOfferId expectedMasterOfferId) {
        assertMasterOfferIdAllPropertiesEquals(expectedMasterOfferId, getPersistedMasterOfferId(expectedMasterOfferId));
    }

    protected void assertPersistedMasterOfferIdToMatchUpdatableProperties(MasterOfferId expectedMasterOfferId) {
        assertMasterOfferIdAllUpdatablePropertiesEquals(expectedMasterOfferId, getPersistedMasterOfferId(expectedMasterOfferId));
    }
}
