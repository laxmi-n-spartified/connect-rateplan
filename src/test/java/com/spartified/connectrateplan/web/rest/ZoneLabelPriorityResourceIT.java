package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.ZoneLabelPriorityAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.ZoneLabelPriority;
import com.spartified.connectrateplan.repository.ZoneLabelPriorityRepository;
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
 * Integration tests for the {@link ZoneLabelPriorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ZoneLabelPriorityResourceIT {

    private static final String DEFAULT_LABEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LABEL_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/zone-label-priorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ZoneLabelPriorityRepository zoneLabelPriorityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZoneLabelPriorityMockMvc;

    private ZoneLabelPriority zoneLabelPriority;

    private ZoneLabelPriority insertedZoneLabelPriority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZoneLabelPriority createEntity() {
        return new ZoneLabelPriority()
            .labelType(DEFAULT_LABEL_TYPE)
            .priority(DEFAULT_PRIORITY)
            .tenantId(DEFAULT_TENANT_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ZoneLabelPriority createUpdatedEntity() {
        return new ZoneLabelPriority()
            .labelType(UPDATED_LABEL_TYPE)
            .priority(UPDATED_PRIORITY)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        zoneLabelPriority = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedZoneLabelPriority != null) {
            zoneLabelPriorityRepository.delete(insertedZoneLabelPriority);
            insertedZoneLabelPriority = null;
        }
    }

    @Test
    @Transactional
    void createZoneLabelPriority() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ZoneLabelPriority
        var returnedZoneLabelPriority = om.readValue(
            restZoneLabelPriorityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabelPriority)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ZoneLabelPriority.class
        );

        // Validate the ZoneLabelPriority in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertZoneLabelPriorityUpdatableFieldsEquals(returnedZoneLabelPriority, getPersistedZoneLabelPriority(returnedZoneLabelPriority));

        insertedZoneLabelPriority = returnedZoneLabelPriority;
    }

    @Test
    @Transactional
    void createZoneLabelPriorityWithExistingId() throws Exception {
        // Create the ZoneLabelPriority with an existing ID
        zoneLabelPriority.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneLabelPriorityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabelPriority)))
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllZoneLabelPriorities() throws Exception {
        // Initialize the database
        insertedZoneLabelPriority = zoneLabelPriorityRepository.saveAndFlush(zoneLabelPriority);

        // Get all the zoneLabelPriorityList
        restZoneLabelPriorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zoneLabelPriority.getId().intValue())))
            .andExpect(jsonPath("$.[*].labelType").value(hasItem(DEFAULT_LABEL_TYPE)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getZoneLabelPriority() throws Exception {
        // Initialize the database
        insertedZoneLabelPriority = zoneLabelPriorityRepository.saveAndFlush(zoneLabelPriority);

        // Get the zoneLabelPriority
        restZoneLabelPriorityMockMvc
            .perform(get(ENTITY_API_URL_ID, zoneLabelPriority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zoneLabelPriority.getId().intValue()))
            .andExpect(jsonPath("$.labelType").value(DEFAULT_LABEL_TYPE))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingZoneLabelPriority() throws Exception {
        // Get the zoneLabelPriority
        restZoneLabelPriorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingZoneLabelPriority() throws Exception {
        // Initialize the database
        insertedZoneLabelPriority = zoneLabelPriorityRepository.saveAndFlush(zoneLabelPriority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zoneLabelPriority
        ZoneLabelPriority updatedZoneLabelPriority = zoneLabelPriorityRepository.findById(zoneLabelPriority.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedZoneLabelPriority are not directly saved in db
        em.detach(updatedZoneLabelPriority);
        updatedZoneLabelPriority
            .labelType(UPDATED_LABEL_TYPE)
            .priority(UPDATED_PRIORITY)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restZoneLabelPriorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedZoneLabelPriority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedZoneLabelPriority))
            )
            .andExpect(status().isOk());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedZoneLabelPriorityToMatchAllProperties(updatedZoneLabelPriority);
    }

    @Test
    @Transactional
    void putNonExistingZoneLabelPriority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabelPriority.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneLabelPriorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zoneLabelPriority.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(zoneLabelPriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZoneLabelPriority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabelPriority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelPriorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(zoneLabelPriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZoneLabelPriority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabelPriority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelPriorityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zoneLabelPriority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZoneLabelPriorityWithPatch() throws Exception {
        // Initialize the database
        insertedZoneLabelPriority = zoneLabelPriorityRepository.saveAndFlush(zoneLabelPriority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zoneLabelPriority using partial update
        ZoneLabelPriority partialUpdatedZoneLabelPriority = new ZoneLabelPriority();
        partialUpdatedZoneLabelPriority.setId(zoneLabelPriority.getId());

        partialUpdatedZoneLabelPriority.labelType(UPDATED_LABEL_TYPE).priority(UPDATED_PRIORITY).createdDate(UPDATED_CREATED_DATE);

        restZoneLabelPriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZoneLabelPriority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedZoneLabelPriority))
            )
            .andExpect(status().isOk());

        // Validate the ZoneLabelPriority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertZoneLabelPriorityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedZoneLabelPriority, zoneLabelPriority),
            getPersistedZoneLabelPriority(zoneLabelPriority)
        );
    }

    @Test
    @Transactional
    void fullUpdateZoneLabelPriorityWithPatch() throws Exception {
        // Initialize the database
        insertedZoneLabelPriority = zoneLabelPriorityRepository.saveAndFlush(zoneLabelPriority);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zoneLabelPriority using partial update
        ZoneLabelPriority partialUpdatedZoneLabelPriority = new ZoneLabelPriority();
        partialUpdatedZoneLabelPriority.setId(zoneLabelPriority.getId());

        partialUpdatedZoneLabelPriority
            .labelType(UPDATED_LABEL_TYPE)
            .priority(UPDATED_PRIORITY)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restZoneLabelPriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZoneLabelPriority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedZoneLabelPriority))
            )
            .andExpect(status().isOk());

        // Validate the ZoneLabelPriority in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertZoneLabelPriorityUpdatableFieldsEquals(
            partialUpdatedZoneLabelPriority,
            getPersistedZoneLabelPriority(partialUpdatedZoneLabelPriority)
        );
    }

    @Test
    @Transactional
    void patchNonExistingZoneLabelPriority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabelPriority.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneLabelPriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zoneLabelPriority.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(zoneLabelPriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZoneLabelPriority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabelPriority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelPriorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(zoneLabelPriority))
            )
            .andExpect(status().isBadRequest());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZoneLabelPriority() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zoneLabelPriority.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneLabelPriorityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(zoneLabelPriority)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ZoneLabelPriority in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZoneLabelPriority() throws Exception {
        // Initialize the database
        insertedZoneLabelPriority = zoneLabelPriorityRepository.saveAndFlush(zoneLabelPriority);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the zoneLabelPriority
        restZoneLabelPriorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, zoneLabelPriority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return zoneLabelPriorityRepository.count();
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

    protected ZoneLabelPriority getPersistedZoneLabelPriority(ZoneLabelPriority zoneLabelPriority) {
        return zoneLabelPriorityRepository.findById(zoneLabelPriority.getId()).orElseThrow();
    }

    protected void assertPersistedZoneLabelPriorityToMatchAllProperties(ZoneLabelPriority expectedZoneLabelPriority) {
        assertZoneLabelPriorityAllPropertiesEquals(expectedZoneLabelPriority, getPersistedZoneLabelPriority(expectedZoneLabelPriority));
    }

    protected void assertPersistedZoneLabelPriorityToMatchUpdatableProperties(ZoneLabelPriority expectedZoneLabelPriority) {
        assertZoneLabelPriorityAllUpdatablePropertiesEquals(
            expectedZoneLabelPriority,
            getPersistedZoneLabelPriority(expectedZoneLabelPriority)
        );
    }
}
