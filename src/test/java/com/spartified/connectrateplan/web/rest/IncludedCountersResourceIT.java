package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.IncludedCountersAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.IncludedCounters;
import com.spartified.connectrateplan.repository.IncludedCountersRepository;
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
 * Integration tests for the {@link IncludedCountersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncludedCountersResourceIT {

    private static final Long DEFAULT_COUNTER_DATA = 1L;
    private static final Long UPDATED_COUNTER_DATA = 2L;

    private static final Long DEFAULT_COUNTER_SMS_MO = 1L;
    private static final Long UPDATED_COUNTER_SMS_MO = 2L;

    private static final Long DEFAULT_COUNTER_SMS_MT = 1L;
    private static final Long UPDATED_COUNTER_SMS_MT = 2L;

    private static final Long DEFAULT_COUNTER_VOICE_MO = 1L;
    private static final Long UPDATED_COUNTER_VOICE_MO = 2L;

    private static final Long DEFAULT_COUNTER_VOICE_MT = 1L;
    private static final Long UPDATED_COUNTER_VOICE_MT = 2L;

    private static final Boolean DEFAULT_ROLLOVER_DATA = false;
    private static final Boolean UPDATED_ROLLOVER_DATA = true;

    private static final Integer DEFAULT_ROLLOVER_DATA_DURATION = 1;
    private static final Integer UPDATED_ROLLOVER_DATA_DURATION = 2;

    private static final Boolean DEFAULT_ROLLOVER_VOICE_MO = false;
    private static final Boolean UPDATED_ROLLOVER_VOICE_MO = true;

    private static final Integer DEFAULT_ROLLOVER_VOICE_MO_DURATION = 1;
    private static final Integer UPDATED_ROLLOVER_VOICE_MO_DURATION = 2;

    private static final Boolean DEFAULT_ROLLOVER_SMS_MO = false;
    private static final Boolean UPDATED_ROLLOVER_SMS_MO = true;

    private static final Integer DEFAULT_ROLLOVER_SMS_MO_DURATION = 1;
    private static final Integer UPDATED_ROLLOVER_SMS_MO_DURATION = 2;

    private static final String DEFAULT_COUNTER_DATA_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_DATA_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTER_SMS_MO_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_SMS_MO_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTER_SMS_MT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_SMS_MT_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTER_VOICE_MO_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_VOICE_MO_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTER_VOICE_MT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_VOICE_MT_UNIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RENEW_ON_QUOTA_EXHAUST = false;
    private static final Boolean UPDATED_RENEW_ON_QUOTA_EXHAUST = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/included-counters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IncludedCountersRepository includedCountersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncludedCountersMockMvc;

    private IncludedCounters includedCounters;

    private IncludedCounters insertedIncludedCounters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncludedCounters createEntity() {
        return new IncludedCounters()
            .counterData(DEFAULT_COUNTER_DATA)
            .counterSmsMo(DEFAULT_COUNTER_SMS_MO)
            .counterSmsMt(DEFAULT_COUNTER_SMS_MT)
            .counterVoiceMo(DEFAULT_COUNTER_VOICE_MO)
            .counterVoiceMt(DEFAULT_COUNTER_VOICE_MT)
            .rolloverData(DEFAULT_ROLLOVER_DATA)
            .rolloverDataDuration(DEFAULT_ROLLOVER_DATA_DURATION)
            .rolloverVoiceMo(DEFAULT_ROLLOVER_VOICE_MO)
            .rolloverVoiceMoDuration(DEFAULT_ROLLOVER_VOICE_MO_DURATION)
            .rolloverSmsMo(DEFAULT_ROLLOVER_SMS_MO)
            .rolloverSmsMoDuration(DEFAULT_ROLLOVER_SMS_MO_DURATION)
            .counterDataUnit(DEFAULT_COUNTER_DATA_UNIT)
            .counterSmsMoUnit(DEFAULT_COUNTER_SMS_MO_UNIT)
            .counterSmsMtUnit(DEFAULT_COUNTER_SMS_MT_UNIT)
            .counterVoiceMoUnit(DEFAULT_COUNTER_VOICE_MO_UNIT)
            .counterVoiceMtUnit(DEFAULT_COUNTER_VOICE_MT_UNIT)
            .renewOnQuotaExhaust(DEFAULT_RENEW_ON_QUOTA_EXHAUST)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncludedCounters createUpdatedEntity() {
        return new IncludedCounters()
            .counterData(UPDATED_COUNTER_DATA)
            .counterSmsMo(UPDATED_COUNTER_SMS_MO)
            .counterSmsMt(UPDATED_COUNTER_SMS_MT)
            .counterVoiceMo(UPDATED_COUNTER_VOICE_MO)
            .counterVoiceMt(UPDATED_COUNTER_VOICE_MT)
            .rolloverData(UPDATED_ROLLOVER_DATA)
            .rolloverDataDuration(UPDATED_ROLLOVER_DATA_DURATION)
            .rolloverVoiceMo(UPDATED_ROLLOVER_VOICE_MO)
            .rolloverVoiceMoDuration(UPDATED_ROLLOVER_VOICE_MO_DURATION)
            .rolloverSmsMo(UPDATED_ROLLOVER_SMS_MO)
            .rolloverSmsMoDuration(UPDATED_ROLLOVER_SMS_MO_DURATION)
            .counterDataUnit(UPDATED_COUNTER_DATA_UNIT)
            .counterSmsMoUnit(UPDATED_COUNTER_SMS_MO_UNIT)
            .counterSmsMtUnit(UPDATED_COUNTER_SMS_MT_UNIT)
            .counterVoiceMoUnit(UPDATED_COUNTER_VOICE_MO_UNIT)
            .counterVoiceMtUnit(UPDATED_COUNTER_VOICE_MT_UNIT)
            .renewOnQuotaExhaust(UPDATED_RENEW_ON_QUOTA_EXHAUST)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        includedCounters = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIncludedCounters != null) {
            includedCountersRepository.delete(insertedIncludedCounters);
            insertedIncludedCounters = null;
        }
    }

    @Test
    @Transactional
    void createIncludedCounters() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IncludedCounters
        var returnedIncludedCounters = om.readValue(
            restIncludedCountersMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(includedCounters)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IncludedCounters.class
        );

        // Validate the IncludedCounters in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIncludedCountersUpdatableFieldsEquals(returnedIncludedCounters, getPersistedIncludedCounters(returnedIncludedCounters));

        insertedIncludedCounters = returnedIncludedCounters;
    }

    @Test
    @Transactional
    void createIncludedCountersWithExistingId() throws Exception {
        // Create the IncludedCounters with an existing ID
        includedCounters.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncludedCountersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(includedCounters)))
            .andExpect(status().isBadRequest());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIncludedCounterses() throws Exception {
        // Initialize the database
        insertedIncludedCounters = includedCountersRepository.saveAndFlush(includedCounters);

        // Get all the includedCountersList
        restIncludedCountersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(includedCounters.getId().intValue())))
            .andExpect(jsonPath("$.[*].counterData").value(hasItem(DEFAULT_COUNTER_DATA.intValue())))
            .andExpect(jsonPath("$.[*].counterSmsMo").value(hasItem(DEFAULT_COUNTER_SMS_MO.intValue())))
            .andExpect(jsonPath("$.[*].counterSmsMt").value(hasItem(DEFAULT_COUNTER_SMS_MT.intValue())))
            .andExpect(jsonPath("$.[*].counterVoiceMo").value(hasItem(DEFAULT_COUNTER_VOICE_MO.intValue())))
            .andExpect(jsonPath("$.[*].counterVoiceMt").value(hasItem(DEFAULT_COUNTER_VOICE_MT.intValue())))
            .andExpect(jsonPath("$.[*].rolloverData").value(hasItem(DEFAULT_ROLLOVER_DATA)))
            .andExpect(jsonPath("$.[*].rolloverDataDuration").value(hasItem(DEFAULT_ROLLOVER_DATA_DURATION)))
            .andExpect(jsonPath("$.[*].rolloverVoiceMo").value(hasItem(DEFAULT_ROLLOVER_VOICE_MO)))
            .andExpect(jsonPath("$.[*].rolloverVoiceMoDuration").value(hasItem(DEFAULT_ROLLOVER_VOICE_MO_DURATION)))
            .andExpect(jsonPath("$.[*].rolloverSmsMo").value(hasItem(DEFAULT_ROLLOVER_SMS_MO)))
            .andExpect(jsonPath("$.[*].rolloverSmsMoDuration").value(hasItem(DEFAULT_ROLLOVER_SMS_MO_DURATION)))
            .andExpect(jsonPath("$.[*].counterDataUnit").value(hasItem(DEFAULT_COUNTER_DATA_UNIT)))
            .andExpect(jsonPath("$.[*].counterSmsMoUnit").value(hasItem(DEFAULT_COUNTER_SMS_MO_UNIT)))
            .andExpect(jsonPath("$.[*].counterSmsMtUnit").value(hasItem(DEFAULT_COUNTER_SMS_MT_UNIT)))
            .andExpect(jsonPath("$.[*].counterVoiceMoUnit").value(hasItem(DEFAULT_COUNTER_VOICE_MO_UNIT)))
            .andExpect(jsonPath("$.[*].counterVoiceMtUnit").value(hasItem(DEFAULT_COUNTER_VOICE_MT_UNIT)))
            .andExpect(jsonPath("$.[*].renewOnQuotaExhaust").value(hasItem(DEFAULT_RENEW_ON_QUOTA_EXHAUST)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getIncludedCounters() throws Exception {
        // Initialize the database
        insertedIncludedCounters = includedCountersRepository.saveAndFlush(includedCounters);

        // Get the includedCounters
        restIncludedCountersMockMvc
            .perform(get(ENTITY_API_URL_ID, includedCounters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(includedCounters.getId().intValue()))
            .andExpect(jsonPath("$.counterData").value(DEFAULT_COUNTER_DATA.intValue()))
            .andExpect(jsonPath("$.counterSmsMo").value(DEFAULT_COUNTER_SMS_MO.intValue()))
            .andExpect(jsonPath("$.counterSmsMt").value(DEFAULT_COUNTER_SMS_MT.intValue()))
            .andExpect(jsonPath("$.counterVoiceMo").value(DEFAULT_COUNTER_VOICE_MO.intValue()))
            .andExpect(jsonPath("$.counterVoiceMt").value(DEFAULT_COUNTER_VOICE_MT.intValue()))
            .andExpect(jsonPath("$.rolloverData").value(DEFAULT_ROLLOVER_DATA))
            .andExpect(jsonPath("$.rolloverDataDuration").value(DEFAULT_ROLLOVER_DATA_DURATION))
            .andExpect(jsonPath("$.rolloverVoiceMo").value(DEFAULT_ROLLOVER_VOICE_MO))
            .andExpect(jsonPath("$.rolloverVoiceMoDuration").value(DEFAULT_ROLLOVER_VOICE_MO_DURATION))
            .andExpect(jsonPath("$.rolloverSmsMo").value(DEFAULT_ROLLOVER_SMS_MO))
            .andExpect(jsonPath("$.rolloverSmsMoDuration").value(DEFAULT_ROLLOVER_SMS_MO_DURATION))
            .andExpect(jsonPath("$.counterDataUnit").value(DEFAULT_COUNTER_DATA_UNIT))
            .andExpect(jsonPath("$.counterSmsMoUnit").value(DEFAULT_COUNTER_SMS_MO_UNIT))
            .andExpect(jsonPath("$.counterSmsMtUnit").value(DEFAULT_COUNTER_SMS_MT_UNIT))
            .andExpect(jsonPath("$.counterVoiceMoUnit").value(DEFAULT_COUNTER_VOICE_MO_UNIT))
            .andExpect(jsonPath("$.counterVoiceMtUnit").value(DEFAULT_COUNTER_VOICE_MT_UNIT))
            .andExpect(jsonPath("$.renewOnQuotaExhaust").value(DEFAULT_RENEW_ON_QUOTA_EXHAUST))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIncludedCounters() throws Exception {
        // Get the includedCounters
        restIncludedCountersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncludedCounters() throws Exception {
        // Initialize the database
        insertedIncludedCounters = includedCountersRepository.saveAndFlush(includedCounters);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the includedCounters
        IncludedCounters updatedIncludedCounters = includedCountersRepository.findById(includedCounters.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIncludedCounters are not directly saved in db
        em.detach(updatedIncludedCounters);
        updatedIncludedCounters
            .counterData(UPDATED_COUNTER_DATA)
            .counterSmsMo(UPDATED_COUNTER_SMS_MO)
            .counterSmsMt(UPDATED_COUNTER_SMS_MT)
            .counterVoiceMo(UPDATED_COUNTER_VOICE_MO)
            .counterVoiceMt(UPDATED_COUNTER_VOICE_MT)
            .rolloverData(UPDATED_ROLLOVER_DATA)
            .rolloverDataDuration(UPDATED_ROLLOVER_DATA_DURATION)
            .rolloverVoiceMo(UPDATED_ROLLOVER_VOICE_MO)
            .rolloverVoiceMoDuration(UPDATED_ROLLOVER_VOICE_MO_DURATION)
            .rolloverSmsMo(UPDATED_ROLLOVER_SMS_MO)
            .rolloverSmsMoDuration(UPDATED_ROLLOVER_SMS_MO_DURATION)
            .counterDataUnit(UPDATED_COUNTER_DATA_UNIT)
            .counterSmsMoUnit(UPDATED_COUNTER_SMS_MO_UNIT)
            .counterSmsMtUnit(UPDATED_COUNTER_SMS_MT_UNIT)
            .counterVoiceMoUnit(UPDATED_COUNTER_VOICE_MO_UNIT)
            .counterVoiceMtUnit(UPDATED_COUNTER_VOICE_MT_UNIT)
            .renewOnQuotaExhaust(UPDATED_RENEW_ON_QUOTA_EXHAUST)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restIncludedCountersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncludedCounters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIncludedCounters))
            )
            .andExpect(status().isOk());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIncludedCountersToMatchAllProperties(updatedIncludedCounters);
    }

    @Test
    @Transactional
    void putNonExistingIncludedCounters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        includedCounters.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncludedCountersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, includedCounters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(includedCounters))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncludedCounters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        includedCounters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncludedCountersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(includedCounters))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncludedCounters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        includedCounters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncludedCountersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(includedCounters)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncludedCountersWithPatch() throws Exception {
        // Initialize the database
        insertedIncludedCounters = includedCountersRepository.saveAndFlush(includedCounters);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the includedCounters using partial update
        IncludedCounters partialUpdatedIncludedCounters = new IncludedCounters();
        partialUpdatedIncludedCounters.setId(includedCounters.getId());

        partialUpdatedIncludedCounters
            .counterSmsMt(UPDATED_COUNTER_SMS_MT)
            .counterVoiceMo(UPDATED_COUNTER_VOICE_MO)
            .rolloverData(UPDATED_ROLLOVER_DATA)
            .rolloverDataDuration(UPDATED_ROLLOVER_DATA_DURATION)
            .rolloverVoiceMoDuration(UPDATED_ROLLOVER_VOICE_MO_DURATION)
            .counterSmsMoUnit(UPDATED_COUNTER_SMS_MO_UNIT)
            .counterVoiceMoUnit(UPDATED_COUNTER_VOICE_MO_UNIT);

        restIncludedCountersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncludedCounters.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncludedCounters))
            )
            .andExpect(status().isOk());

        // Validate the IncludedCounters in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncludedCountersUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIncludedCounters, includedCounters),
            getPersistedIncludedCounters(includedCounters)
        );
    }

    @Test
    @Transactional
    void fullUpdateIncludedCountersWithPatch() throws Exception {
        // Initialize the database
        insertedIncludedCounters = includedCountersRepository.saveAndFlush(includedCounters);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the includedCounters using partial update
        IncludedCounters partialUpdatedIncludedCounters = new IncludedCounters();
        partialUpdatedIncludedCounters.setId(includedCounters.getId());

        partialUpdatedIncludedCounters
            .counterData(UPDATED_COUNTER_DATA)
            .counterSmsMo(UPDATED_COUNTER_SMS_MO)
            .counterSmsMt(UPDATED_COUNTER_SMS_MT)
            .counterVoiceMo(UPDATED_COUNTER_VOICE_MO)
            .counterVoiceMt(UPDATED_COUNTER_VOICE_MT)
            .rolloverData(UPDATED_ROLLOVER_DATA)
            .rolloverDataDuration(UPDATED_ROLLOVER_DATA_DURATION)
            .rolloverVoiceMo(UPDATED_ROLLOVER_VOICE_MO)
            .rolloverVoiceMoDuration(UPDATED_ROLLOVER_VOICE_MO_DURATION)
            .rolloverSmsMo(UPDATED_ROLLOVER_SMS_MO)
            .rolloverSmsMoDuration(UPDATED_ROLLOVER_SMS_MO_DURATION)
            .counterDataUnit(UPDATED_COUNTER_DATA_UNIT)
            .counterSmsMoUnit(UPDATED_COUNTER_SMS_MO_UNIT)
            .counterSmsMtUnit(UPDATED_COUNTER_SMS_MT_UNIT)
            .counterVoiceMoUnit(UPDATED_COUNTER_VOICE_MO_UNIT)
            .counterVoiceMtUnit(UPDATED_COUNTER_VOICE_MT_UNIT)
            .renewOnQuotaExhaust(UPDATED_RENEW_ON_QUOTA_EXHAUST)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restIncludedCountersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncludedCounters.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIncludedCounters))
            )
            .andExpect(status().isOk());

        // Validate the IncludedCounters in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIncludedCountersUpdatableFieldsEquals(
            partialUpdatedIncludedCounters,
            getPersistedIncludedCounters(partialUpdatedIncludedCounters)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIncludedCounters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        includedCounters.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncludedCountersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, includedCounters.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(includedCounters))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncludedCounters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        includedCounters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncludedCountersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(includedCounters))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncludedCounters() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        includedCounters.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncludedCountersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(includedCounters)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncludedCounters in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncludedCounters() throws Exception {
        // Initialize the database
        insertedIncludedCounters = includedCountersRepository.saveAndFlush(includedCounters);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the includedCounters
        restIncludedCountersMockMvc
            .perform(delete(ENTITY_API_URL_ID, includedCounters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return includedCountersRepository.count();
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

    protected IncludedCounters getPersistedIncludedCounters(IncludedCounters includedCounters) {
        return includedCountersRepository.findById(includedCounters.getId()).orElseThrow();
    }

    protected void assertPersistedIncludedCountersToMatchAllProperties(IncludedCounters expectedIncludedCounters) {
        assertIncludedCountersAllPropertiesEquals(expectedIncludedCounters, getPersistedIncludedCounters(expectedIncludedCounters));
    }

    protected void assertPersistedIncludedCountersToMatchUpdatableProperties(IncludedCounters expectedIncludedCounters) {
        assertIncludedCountersAllUpdatablePropertiesEquals(
            expectedIncludedCounters,
            getPersistedIncludedCounters(expectedIncludedCounters)
        );
    }
}
