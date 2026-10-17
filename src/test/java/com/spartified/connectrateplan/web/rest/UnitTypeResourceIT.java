package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.UnitTypeAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.UnitType;
import com.spartified.connectrateplan.repository.UnitTypeRepository;
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
 * Integration tests for the {@link UnitTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONVERSION_FACTOR = 1;
    private static final Integer UPDATED_CONVERSION_FACTOR = 2;

    private static final String DEFAULT_LOWEST_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_LOWEST_UNIT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/unit-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UnitTypeRepository unitTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitTypeMockMvc;

    private UnitType unitType;

    private UnitType insertedUnitType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitType createEntity() {
        return new UnitType()
            .name(DEFAULT_NAME)
            .conversionFactor(DEFAULT_CONVERSION_FACTOR)
            .lowestUnit(DEFAULT_LOWEST_UNIT)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitType createUpdatedEntity() {
        return new UnitType()
            .name(UPDATED_NAME)
            .conversionFactor(UPDATED_CONVERSION_FACTOR)
            .lowestUnit(UPDATED_LOWEST_UNIT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        unitType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUnitType != null) {
            unitTypeRepository.delete(insertedUnitType);
            insertedUnitType = null;
        }
    }

    @Test
    @Transactional
    void createUnitType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UnitType
        var returnedUnitType = om.readValue(
            restUnitTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UnitType.class
        );

        // Validate the UnitType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUnitTypeUpdatableFieldsEquals(returnedUnitType, getPersistedUnitType(returnedUnitType));

        insertedUnitType = returnedUnitType;
    }

    @Test
    @Transactional
    void createUnitTypeWithExistingId() throws Exception {
        // Create the UnitType with an existing ID
        unitType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitType)))
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUnitTypes() throws Exception {
        // Initialize the database
        insertedUnitType = unitTypeRepository.saveAndFlush(unitType);

        // Get all the unitTypeList
        restUnitTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].conversionFactor").value(hasItem(DEFAULT_CONVERSION_FACTOR)))
            .andExpect(jsonPath("$.[*].lowestUnit").value(hasItem(DEFAULT_LOWEST_UNIT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUnitType() throws Exception {
        // Initialize the database
        insertedUnitType = unitTypeRepository.saveAndFlush(unitType);

        // Get the unitType
        restUnitTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, unitType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.conversionFactor").value(DEFAULT_CONVERSION_FACTOR))
            .andExpect(jsonPath("$.lowestUnit").value(DEFAULT_LOWEST_UNIT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUnitType() throws Exception {
        // Get the unitType
        restUnitTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnitType() throws Exception {
        // Initialize the database
        insertedUnitType = unitTypeRepository.saveAndFlush(unitType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unitType
        UnitType updatedUnitType = unitTypeRepository.findById(unitType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUnitType are not directly saved in db
        em.detach(updatedUnitType);
        updatedUnitType
            .name(UPDATED_NAME)
            .conversionFactor(UPDATED_CONVERSION_FACTOR)
            .lowestUnit(UPDATED_LOWEST_UNIT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restUnitTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUnitType))
            )
            .andExpect(status().isOk());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUnitTypeToMatchAllProperties(updatedUnitType);
    }

    @Test
    @Transactional
    void putNonExistingUnitType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(unitType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitTypeWithPatch() throws Exception {
        // Initialize the database
        insertedUnitType = unitTypeRepository.saveAndFlush(unitType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unitType using partial update
        UnitType partialUpdatedUnitType = new UnitType();
        partialUpdatedUnitType.setId(unitType.getId());

        partialUpdatedUnitType.name(UPDATED_NAME).modifiedDate(UPDATED_MODIFIED_DATE);

        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnitType))
            )
            .andExpect(status().isOk());

        // Validate the UnitType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnitTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUnitType, unitType), getPersistedUnitType(unitType));
    }

    @Test
    @Transactional
    void fullUpdateUnitTypeWithPatch() throws Exception {
        // Initialize the database
        insertedUnitType = unitTypeRepository.saveAndFlush(unitType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the unitType using partial update
        UnitType partialUpdatedUnitType = new UnitType();
        partialUpdatedUnitType.setId(unitType.getId());

        partialUpdatedUnitType
            .name(UPDATED_NAME)
            .conversionFactor(UPDATED_CONVERSION_FACTOR)
            .lowestUnit(UPDATED_LOWEST_UNIT)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnitType))
            )
            .andExpect(status().isOk());

        // Validate the UnitType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnitTypeUpdatableFieldsEquals(partialUpdatedUnitType, getPersistedUnitType(partialUpdatedUnitType));
    }

    @Test
    @Transactional
    void patchNonExistingUnitType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        unitType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(unitType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitType() throws Exception {
        // Initialize the database
        insertedUnitType = unitTypeRepository.saveAndFlush(unitType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the unitType
        restUnitTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return unitTypeRepository.count();
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

    protected UnitType getPersistedUnitType(UnitType unitType) {
        return unitTypeRepository.findById(unitType.getId()).orElseThrow();
    }

    protected void assertPersistedUnitTypeToMatchAllProperties(UnitType expectedUnitType) {
        assertUnitTypeAllPropertiesEquals(expectedUnitType, getPersistedUnitType(expectedUnitType));
    }

    protected void assertPersistedUnitTypeToMatchUpdatableProperties(UnitType expectedUnitType) {
        assertUnitTypeAllUpdatablePropertiesEquals(expectedUnitType, getPersistedUnitType(expectedUnitType));
    }
}
