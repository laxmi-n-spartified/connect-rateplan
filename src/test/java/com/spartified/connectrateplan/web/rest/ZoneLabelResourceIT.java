package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.ZoneLabelAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.ZoneLabel;
import com.spartified.connectrateplan.repository.ZoneLabelRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ZoneLabelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ZoneLabelResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REST_OF_WORLD = false;
    private static final Boolean UPDATED_IS_REST_OF_WORLD = true;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/zone-labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ZoneLabelRepository zoneLabelRepository;

    @Mock
    private ZoneLabelRepository zoneLabelRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZoneLabelMockMvc;

    private ZoneLabel zoneLabel;

    private ZoneLabel insertedZoneLabel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZoneLabel createEntity() {
        return new ZoneLabel()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .isRestOfWorld(DEFAULT_IS_REST_OF_WORLD)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZoneLabel createUpdatedEntity() {
        return new ZoneLabel()
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .isRestOfWorld(UPDATED_IS_REST_OF_WORLD)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        zoneLabel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedZoneLabel != null) {
            zoneLabelRepository.delete(insertedZoneLabel);
            insertedZoneLabel = null;
        }
    }

    @Test
    @Transactional
    void createZoneLabel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ZoneLabel
        var returnedZoneLabel = om.readValue(
            restZoneLabelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ZoneLabel.class
        );

        // Validate the ZoneLabel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertZoneLabelUpdatableFieldsEquals(returnedZoneLabel, getPersistedZoneLabel(returnedZoneLabel));

        insertedZoneLabel = returnedZoneLabel;
    }

    @Test
    @Transactional
    void createZoneLabelWithExistingId() throws Exception {
        // Create the ZoneLabel with an existing ID
        zoneLabel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneLabelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabel)))
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllZoneLabels() throws Exception {
        // Initialize the database
        insertedZoneLabel = zoneLabelRepository.saveAndFlush(zoneLabel);

        // Get all the zoneLabelList
        restZoneLabelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zoneLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isRestOfWorld").value(hasItem(DEFAULT_IS_REST_OF_WORLD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZoneLabelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(zoneLabelRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restZoneLabelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(zoneLabelRepositoryMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZoneLabelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(zoneLabelRepositoryMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restZoneLabelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(zoneLabelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getZoneLabel() throws Exception {
        // Initialize the database
        insertedZoneLabel = zoneLabelRepository.saveAndFlush(zoneLabel);

        // Get the zoneLabel
        restZoneLabelMockMvc
            .perform(get(ENTITY_API_URL_ID, zoneLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zoneLabel.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isRestOfWorld").value(DEFAULT_IS_REST_OF_WORLD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingZoneLabel() throws Exception {
        // Get the zoneLabel
        restZoneLabelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingZoneLabel() throws Exception {
        // Initialize the database
        insertedZoneLabel = zoneLabelRepository.saveAndFlush(zoneLabel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zoneLabel
        ZoneLabel updatedZoneLabel = zoneLabelRepository.findById(zoneLabel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedZoneLabel are not directly saved in db
        em.detach(updatedZoneLabel);
        updatedZoneLabel
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .isRestOfWorld(UPDATED_IS_REST_OF_WORLD)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restZoneLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedZoneLabel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedZoneLabel))
            )
            .andExpect(status().isOk());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedZoneLabelToMatchAllProperties(updatedZoneLabel);
    }

    @Test
    @Transactional
    void putNonExistingZoneLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zoneLabel.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZoneLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(zoneLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZoneLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZoneLabelWithPatch() throws Exception {
        // Initialize the database
        insertedZoneLabel = zoneLabelRepository.saveAndFlush(zoneLabel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zoneLabel using partial update
        ZoneLabel partialUpdatedZoneLabel = new ZoneLabel();
        partialUpdatedZoneLabel.setId(zoneLabel.getId());

        partialUpdatedZoneLabel.tenantId(UPDATED_TENANT_ID).createdDate(UPDATED_CREATED_DATE);

        restZoneLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZoneLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedZoneLabel))
            )
            .andExpect(status().isOk());

        // Validate the ZoneLabel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertZoneLabelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedZoneLabel, zoneLabel),
            getPersistedZoneLabel(zoneLabel)
        );
    }

    @Test
    @Transactional
    void fullUpdateZoneLabelWithPatch() throws Exception {
        // Initialize the database
        insertedZoneLabel = zoneLabelRepository.saveAndFlush(zoneLabel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zoneLabel using partial update
        ZoneLabel partialUpdatedZoneLabel = new ZoneLabel();
        partialUpdatedZoneLabel.setId(zoneLabel.getId());

        partialUpdatedZoneLabel
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .isRestOfWorld(UPDATED_IS_REST_OF_WORLD)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restZoneLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZoneLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedZoneLabel))
            )
            .andExpect(status().isOk());

        // Validate the ZoneLabel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertZoneLabelUpdatableFieldsEquals(partialUpdatedZoneLabel, getPersistedZoneLabel(partialUpdatedZoneLabel));
    }

    @Test
    @Transactional
    void patchNonExistingZoneLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zoneLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(zoneLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZoneLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(zoneLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZoneLabel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(zoneLabel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZoneLabel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZoneLabel() throws Exception {
        // Initialize the database
        insertedZoneLabel = zoneLabelRepository.saveAndFlush(zoneLabel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the zoneLabel
        restZoneLabelMockMvc
            .perform(delete(ENTITY_API_URL_ID, zoneLabel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return zoneLabelRepository.count();
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

    protected ZoneLabel getPersistedZoneLabel(ZoneLabel zoneLabel) {
        return zoneLabelRepository.findById(zoneLabel.getId()).orElseThrow();
    }

    protected void assertPersistedZoneLabelToMatchAllProperties(ZoneLabel expectedZoneLabel) {
        assertZoneLabelAllPropertiesEquals(expectedZoneLabel, getPersistedZoneLabel(expectedZoneLabel));
    }

    protected void assertPersistedZoneLabelToMatchUpdatableProperties(ZoneLabel expectedZoneLabel) {
        assertZoneLabelAllUpdatablePropertiesEquals(expectedZoneLabel, getPersistedZoneLabel(expectedZoneLabel));
    }
}
