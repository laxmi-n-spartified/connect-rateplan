package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.StatesAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.States;
import com.spartified.connectrateplan.repository.StatesRepository;
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
 * Integration tests for the {@link StatesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatesResourceIT {

    private static final String DEFAULT_LC_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LC_STATE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatesRepository statesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatesMockMvc;

    private States states;

    private States insertedStates;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static States createEntity() {
        return new States().lcStateName(DEFAULT_LC_STATE_NAME).createdDate(DEFAULT_CREATED_DATE).modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static States createUpdatedEntity() {
        return new States().lcStateName(UPDATED_LC_STATE_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        states = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStates != null) {
            statesRepository.delete(insertedStates);
            insertedStates = null;
        }
    }

    @Test
    @Transactional
    void createStates() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the States
        var returnedStates = om.readValue(
            restStatesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(states)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            States.class
        );

        // Validate the States in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStatesUpdatableFieldsEquals(returnedStates, getPersistedStates(returnedStates));

        insertedStates = returnedStates;
    }

    @Test
    @Transactional
    void createStatesWithExistingId() throws Exception {
        // Create the States with an existing ID
        states.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(states)))
            .andExpect(status().isBadRequest());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStateses() throws Exception {
        // Initialize the database
        insertedStates = statesRepository.saveAndFlush(states);

        // Get all the statesList
        restStatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(states.getId().intValue())))
            .andExpect(jsonPath("$.[*].lcStateName").value(hasItem(DEFAULT_LC_STATE_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStates() throws Exception {
        // Initialize the database
        insertedStates = statesRepository.saveAndFlush(states);

        // Get the states
        restStatesMockMvc
            .perform(get(ENTITY_API_URL_ID, states.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(states.getId().intValue()))
            .andExpect(jsonPath("$.lcStateName").value(DEFAULT_LC_STATE_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStates() throws Exception {
        // Get the states
        restStatesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStates() throws Exception {
        // Initialize the database
        insertedStates = statesRepository.saveAndFlush(states);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the states
        States updatedStates = statesRepository.findById(states.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStates are not directly saved in db
        em.detach(updatedStates);
        updatedStates.lcStateName(UPDATED_LC_STATE_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restStatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStates.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStates))
            )
            .andExpect(status().isOk());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatesToMatchAllProperties(updatedStates);
    }

    @Test
    @Transactional
    void putNonExistingStates() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        states.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatesMockMvc
            .perform(put(ENTITY_API_URL_ID, states.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(states)))
            .andExpect(status().isBadRequest());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStates() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        states.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(states))
            )
            .andExpect(status().isBadRequest());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStates() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        states.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(states)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatesWithPatch() throws Exception {
        // Initialize the database
        insertedStates = statesRepository.saveAndFlush(states);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the states using partial update
        States partialUpdatedStates = new States();
        partialUpdatedStates.setId(states.getId());

        partialUpdatedStates.createdDate(UPDATED_CREATED_DATE);

        restStatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStates.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStates))
            )
            .andExpect(status().isOk());

        // Validate the States in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStates, states), getPersistedStates(states));
    }

    @Test
    @Transactional
    void fullUpdateStatesWithPatch() throws Exception {
        // Initialize the database
        insertedStates = statesRepository.saveAndFlush(states);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the states using partial update
        States partialUpdatedStates = new States();
        partialUpdatedStates.setId(states.getId());

        partialUpdatedStates.lcStateName(UPDATED_LC_STATE_NAME).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restStatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStates.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStates))
            )
            .andExpect(status().isOk());

        // Validate the States in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatesUpdatableFieldsEquals(partialUpdatedStates, getPersistedStates(partialUpdatedStates));
    }

    @Test
    @Transactional
    void patchNonExistingStates() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        states.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, states.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(states))
            )
            .andExpect(status().isBadRequest());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStates() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        states.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(states))
            )
            .andExpect(status().isBadRequest());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStates() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        states.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(states)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the States in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStates() throws Exception {
        // Initialize the database
        insertedStates = statesRepository.saveAndFlush(states);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the states
        restStatesMockMvc
            .perform(delete(ENTITY_API_URL_ID, states.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statesRepository.count();
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

    protected States getPersistedStates(States states) {
        return statesRepository.findById(states.getId()).orElseThrow();
    }

    protected void assertPersistedStatesToMatchAllProperties(States expectedStates) {
        assertStatesAllPropertiesEquals(expectedStates, getPersistedStates(expectedStates));
    }

    protected void assertPersistedStatesToMatchUpdatableProperties(States expectedStates) {
        assertStatesAllUpdatablePropertiesEquals(expectedStates, getPersistedStates(expectedStates));
    }
}
