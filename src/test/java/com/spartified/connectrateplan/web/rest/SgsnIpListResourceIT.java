package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.SgsnIpListAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.SgsnIpList;
import com.spartified.connectrateplan.repository.SgsnIpListRepository;
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
 * Integration tests for the {@link SgsnIpListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SgsnIpListResourceIT {

    private static final String DEFAULT_SGSN_IP = "AAAAAAAAAA";
    private static final String UPDATED_SGSN_IP = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/sgsn-ip-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SgsnIpListRepository sgsnIpListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSgsnIpListMockMvc;

    private SgsnIpList sgsnIpList;

    private SgsnIpList insertedSgsnIpList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SgsnIpList createEntity() {
        return new SgsnIpList()
            .sgsnIp(DEFAULT_SGSN_IP)
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
    public static SgsnIpList createUpdatedEntity() {
        return new SgsnIpList()
            .sgsnIp(UPDATED_SGSN_IP)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        sgsnIpList = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSgsnIpList != null) {
            sgsnIpListRepository.delete(insertedSgsnIpList);
            insertedSgsnIpList = null;
        }
    }

    @Test
    @Transactional
    void createSgsnIpList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SgsnIpList
        var returnedSgsnIpList = om.readValue(
            restSgsnIpListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sgsnIpList)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SgsnIpList.class
        );

        // Validate the SgsnIpList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSgsnIpListUpdatableFieldsEquals(returnedSgsnIpList, getPersistedSgsnIpList(returnedSgsnIpList));

        insertedSgsnIpList = returnedSgsnIpList;
    }

    @Test
    @Transactional
    void createSgsnIpListWithExistingId() throws Exception {
        // Create the SgsnIpList with an existing ID
        sgsnIpList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSgsnIpListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sgsnIpList)))
            .andExpect(status().isBadRequest());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSgsnIpLists() throws Exception {
        // Initialize the database
        insertedSgsnIpList = sgsnIpListRepository.saveAndFlush(sgsnIpList);

        // Get all the sgsnIpListList
        restSgsnIpListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sgsnIpList.getId().intValue())))
            .andExpect(jsonPath("$.[*].sgsnIp").value(hasItem(DEFAULT_SGSN_IP)))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSgsnIpList() throws Exception {
        // Initialize the database
        insertedSgsnIpList = sgsnIpListRepository.saveAndFlush(sgsnIpList);

        // Get the sgsnIpList
        restSgsnIpListMockMvc
            .perform(get(ENTITY_API_URL_ID, sgsnIpList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sgsnIpList.getId().intValue()))
            .andExpect(jsonPath("$.sgsnIp").value(DEFAULT_SGSN_IP))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSgsnIpList() throws Exception {
        // Get the sgsnIpList
        restSgsnIpListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSgsnIpList() throws Exception {
        // Initialize the database
        insertedSgsnIpList = sgsnIpListRepository.saveAndFlush(sgsnIpList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sgsnIpList
        SgsnIpList updatedSgsnIpList = sgsnIpListRepository.findById(sgsnIpList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSgsnIpList are not directly saved in db
        em.detach(updatedSgsnIpList);
        updatedSgsnIpList
            .sgsnIp(UPDATED_SGSN_IP)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSgsnIpListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSgsnIpList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSgsnIpList))
            )
            .andExpect(status().isOk());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSgsnIpListToMatchAllProperties(updatedSgsnIpList);
    }

    @Test
    @Transactional
    void putNonExistingSgsnIpList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sgsnIpList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSgsnIpListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sgsnIpList.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sgsnIpList))
            )
            .andExpect(status().isBadRequest());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSgsnIpList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sgsnIpList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSgsnIpListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sgsnIpList))
            )
            .andExpect(status().isBadRequest());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSgsnIpList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sgsnIpList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSgsnIpListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sgsnIpList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSgsnIpListWithPatch() throws Exception {
        // Initialize the database
        insertedSgsnIpList = sgsnIpListRepository.saveAndFlush(sgsnIpList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sgsnIpList using partial update
        SgsnIpList partialUpdatedSgsnIpList = new SgsnIpList();
        partialUpdatedSgsnIpList.setId(sgsnIpList.getId());

        restSgsnIpListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSgsnIpList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSgsnIpList))
            )
            .andExpect(status().isOk());

        // Validate the SgsnIpList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSgsnIpListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSgsnIpList, sgsnIpList),
            getPersistedSgsnIpList(sgsnIpList)
        );
    }

    @Test
    @Transactional
    void fullUpdateSgsnIpListWithPatch() throws Exception {
        // Initialize the database
        insertedSgsnIpList = sgsnIpListRepository.saveAndFlush(sgsnIpList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sgsnIpList using partial update
        SgsnIpList partialUpdatedSgsnIpList = new SgsnIpList();
        partialUpdatedSgsnIpList.setId(sgsnIpList.getId());

        partialUpdatedSgsnIpList
            .sgsnIp(UPDATED_SGSN_IP)
            .tenantId(UPDATED_TENANT_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSgsnIpListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSgsnIpList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSgsnIpList))
            )
            .andExpect(status().isOk());

        // Validate the SgsnIpList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSgsnIpListUpdatableFieldsEquals(partialUpdatedSgsnIpList, getPersistedSgsnIpList(partialUpdatedSgsnIpList));
    }

    @Test
    @Transactional
    void patchNonExistingSgsnIpList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sgsnIpList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSgsnIpListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sgsnIpList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sgsnIpList))
            )
            .andExpect(status().isBadRequest());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSgsnIpList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sgsnIpList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSgsnIpListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sgsnIpList))
            )
            .andExpect(status().isBadRequest());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSgsnIpList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sgsnIpList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSgsnIpListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sgsnIpList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SgsnIpList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSgsnIpList() throws Exception {
        // Initialize the database
        insertedSgsnIpList = sgsnIpListRepository.saveAndFlush(sgsnIpList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sgsnIpList
        restSgsnIpListMockMvc
            .perform(delete(ENTITY_API_URL_ID, sgsnIpList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sgsnIpListRepository.count();
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

    protected SgsnIpList getPersistedSgsnIpList(SgsnIpList sgsnIpList) {
        return sgsnIpListRepository.findById(sgsnIpList.getId()).orElseThrow();
    }

    protected void assertPersistedSgsnIpListToMatchAllProperties(SgsnIpList expectedSgsnIpList) {
        assertSgsnIpListAllPropertiesEquals(expectedSgsnIpList, getPersistedSgsnIpList(expectedSgsnIpList));
    }

    protected void assertPersistedSgsnIpListToMatchUpdatableProperties(SgsnIpList expectedSgsnIpList) {
        assertSgsnIpListAllUpdatablePropertiesEquals(expectedSgsnIpList, getPersistedSgsnIpList(expectedSgsnIpList));
    }
}
