package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.NetworkIdentityAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.NetworkIdentity;
import com.spartified.connectrateplan.repository.NetworkIdentityRepository;
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
 * Integration tests for the {@link NetworkIdentityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NetworkIdentityResourceIT {

    private static final String DEFAULT_MCC = "AAAAAAAAAA";
    private static final String UPDATED_MCC = "BBBBBBBBBB";

    private static final String DEFAULT_MNC = "AAAAAAAAAA";
    private static final String UPDATED_MNC = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATOR = "AAAAAAAAAA";
    private static final String UPDATED_OPERATOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/network-identities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NetworkIdentityRepository networkIdentityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNetworkIdentityMockMvc;

    private NetworkIdentity networkIdentity;

    private NetworkIdentity insertedNetworkIdentity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetworkIdentity createEntity() {
        return new NetworkIdentity()
            .mcc(DEFAULT_MCC)
            .mnc(DEFAULT_MNC)
            .operator(DEFAULT_OPERATOR)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NetworkIdentity createUpdatedEntity() {
        return new NetworkIdentity()
            .mcc(UPDATED_MCC)
            .mnc(UPDATED_MNC)
            .operator(UPDATED_OPERATOR)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        networkIdentity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNetworkIdentity != null) {
            networkIdentityRepository.delete(insertedNetworkIdentity);
            insertedNetworkIdentity = null;
        }
    }

    @Test
    @Transactional
    void createNetworkIdentity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NetworkIdentity
        var returnedNetworkIdentity = om.readValue(
            restNetworkIdentityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(networkIdentity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NetworkIdentity.class
        );

        // Validate the NetworkIdentity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNetworkIdentityUpdatableFieldsEquals(returnedNetworkIdentity, getPersistedNetworkIdentity(returnedNetworkIdentity));

        insertedNetworkIdentity = returnedNetworkIdentity;
    }

    @Test
    @Transactional
    void createNetworkIdentityWithExistingId() throws Exception {
        // Create the NetworkIdentity with an existing ID
        networkIdentity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNetworkIdentityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(networkIdentity)))
            .andExpect(status().isBadRequest());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNetworkIdentities() throws Exception {
        // Initialize the database
        insertedNetworkIdentity = networkIdentityRepository.saveAndFlush(networkIdentity);

        // Get all the networkIdentityList
        restNetworkIdentityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(networkIdentity.getId().intValue())))
            .andExpect(jsonPath("$.[*].mcc").value(hasItem(DEFAULT_MCC)))
            .andExpect(jsonPath("$.[*].mnc").value(hasItem(DEFAULT_MNC)))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNetworkIdentity() throws Exception {
        // Initialize the database
        insertedNetworkIdentity = networkIdentityRepository.saveAndFlush(networkIdentity);

        // Get the networkIdentity
        restNetworkIdentityMockMvc
            .perform(get(ENTITY_API_URL_ID, networkIdentity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(networkIdentity.getId().intValue()))
            .andExpect(jsonPath("$.mcc").value(DEFAULT_MCC))
            .andExpect(jsonPath("$.mnc").value(DEFAULT_MNC))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNetworkIdentity() throws Exception {
        // Get the networkIdentity
        restNetworkIdentityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNetworkIdentity() throws Exception {
        // Initialize the database
        insertedNetworkIdentity = networkIdentityRepository.saveAndFlush(networkIdentity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the networkIdentity
        NetworkIdentity updatedNetworkIdentity = networkIdentityRepository.findById(networkIdentity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNetworkIdentity are not directly saved in db
        em.detach(updatedNetworkIdentity);
        updatedNetworkIdentity
            .mcc(UPDATED_MCC)
            .mnc(UPDATED_MNC)
            .operator(UPDATED_OPERATOR)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restNetworkIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNetworkIdentity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNetworkIdentity))
            )
            .andExpect(status().isOk());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNetworkIdentityToMatchAllProperties(updatedNetworkIdentity);
    }

    @Test
    @Transactional
    void putNonExistingNetworkIdentity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        networkIdentity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetworkIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, networkIdentity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(networkIdentity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNetworkIdentity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        networkIdentity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(networkIdentity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNetworkIdentity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        networkIdentity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkIdentityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(networkIdentity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNetworkIdentityWithPatch() throws Exception {
        // Initialize the database
        insertedNetworkIdentity = networkIdentityRepository.saveAndFlush(networkIdentity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the networkIdentity using partial update
        NetworkIdentity partialUpdatedNetworkIdentity = new NetworkIdentity();
        partialUpdatedNetworkIdentity.setId(networkIdentity.getId());

        partialUpdatedNetworkIdentity.mcc(UPDATED_MCC).operator(UPDATED_OPERATOR).modifiedDate(UPDATED_MODIFIED_DATE);

        restNetworkIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetworkIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNetworkIdentity))
            )
            .andExpect(status().isOk());

        // Validate the NetworkIdentity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNetworkIdentityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNetworkIdentity, networkIdentity),
            getPersistedNetworkIdentity(networkIdentity)
        );
    }

    @Test
    @Transactional
    void fullUpdateNetworkIdentityWithPatch() throws Exception {
        // Initialize the database
        insertedNetworkIdentity = networkIdentityRepository.saveAndFlush(networkIdentity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the networkIdentity using partial update
        NetworkIdentity partialUpdatedNetworkIdentity = new NetworkIdentity();
        partialUpdatedNetworkIdentity.setId(networkIdentity.getId());

        partialUpdatedNetworkIdentity
            .mcc(UPDATED_MCC)
            .mnc(UPDATED_MNC)
            .operator(UPDATED_OPERATOR)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restNetworkIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNetworkIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNetworkIdentity))
            )
            .andExpect(status().isOk());

        // Validate the NetworkIdentity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNetworkIdentityUpdatableFieldsEquals(
            partialUpdatedNetworkIdentity,
            getPersistedNetworkIdentity(partialUpdatedNetworkIdentity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNetworkIdentity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        networkIdentity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNetworkIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, networkIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(networkIdentity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNetworkIdentity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        networkIdentity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(networkIdentity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNetworkIdentity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        networkIdentity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNetworkIdentityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(networkIdentity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NetworkIdentity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNetworkIdentity() throws Exception {
        // Initialize the database
        insertedNetworkIdentity = networkIdentityRepository.saveAndFlush(networkIdentity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the networkIdentity
        restNetworkIdentityMockMvc
            .perform(delete(ENTITY_API_URL_ID, networkIdentity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return networkIdentityRepository.count();
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

    protected NetworkIdentity getPersistedNetworkIdentity(NetworkIdentity networkIdentity) {
        return networkIdentityRepository.findById(networkIdentity.getId()).orElseThrow();
    }

    protected void assertPersistedNetworkIdentityToMatchAllProperties(NetworkIdentity expectedNetworkIdentity) {
        assertNetworkIdentityAllPropertiesEquals(expectedNetworkIdentity, getPersistedNetworkIdentity(expectedNetworkIdentity));
    }

    protected void assertPersistedNetworkIdentityToMatchUpdatableProperties(NetworkIdentity expectedNetworkIdentity) {
        assertNetworkIdentityAllUpdatablePropertiesEquals(expectedNetworkIdentity, getPersistedNetworkIdentity(expectedNetworkIdentity));
    }
}
