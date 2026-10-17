package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.StateTransitionsAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.StateTransitions;
import com.spartified.connectrateplan.repository.StateTransitionsRepository;
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
 * Integration tests for the {@link StateTransitionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateTransitionsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FROM_STATE = "AAAAAAAAAA";
    private static final String UPDATED_FROM_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_TO_STATE = "AAAAAAAAAA";
    private static final String UPDATED_TO_STATE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ALLOWED = false;
    private static final Boolean UPDATED_IS_ALLOWED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/state-transitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StateTransitionsRepository stateTransitionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateTransitionsMockMvc;

    private StateTransitions stateTransitions;

    private StateTransitions insertedStateTransitions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateTransitions createEntity() {
        return new StateTransitions()
            .name(DEFAULT_NAME)
            .fromState(DEFAULT_FROM_STATE)
            .toState(DEFAULT_TO_STATE)
            .isAllowed(DEFAULT_IS_ALLOWED)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateTransitions createUpdatedEntity() {
        return new StateTransitions()
            .name(UPDATED_NAME)
            .fromState(UPDATED_FROM_STATE)
            .toState(UPDATED_TO_STATE)
            .isAllowed(UPDATED_IS_ALLOWED)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        stateTransitions = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStateTransitions != null) {
            stateTransitionsRepository.delete(insertedStateTransitions);
            insertedStateTransitions = null;
        }
    }

    @Test
    @Transactional
    void createStateTransitions() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StateTransitions
        var returnedStateTransitions = om.readValue(
            restStateTransitionsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateTransitions)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StateTransitions.class
        );

        // Validate the StateTransitions in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStateTransitionsUpdatableFieldsEquals(returnedStateTransitions, getPersistedStateTransitions(returnedStateTransitions));

        insertedStateTransitions = returnedStateTransitions;
    }

    @Test
    @Transactional
    void createStateTransitionsWithExistingId() throws Exception {
        // Create the StateTransitions with an existing ID
        stateTransitions.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateTransitionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateTransitions)))
            .andExpect(status().isBadRequest());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStateTransitionses() throws Exception {
        // Initialize the database
        insertedStateTransitions = stateTransitionsRepository.saveAndFlush(stateTransitions);

        // Get all the stateTransitionsList
        restStateTransitionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateTransitions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromState").value(hasItem(DEFAULT_FROM_STATE)))
            .andExpect(jsonPath("$.[*].toState").value(hasItem(DEFAULT_TO_STATE)))
            .andExpect(jsonPath("$.[*].isAllowed").value(hasItem(DEFAULT_IS_ALLOWED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getStateTransitions() throws Exception {
        // Initialize the database
        insertedStateTransitions = stateTransitionsRepository.saveAndFlush(stateTransitions);

        // Get the stateTransitions
        restStateTransitionsMockMvc
            .perform(get(ENTITY_API_URL_ID, stateTransitions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateTransitions.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fromState").value(DEFAULT_FROM_STATE))
            .andExpect(jsonPath("$.toState").value(DEFAULT_TO_STATE))
            .andExpect(jsonPath("$.isAllowed").value(DEFAULT_IS_ALLOWED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStateTransitions() throws Exception {
        // Get the stateTransitions
        restStateTransitionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateTransitions() throws Exception {
        // Initialize the database
        insertedStateTransitions = stateTransitionsRepository.saveAndFlush(stateTransitions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateTransitions
        StateTransitions updatedStateTransitions = stateTransitionsRepository.findById(stateTransitions.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStateTransitions are not directly saved in db
        em.detach(updatedStateTransitions);
        updatedStateTransitions
            .name(UPDATED_NAME)
            .fromState(UPDATED_FROM_STATE)
            .toState(UPDATED_TO_STATE)
            .isAllowed(UPDATED_IS_ALLOWED)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restStateTransitionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStateTransitions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStateTransitions))
            )
            .andExpect(status().isOk());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStateTransitionsToMatchAllProperties(updatedStateTransitions);
    }

    @Test
    @Transactional
    void putNonExistingStateTransitions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateTransitions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateTransitionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateTransitions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateTransitions))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateTransitions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateTransitions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateTransitionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stateTransitions))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateTransitions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateTransitions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateTransitionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stateTransitions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStateTransitionsWithPatch() throws Exception {
        // Initialize the database
        insertedStateTransitions = stateTransitionsRepository.saveAndFlush(stateTransitions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateTransitions using partial update
        StateTransitions partialUpdatedStateTransitions = new StateTransitions();
        partialUpdatedStateTransitions.setId(stateTransitions.getId());

        partialUpdatedStateTransitions.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        restStateTransitionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateTransitions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateTransitions))
            )
            .andExpect(status().isOk());

        // Validate the StateTransitions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateTransitionsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStateTransitions, stateTransitions),
            getPersistedStateTransitions(stateTransitions)
        );
    }

    @Test
    @Transactional
    void fullUpdateStateTransitionsWithPatch() throws Exception {
        // Initialize the database
        insertedStateTransitions = stateTransitionsRepository.saveAndFlush(stateTransitions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stateTransitions using partial update
        StateTransitions partialUpdatedStateTransitions = new StateTransitions();
        partialUpdatedStateTransitions.setId(stateTransitions.getId());

        partialUpdatedStateTransitions
            .name(UPDATED_NAME)
            .fromState(UPDATED_FROM_STATE)
            .toState(UPDATED_TO_STATE)
            .isAllowed(UPDATED_IS_ALLOWED)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restStateTransitionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateTransitions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStateTransitions))
            )
            .andExpect(status().isOk());

        // Validate the StateTransitions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStateTransitionsUpdatableFieldsEquals(
            partialUpdatedStateTransitions,
            getPersistedStateTransitions(partialUpdatedStateTransitions)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStateTransitions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateTransitions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateTransitionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateTransitions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateTransitions))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateTransitions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateTransitions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateTransitionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stateTransitions))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateTransitions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stateTransitions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateTransitionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stateTransitions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateTransitions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStateTransitions() throws Exception {
        // Initialize the database
        insertedStateTransitions = stateTransitionsRepository.saveAndFlush(stateTransitions);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stateTransitions
        restStateTransitionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateTransitions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stateTransitionsRepository.count();
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

    protected StateTransitions getPersistedStateTransitions(StateTransitions stateTransitions) {
        return stateTransitionsRepository.findById(stateTransitions.getId()).orElseThrow();
    }

    protected void assertPersistedStateTransitionsToMatchAllProperties(StateTransitions expectedStateTransitions) {
        assertStateTransitionsAllPropertiesEquals(expectedStateTransitions, getPersistedStateTransitions(expectedStateTransitions));
    }

    protected void assertPersistedStateTransitionsToMatchUpdatableProperties(StateTransitions expectedStateTransitions) {
        assertStateTransitionsAllUpdatablePropertiesEquals(
            expectedStateTransitions,
            getPersistedStateTransitions(expectedStateTransitions)
        );
    }
}
