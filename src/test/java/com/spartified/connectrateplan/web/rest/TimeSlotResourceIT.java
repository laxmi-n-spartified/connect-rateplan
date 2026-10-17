package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.TimeSlotAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.TimeSlot;
import com.spartified.connectrateplan.repository.TimeSlotRepository;
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
 * Integration tests for the {@link TimeSlotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeSlotResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TIME_SLOT_START = "AAAAAAAAAA";
    private static final String UPDATED_TIME_SLOT_START = "BBBBBBBBBB";

    private static final String DEFAULT_TIME_SLOT_END = "AAAAAAAAAA";
    private static final String UPDATED_TIME_SLOT_END = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/time-slots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeSlotMockMvc;

    private TimeSlot timeSlot;

    private TimeSlot insertedTimeSlot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSlot createEntity() {
        return new TimeSlot()
            .name(DEFAULT_NAME)
            .timeSlotStart(DEFAULT_TIME_SLOT_START)
            .timeSlotEnd(DEFAULT_TIME_SLOT_END)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSlot createUpdatedEntity() {
        return new TimeSlot()
            .name(UPDATED_NAME)
            .timeSlotStart(UPDATED_TIME_SLOT_START)
            .timeSlotEnd(UPDATED_TIME_SLOT_END)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        timeSlot = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTimeSlot != null) {
            timeSlotRepository.delete(insertedTimeSlot);
            insertedTimeSlot = null;
        }
    }

    @Test
    @Transactional
    void createTimeSlot() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimeSlot
        var returnedTimeSlot = om.readValue(
            restTimeSlotMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSlot)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimeSlot.class
        );

        // Validate the TimeSlot in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTimeSlotUpdatableFieldsEquals(returnedTimeSlot, getPersistedTimeSlot(returnedTimeSlot));

        insertedTimeSlot = returnedTimeSlot;
    }

    @Test
    @Transactional
    void createTimeSlotWithExistingId() throws Exception {
        // Create the TimeSlot with an existing ID
        timeSlot.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSlot)))
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTimeSlots() throws Exception {
        // Initialize the database
        insertedTimeSlot = timeSlotRepository.saveAndFlush(timeSlot);

        // Get all the timeSlotList
        restTimeSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].timeSlotStart").value(hasItem(DEFAULT_TIME_SLOT_START)))
            .andExpect(jsonPath("$.[*].timeSlotEnd").value(hasItem(DEFAULT_TIME_SLOT_END)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTimeSlot() throws Exception {
        // Initialize the database
        insertedTimeSlot = timeSlotRepository.saveAndFlush(timeSlot);

        // Get the timeSlot
        restTimeSlotMockMvc
            .perform(get(ENTITY_API_URL_ID, timeSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeSlot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.timeSlotStart").value(DEFAULT_TIME_SLOT_START))
            .andExpect(jsonPath("$.timeSlotEnd").value(DEFAULT_TIME_SLOT_END))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimeSlot() throws Exception {
        // Get the timeSlot
        restTimeSlotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeSlot() throws Exception {
        // Initialize the database
        insertedTimeSlot = timeSlotRepository.saveAndFlush(timeSlot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSlot
        TimeSlot updatedTimeSlot = timeSlotRepository.findById(timeSlot.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimeSlot are not directly saved in db
        em.detach(updatedTimeSlot);
        updatedTimeSlot
            .name(UPDATED_NAME)
            .timeSlotStart(UPDATED_TIME_SLOT_START)
            .timeSlotEnd(UPDATED_TIME_SLOT_END)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTimeSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimeSlot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTimeSlot))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimeSlotToMatchAllProperties(updatedTimeSlot);
    }

    @Test
    @Transactional
    void putNonExistingTimeSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSlot.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSlot.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSlot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSlot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSlot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeSlotWithPatch() throws Exception {
        // Initialize the database
        insertedTimeSlot = timeSlotRepository.saveAndFlush(timeSlot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSlot using partial update
        TimeSlot partialUpdatedTimeSlot = new TimeSlot();
        partialUpdatedTimeSlot.setId(timeSlot.getId());

        partialUpdatedTimeSlot.name(UPDATED_NAME);

        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeSlot))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeSlotUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTimeSlot, timeSlot), getPersistedTimeSlot(timeSlot));
    }

    @Test
    @Transactional
    void fullUpdateTimeSlotWithPatch() throws Exception {
        // Initialize the database
        insertedTimeSlot = timeSlotRepository.saveAndFlush(timeSlot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSlot using partial update
        TimeSlot partialUpdatedTimeSlot = new TimeSlot();
        partialUpdatedTimeSlot.setId(timeSlot.getId());

        partialUpdatedTimeSlot
            .name(UPDATED_NAME)
            .timeSlotStart(UPDATED_TIME_SLOT_START)
            .timeSlotEnd(UPDATED_TIME_SLOT_END)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeSlot))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeSlotUpdatableFieldsEquals(partialUpdatedTimeSlot, getPersistedTimeSlot(partialUpdatedTimeSlot));
    }

    @Test
    @Transactional
    void patchNonExistingTimeSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSlot.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSlot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeSlot))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSlot.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timeSlot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSlot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeSlot() throws Exception {
        // Initialize the database
        insertedTimeSlot = timeSlotRepository.saveAndFlush(timeSlot);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timeSlot
        restTimeSlotMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeSlot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timeSlotRepository.count();
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

    protected TimeSlot getPersistedTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.findById(timeSlot.getId()).orElseThrow();
    }

    protected void assertPersistedTimeSlotToMatchAllProperties(TimeSlot expectedTimeSlot) {
        assertTimeSlotAllPropertiesEquals(expectedTimeSlot, getPersistedTimeSlot(expectedTimeSlot));
    }

    protected void assertPersistedTimeSlotToMatchUpdatableProperties(TimeSlot expectedTimeSlot) {
        assertTimeSlotAllUpdatablePropertiesEquals(expectedTimeSlot, getPersistedTimeSlot(expectedTimeSlot));
    }
}
