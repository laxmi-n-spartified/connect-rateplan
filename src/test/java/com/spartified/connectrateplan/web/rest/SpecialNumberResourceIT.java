package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.SpecialNumberAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.SpecialNumber;
import com.spartified.connectrateplan.repository.SpecialNumberRepository;
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
 * Integration tests for the {@link SpecialNumberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialNumberResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_INPUT_NUM = 1L;
    private static final Long UPDATED_INPUT_NUM = 2L;

    private static final Long DEFAULT_TRANSLATED_NUM = 1L;
    private static final Long UPDATED_TRANSLATED_NUM = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/special-numbers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialNumberRepository specialNumberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialNumberMockMvc;

    private SpecialNumber specialNumber;

    private SpecialNumber insertedSpecialNumber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialNumber createEntity() {
        return new SpecialNumber()
            .tenantId(DEFAULT_TENANT_ID)
            .inputNum(DEFAULT_INPUT_NUM)
            .translatedNum(DEFAULT_TRANSLATED_NUM)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialNumber createUpdatedEntity() {
        return new SpecialNumber()
            .tenantId(UPDATED_TENANT_ID)
            .inputNum(UPDATED_INPUT_NUM)
            .translatedNum(UPDATED_TRANSLATED_NUM)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        specialNumber = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSpecialNumber != null) {
            specialNumberRepository.delete(insertedSpecialNumber);
            insertedSpecialNumber = null;
        }
    }

    @Test
    @Transactional
    void createSpecialNumber() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SpecialNumber
        var returnedSpecialNumber = om.readValue(
            restSpecialNumberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialNumber)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpecialNumber.class
        );

        // Validate the SpecialNumber in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSpecialNumberUpdatableFieldsEquals(returnedSpecialNumber, getPersistedSpecialNumber(returnedSpecialNumber));

        insertedSpecialNumber = returnedSpecialNumber;
    }

    @Test
    @Transactional
    void createSpecialNumberWithExistingId() throws Exception {
        // Create the SpecialNumber with an existing ID
        specialNumber.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialNumberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialNumber)))
            .andExpect(status().isBadRequest());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpecialNumbers() throws Exception {
        // Initialize the database
        insertedSpecialNumber = specialNumberRepository.saveAndFlush(specialNumber);

        // Get all the specialNumberList
        restSpecialNumberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialNumber.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].inputNum").value(hasItem(DEFAULT_INPUT_NUM.intValue())))
            .andExpect(jsonPath("$.[*].translatedNum").value(hasItem(DEFAULT_TRANSLATED_NUM.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSpecialNumber() throws Exception {
        // Initialize the database
        insertedSpecialNumber = specialNumberRepository.saveAndFlush(specialNumber);

        // Get the specialNumber
        restSpecialNumberMockMvc
            .perform(get(ENTITY_API_URL_ID, specialNumber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialNumber.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.inputNum").value(DEFAULT_INPUT_NUM.intValue()))
            .andExpect(jsonPath("$.translatedNum").value(DEFAULT_TRANSLATED_NUM.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSpecialNumber() throws Exception {
        // Get the specialNumber
        restSpecialNumberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialNumber() throws Exception {
        // Initialize the database
        insertedSpecialNumber = specialNumberRepository.saveAndFlush(specialNumber);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialNumber
        SpecialNumber updatedSpecialNumber = specialNumberRepository.findById(specialNumber.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialNumber are not directly saved in db
        em.detach(updatedSpecialNumber);
        updatedSpecialNumber
            .tenantId(UPDATED_TENANT_ID)
            .inputNum(UPDATED_INPUT_NUM)
            .translatedNum(UPDATED_TRANSLATED_NUM)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSpecialNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpecialNumber.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSpecialNumber))
            )
            .andExpect(status().isOk());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialNumberToMatchAllProperties(updatedSpecialNumber);
    }

    @Test
    @Transactional
    void putNonExistingSpecialNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialNumber.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialNumber.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialNumberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialNumber)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialNumberWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialNumber = specialNumberRepository.saveAndFlush(specialNumber);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialNumber using partial update
        SpecialNumber partialUpdatedSpecialNumber = new SpecialNumber();
        partialUpdatedSpecialNumber.setId(specialNumber.getId());

        partialUpdatedSpecialNumber.inputNum(UPDATED_INPUT_NUM);

        restSpecialNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialNumber))
            )
            .andExpect(status().isOk());

        // Validate the SpecialNumber in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialNumberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialNumber, specialNumber),
            getPersistedSpecialNumber(specialNumber)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialNumberWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialNumber = specialNumberRepository.saveAndFlush(specialNumber);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialNumber using partial update
        SpecialNumber partialUpdatedSpecialNumber = new SpecialNumber();
        partialUpdatedSpecialNumber.setId(specialNumber.getId());

        partialUpdatedSpecialNumber
            .tenantId(UPDATED_TENANT_ID)
            .inputNum(UPDATED_INPUT_NUM)
            .translatedNum(UPDATED_TRANSLATED_NUM)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restSpecialNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialNumber))
            )
            .andExpect(status().isOk());

        // Validate the SpecialNumber in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialNumberUpdatableFieldsEquals(partialUpdatedSpecialNumber, getPersistedSpecialNumber(partialUpdatedSpecialNumber));
    }

    @Test
    @Transactional
    void patchNonExistingSpecialNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialNumber.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialNumberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(specialNumber)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialNumber() throws Exception {
        // Initialize the database
        insertedSpecialNumber = specialNumberRepository.saveAndFlush(specialNumber);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialNumber
        restSpecialNumberMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialNumber.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialNumberRepository.count();
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

    protected SpecialNumber getPersistedSpecialNumber(SpecialNumber specialNumber) {
        return specialNumberRepository.findById(specialNumber.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialNumberToMatchAllProperties(SpecialNumber expectedSpecialNumber) {
        assertSpecialNumberAllPropertiesEquals(expectedSpecialNumber, getPersistedSpecialNumber(expectedSpecialNumber));
    }

    protected void assertPersistedSpecialNumberToMatchUpdatableProperties(SpecialNumber expectedSpecialNumber) {
        assertSpecialNumberAllUpdatablePropertiesEquals(expectedSpecialNumber, getPersistedSpecialNumber(expectedSpecialNumber));
    }
}
