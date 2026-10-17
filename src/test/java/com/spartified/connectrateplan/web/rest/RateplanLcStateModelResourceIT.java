package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.RateplanLcStateModelAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.RateplanLcStateModel;
import com.spartified.connectrateplan.repository.RateplanLcStateModelRepository;
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
 * Integration tests for the {@link RateplanLcStateModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RateplanLcStateModelResourceIT {

    private static final String DEFAULT_RATEPLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RATEPLAN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_LC_STATE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_LC_STATE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/rateplan-lc-state-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RateplanLcStateModelRepository rateplanLcStateModelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRateplanLcStateModelMockMvc;

    private RateplanLcStateModel rateplanLcStateModel;

    private RateplanLcStateModel insertedRateplanLcStateModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RateplanLcStateModel createEntity() {
        return new RateplanLcStateModel()
            .rateplanName(DEFAULT_RATEPLAN_NAME)
            .tenantId(DEFAULT_TENANT_ID)
            .currentLcState(DEFAULT_CURRENT_LC_STATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RateplanLcStateModel createUpdatedEntity() {
        return new RateplanLcStateModel()
            .rateplanName(UPDATED_RATEPLAN_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .currentLcState(UPDATED_CURRENT_LC_STATE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        rateplanLcStateModel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRateplanLcStateModel != null) {
            rateplanLcStateModelRepository.delete(insertedRateplanLcStateModel);
            insertedRateplanLcStateModel = null;
        }
    }

    @Test
    @Transactional
    void createRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RateplanLcStateModel
        var returnedRateplanLcStateModel = om.readValue(
            restRateplanLcStateModelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplanLcStateModel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RateplanLcStateModel.class
        );

        // Validate the RateplanLcStateModel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRateplanLcStateModelUpdatableFieldsEquals(
            returnedRateplanLcStateModel,
            getPersistedRateplanLcStateModel(returnedRateplanLcStateModel)
        );

        insertedRateplanLcStateModel = returnedRateplanLcStateModel;
    }

    @Test
    @Transactional
    void createRateplanLcStateModelWithExistingId() throws Exception {
        // Create the RateplanLcStateModel with an existing ID
        rateplanLcStateModel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRateplanLcStateModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplanLcStateModel)))
            .andExpect(status().isBadRequest());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRateplanLcStateModels() throws Exception {
        // Initialize the database
        insertedRateplanLcStateModel = rateplanLcStateModelRepository.saveAndFlush(rateplanLcStateModel);

        // Get all the rateplanLcStateModelList
        restRateplanLcStateModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rateplanLcStateModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].rateplanName").value(hasItem(DEFAULT_RATEPLAN_NAME)))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].currentLcState").value(hasItem(DEFAULT_CURRENT_LC_STATE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRateplanLcStateModel() throws Exception {
        // Initialize the database
        insertedRateplanLcStateModel = rateplanLcStateModelRepository.saveAndFlush(rateplanLcStateModel);

        // Get the rateplanLcStateModel
        restRateplanLcStateModelMockMvc
            .perform(get(ENTITY_API_URL_ID, rateplanLcStateModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rateplanLcStateModel.getId().intValue()))
            .andExpect(jsonPath("$.rateplanName").value(DEFAULT_RATEPLAN_NAME))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.currentLcState").value(DEFAULT_CURRENT_LC_STATE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRateplanLcStateModel() throws Exception {
        // Get the rateplanLcStateModel
        restRateplanLcStateModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRateplanLcStateModel() throws Exception {
        // Initialize the database
        insertedRateplanLcStateModel = rateplanLcStateModelRepository.saveAndFlush(rateplanLcStateModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateplanLcStateModel
        RateplanLcStateModel updatedRateplanLcStateModel = rateplanLcStateModelRepository
            .findById(rateplanLcStateModel.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedRateplanLcStateModel are not directly saved in db
        em.detach(updatedRateplanLcStateModel);
        updatedRateplanLcStateModel
            .rateplanName(UPDATED_RATEPLAN_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .currentLcState(UPDATED_CURRENT_LC_STATE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRateplanLcStateModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRateplanLcStateModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRateplanLcStateModel))
            )
            .andExpect(status().isOk());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRateplanLcStateModelToMatchAllProperties(updatedRateplanLcStateModel);
    }

    @Test
    @Transactional
    void putNonExistingRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplanLcStateModel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateplanLcStateModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rateplanLcStateModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rateplanLcStateModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplanLcStateModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanLcStateModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rateplanLcStateModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplanLcStateModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanLcStateModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rateplanLcStateModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRateplanLcStateModelWithPatch() throws Exception {
        // Initialize the database
        insertedRateplanLcStateModel = rateplanLcStateModelRepository.saveAndFlush(rateplanLcStateModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateplanLcStateModel using partial update
        RateplanLcStateModel partialUpdatedRateplanLcStateModel = new RateplanLcStateModel();
        partialUpdatedRateplanLcStateModel.setId(rateplanLcStateModel.getId());

        partialUpdatedRateplanLcStateModel.currentLcState(UPDATED_CURRENT_LC_STATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restRateplanLcStateModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRateplanLcStateModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRateplanLcStateModel))
            )
            .andExpect(status().isOk());

        // Validate the RateplanLcStateModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRateplanLcStateModelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRateplanLcStateModel, rateplanLcStateModel),
            getPersistedRateplanLcStateModel(rateplanLcStateModel)
        );
    }

    @Test
    @Transactional
    void fullUpdateRateplanLcStateModelWithPatch() throws Exception {
        // Initialize the database
        insertedRateplanLcStateModel = rateplanLcStateModelRepository.saveAndFlush(rateplanLcStateModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rateplanLcStateModel using partial update
        RateplanLcStateModel partialUpdatedRateplanLcStateModel = new RateplanLcStateModel();
        partialUpdatedRateplanLcStateModel.setId(rateplanLcStateModel.getId());

        partialUpdatedRateplanLcStateModel
            .rateplanName(UPDATED_RATEPLAN_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .currentLcState(UPDATED_CURRENT_LC_STATE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restRateplanLcStateModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRateplanLcStateModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRateplanLcStateModel))
            )
            .andExpect(status().isOk());

        // Validate the RateplanLcStateModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRateplanLcStateModelUpdatableFieldsEquals(
            partialUpdatedRateplanLcStateModel,
            getPersistedRateplanLcStateModel(partialUpdatedRateplanLcStateModel)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplanLcStateModel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateplanLcStateModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rateplanLcStateModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rateplanLcStateModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplanLcStateModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanLcStateModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rateplanLcStateModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRateplanLcStateModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rateplanLcStateModel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRateplanLcStateModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rateplanLcStateModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RateplanLcStateModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRateplanLcStateModel() throws Exception {
        // Initialize the database
        insertedRateplanLcStateModel = rateplanLcStateModelRepository.saveAndFlush(rateplanLcStateModel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rateplanLcStateModel
        restRateplanLcStateModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, rateplanLcStateModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rateplanLcStateModelRepository.count();
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

    protected RateplanLcStateModel getPersistedRateplanLcStateModel(RateplanLcStateModel rateplanLcStateModel) {
        return rateplanLcStateModelRepository.findById(rateplanLcStateModel.getId()).orElseThrow();
    }

    protected void assertPersistedRateplanLcStateModelToMatchAllProperties(RateplanLcStateModel expectedRateplanLcStateModel) {
        assertRateplanLcStateModelAllPropertiesEquals(
            expectedRateplanLcStateModel,
            getPersistedRateplanLcStateModel(expectedRateplanLcStateModel)
        );
    }

    protected void assertPersistedRateplanLcStateModelToMatchUpdatableProperties(RateplanLcStateModel expectedRateplanLcStateModel) {
        assertRateplanLcStateModelAllUpdatablePropertiesEquals(
            expectedRateplanLcStateModel,
            getPersistedRateplanLcStateModel(expectedRateplanLcStateModel)
        );
    }
}
