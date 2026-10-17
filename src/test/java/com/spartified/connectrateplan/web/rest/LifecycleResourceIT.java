package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.LifecycleAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.Lifecycle;
import com.spartified.connectrateplan.repository.LifecycleRepository;
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
 * Integration tests for the {@link LifecycleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LifecycleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/lifecycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LifecycleRepository lifecycleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLifecycleMockMvc;

    private Lifecycle lifecycle;

    private Lifecycle insertedLifecycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lifecycle createEntity() {
        return new Lifecycle().name(DEFAULT_NAME).createdDate(DEFAULT_CREATED_DATE).modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lifecycle createUpdatedEntity() {
        return new Lifecycle().name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        lifecycle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLifecycle != null) {
            lifecycleRepository.delete(insertedLifecycle);
            insertedLifecycle = null;
        }
    }

    @Test
    @Transactional
    void createLifecycle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lifecycle
        var returnedLifecycle = om.readValue(
            restLifecycleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lifecycle)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Lifecycle.class
        );

        // Validate the Lifecycle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLifecycleUpdatableFieldsEquals(returnedLifecycle, getPersistedLifecycle(returnedLifecycle));

        insertedLifecycle = returnedLifecycle;
    }

    @Test
    @Transactional
    void createLifecycleWithExistingId() throws Exception {
        // Create the Lifecycle with an existing ID
        lifecycle.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLifecycleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lifecycle)))
            .andExpect(status().isBadRequest());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLifecycles() throws Exception {
        // Initialize the database
        insertedLifecycle = lifecycleRepository.saveAndFlush(lifecycle);

        // Get all the lifecycleList
        restLifecycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lifecycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getLifecycle() throws Exception {
        // Initialize the database
        insertedLifecycle = lifecycleRepository.saveAndFlush(lifecycle);

        // Get the lifecycle
        restLifecycleMockMvc
            .perform(get(ENTITY_API_URL_ID, lifecycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lifecycle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLifecycle() throws Exception {
        // Get the lifecycle
        restLifecycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLifecycle() throws Exception {
        // Initialize the database
        insertedLifecycle = lifecycleRepository.saveAndFlush(lifecycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lifecycle
        Lifecycle updatedLifecycle = lifecycleRepository.findById(lifecycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLifecycle are not directly saved in db
        em.detach(updatedLifecycle);
        updatedLifecycle.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restLifecycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLifecycle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLifecycle))
            )
            .andExpect(status().isOk());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLifecycleToMatchAllProperties(updatedLifecycle);
    }

    @Test
    @Transactional
    void putNonExistingLifecycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lifecycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLifecycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lifecycle.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lifecycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLifecycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lifecycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLifecycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lifecycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLifecycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lifecycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLifecycleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lifecycle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLifecycleWithPatch() throws Exception {
        // Initialize the database
        insertedLifecycle = lifecycleRepository.saveAndFlush(lifecycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lifecycle using partial update
        Lifecycle partialUpdatedLifecycle = new Lifecycle();
        partialUpdatedLifecycle.setId(lifecycle.getId());

        partialUpdatedLifecycle.modifiedDate(UPDATED_MODIFIED_DATE);

        restLifecycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLifecycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLifecycle))
            )
            .andExpect(status().isOk());

        // Validate the Lifecycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLifecycleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLifecycle, lifecycle),
            getPersistedLifecycle(lifecycle)
        );
    }

    @Test
    @Transactional
    void fullUpdateLifecycleWithPatch() throws Exception {
        // Initialize the database
        insertedLifecycle = lifecycleRepository.saveAndFlush(lifecycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lifecycle using partial update
        Lifecycle partialUpdatedLifecycle = new Lifecycle();
        partialUpdatedLifecycle.setId(lifecycle.getId());

        partialUpdatedLifecycle.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restLifecycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLifecycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLifecycle))
            )
            .andExpect(status().isOk());

        // Validate the Lifecycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLifecycleUpdatableFieldsEquals(partialUpdatedLifecycle, getPersistedLifecycle(partialUpdatedLifecycle));
    }

    @Test
    @Transactional
    void patchNonExistingLifecycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lifecycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLifecycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lifecycle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lifecycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLifecycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lifecycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLifecycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lifecycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLifecycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lifecycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLifecycleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lifecycle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lifecycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLifecycle() throws Exception {
        // Initialize the database
        insertedLifecycle = lifecycleRepository.saveAndFlush(lifecycle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lifecycle
        restLifecycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, lifecycle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lifecycleRepository.count();
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

    protected Lifecycle getPersistedLifecycle(Lifecycle lifecycle) {
        return lifecycleRepository.findById(lifecycle.getId()).orElseThrow();
    }

    protected void assertPersistedLifecycleToMatchAllProperties(Lifecycle expectedLifecycle) {
        assertLifecycleAllPropertiesEquals(expectedLifecycle, getPersistedLifecycle(expectedLifecycle));
    }

    protected void assertPersistedLifecycleToMatchUpdatableProperties(Lifecycle expectedLifecycle) {
        assertLifecycleAllUpdatablePropertiesEquals(expectedLifecycle, getPersistedLifecycle(expectedLifecycle));
    }
}
