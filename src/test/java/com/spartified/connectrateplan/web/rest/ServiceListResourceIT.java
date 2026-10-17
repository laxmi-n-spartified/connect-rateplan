package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.ServiceListAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.ServiceList;
import com.spartified.connectrateplan.repository.ServiceListRepository;
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
 * Integration tests for the {@link ServiceListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceListResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICETYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICETYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/service-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceListRepository serviceListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceListMockMvc;

    private ServiceList serviceList;

    private ServiceList insertedServiceList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceList createEntity() {
        return new ServiceList()
            .tenantId(DEFAULT_TENANT_ID)
            .servicetype(DEFAULT_SERVICETYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceList createUpdatedEntity() {
        return new ServiceList()
            .tenantId(UPDATED_TENANT_ID)
            .servicetype(UPDATED_SERVICETYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        serviceList = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceList != null) {
            serviceListRepository.delete(insertedServiceList);
            insertedServiceList = null;
        }
    }

    @Test
    @Transactional
    void createServiceList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceList
        var returnedServiceList = om.readValue(
            restServiceListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceList)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceList.class
        );

        // Validate the ServiceList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertServiceListUpdatableFieldsEquals(returnedServiceList, getPersistedServiceList(returnedServiceList));

        insertedServiceList = returnedServiceList;
    }

    @Test
    @Transactional
    void createServiceListWithExistingId() throws Exception {
        // Create the ServiceList with an existing ID
        serviceList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceList)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceLists() throws Exception {
        // Initialize the database
        insertedServiceList = serviceListRepository.saveAndFlush(serviceList);

        // Get all the serviceListList
        restServiceListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceList.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].servicetype").value(hasItem(DEFAULT_SERVICETYPE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getServiceList() throws Exception {
        // Initialize the database
        insertedServiceList = serviceListRepository.saveAndFlush(serviceList);

        // Get the serviceList
        restServiceListMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceList.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.servicetype").value(DEFAULT_SERVICETYPE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingServiceList() throws Exception {
        // Get the serviceList
        restServiceListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceList() throws Exception {
        // Initialize the database
        insertedServiceList = serviceListRepository.saveAndFlush(serviceList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceList
        ServiceList updatedServiceList = serviceListRepository.findById(serviceList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceList are not directly saved in db
        em.detach(updatedServiceList);
        updatedServiceList
            .tenantId(UPDATED_TENANT_ID)
            .servicetype(UPDATED_SERVICETYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restServiceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedServiceList))
            )
            .andExpect(status().isOk());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceListToMatchAllProperties(updatedServiceList);
    }

    @Test
    @Transactional
    void putNonExistingServiceList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceListWithPatch() throws Exception {
        // Initialize the database
        insertedServiceList = serviceListRepository.saveAndFlush(serviceList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceList using partial update
        ServiceList partialUpdatedServiceList = new ServiceList();
        partialUpdatedServiceList.setId(serviceList.getId());

        partialUpdatedServiceList.tenantId(UPDATED_TENANT_ID).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restServiceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceList))
            )
            .andExpect(status().isOk());

        // Validate the ServiceList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceList, serviceList),
            getPersistedServiceList(serviceList)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceListWithPatch() throws Exception {
        // Initialize the database
        insertedServiceList = serviceListRepository.saveAndFlush(serviceList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceList using partial update
        ServiceList partialUpdatedServiceList = new ServiceList();
        partialUpdatedServiceList.setId(serviceList.getId());

        partialUpdatedServiceList
            .tenantId(UPDATED_TENANT_ID)
            .servicetype(UPDATED_SERVICETYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restServiceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceList))
            )
            .andExpect(status().isOk());

        // Validate the ServiceList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceListUpdatableFieldsEquals(partialUpdatedServiceList, getPersistedServiceList(partialUpdatedServiceList));
    }

    @Test
    @Transactional
    void patchNonExistingServiceList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceList() throws Exception {
        // Initialize the database
        insertedServiceList = serviceListRepository.saveAndFlush(serviceList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceList
        restServiceListMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceListRepository.count();
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

    protected ServiceList getPersistedServiceList(ServiceList serviceList) {
        return serviceListRepository.findById(serviceList.getId()).orElseThrow();
    }

    protected void assertPersistedServiceListToMatchAllProperties(ServiceList expectedServiceList) {
        assertServiceListAllPropertiesEquals(expectedServiceList, getPersistedServiceList(expectedServiceList));
    }

    protected void assertPersistedServiceListToMatchUpdatableProperties(ServiceList expectedServiceList) {
        assertServiceListAllUpdatablePropertiesEquals(expectedServiceList, getPersistedServiceList(expectedServiceList));
    }
}
