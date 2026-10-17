package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.TimeIntervalAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.TimeInterval;
import com.spartified.connectrateplan.repository.TimeIntervalRepository;
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
 * Integration tests for the {@link TimeIntervalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeIntervalResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.ofEpochMilli(1792198539903L);

    private static final Boolean DEFAULT_IS_ASSIGNED_RATEPLAN = false;
    private static final Boolean UPDATED_IS_ASSIGNED_RATEPLAN = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/time-intervals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimeIntervalRepository timeIntervalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeIntervalMockMvc;

    private TimeInterval timeInterval;

    private TimeInterval insertedTimeInterval;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeInterval createEntity() {
        return new TimeInterval()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .isAssignedRateplan(DEFAULT_IS_ASSIGNED_RATEPLAN)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeInterval createUpdatedEntity() {
        return new TimeInterval()
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .isAssignedRateplan(UPDATED_IS_ASSIGNED_RATEPLAN)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        timeInterval = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTimeInterval != null) {
            timeIntervalRepository.delete(insertedTimeInterval);
            insertedTimeInterval = null;
        }
    }

    @Test
    @Transactional
    void createTimeInterval() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimeInterval
        var returnedTimeInterval = om.readValue(
            restTimeIntervalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeInterval)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimeInterval.class
        );

        // Validate the TimeInterval in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTimeIntervalUpdatableFieldsEquals(returnedTimeInterval, getPersistedTimeInterval(returnedTimeInterval));

        insertedTimeInterval = returnedTimeInterval;
    }

    @Test
    @Transactional
    void createTimeIntervalWithExistingId() throws Exception {
        // Create the TimeInterval with an existing ID
        timeInterval.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeIntervalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeInterval)))
            .andExpect(status().isBadRequest());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTimeIntervals() throws Exception {
        // Initialize the database
        insertedTimeInterval = timeIntervalRepository.saveAndFlush(timeInterval);

        // Get all the timeIntervalList
        restTimeIntervalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeInterval.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].isAssignedRateplan").value(hasItem(DEFAULT_IS_ASSIGNED_RATEPLAN)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTimeInterval() throws Exception {
        // Initialize the database
        insertedTimeInterval = timeIntervalRepository.saveAndFlush(timeInterval);

        // Get the timeInterval
        restTimeIntervalMockMvc
            .perform(get(ENTITY_API_URL_ID, timeInterval.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeInterval.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.isAssignedRateplan").value(DEFAULT_IS_ASSIGNED_RATEPLAN))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimeInterval() throws Exception {
        // Get the timeInterval
        restTimeIntervalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeInterval() throws Exception {
        // Initialize the database
        insertedTimeInterval = timeIntervalRepository.saveAndFlush(timeInterval);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeInterval
        TimeInterval updatedTimeInterval = timeIntervalRepository.findById(timeInterval.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimeInterval are not directly saved in db
        em.detach(updatedTimeInterval);
        updatedTimeInterval
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .isAssignedRateplan(UPDATED_IS_ASSIGNED_RATEPLAN)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTimeIntervalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimeInterval.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTimeInterval))
            )
            .andExpect(status().isOk());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimeIntervalToMatchAllProperties(updatedTimeInterval);
    }

    @Test
    @Transactional
    void putNonExistingTimeInterval() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeInterval.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeIntervalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeInterval.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeInterval))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeInterval() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeInterval.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeIntervalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeInterval))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeInterval() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeInterval.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeIntervalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeInterval)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeIntervalWithPatch() throws Exception {
        // Initialize the database
        insertedTimeInterval = timeIntervalRepository.saveAndFlush(timeInterval);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeInterval using partial update
        TimeInterval partialUpdatedTimeInterval = new TimeInterval();
        partialUpdatedTimeInterval.setId(timeInterval.getId());

        partialUpdatedTimeInterval.tenantId(UPDATED_TENANT_ID).createdDate(UPDATED_CREATED_DATE);

        restTimeIntervalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeInterval.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeInterval))
            )
            .andExpect(status().isOk());

        // Validate the TimeInterval in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeIntervalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimeInterval, timeInterval),
            getPersistedTimeInterval(timeInterval)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimeIntervalWithPatch() throws Exception {
        // Initialize the database
        insertedTimeInterval = timeIntervalRepository.saveAndFlush(timeInterval);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeInterval using partial update
        TimeInterval partialUpdatedTimeInterval = new TimeInterval();
        partialUpdatedTimeInterval.setId(timeInterval.getId());

        partialUpdatedTimeInterval
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .isAssignedRateplan(UPDATED_IS_ASSIGNED_RATEPLAN)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTimeIntervalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeInterval.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeInterval))
            )
            .andExpect(status().isOk());

        // Validate the TimeInterval in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeIntervalUpdatableFieldsEquals(partialUpdatedTimeInterval, getPersistedTimeInterval(partialUpdatedTimeInterval));
    }

    @Test
    @Transactional
    void patchNonExistingTimeInterval() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeInterval.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeIntervalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeInterval.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeInterval))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeInterval() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeInterval.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeIntervalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeInterval))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeInterval() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeInterval.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeIntervalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timeInterval)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeInterval in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeInterval() throws Exception {
        // Initialize the database
        insertedTimeInterval = timeIntervalRepository.saveAndFlush(timeInterval);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timeInterval
        restTimeIntervalMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeInterval.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timeIntervalRepository.count();
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

    protected TimeInterval getPersistedTimeInterval(TimeInterval timeInterval) {
        return timeIntervalRepository.findById(timeInterval.getId()).orElseThrow();
    }

    protected void assertPersistedTimeIntervalToMatchAllProperties(TimeInterval expectedTimeInterval) {
        assertTimeIntervalAllPropertiesEquals(expectedTimeInterval, getPersistedTimeInterval(expectedTimeInterval));
    }

    protected void assertPersistedTimeIntervalToMatchUpdatableProperties(TimeInterval expectedTimeInterval) {
        assertTimeIntervalAllUpdatablePropertiesEquals(expectedTimeInterval, getPersistedTimeInterval(expectedTimeInterval));
    }
}
