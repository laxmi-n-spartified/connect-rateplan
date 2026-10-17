package com.spartified.connectrateplan.web.rest;

import static com.spartified.connectrateplan.domain.TadigCodeAsserts.*;
import static com.spartified.connectrateplan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spartified.connectrateplan.IntegrationTest;
import com.spartified.connectrateplan.domain.TadigCode;
import com.spartified.connectrateplan.repository.TadigCodeRepository;
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
 * Integration tests for the {@link TadigCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TadigCodeResourceIT {

    private static final String DEFAULT_TADIG = "AAAAAAAAAA";
    private static final String UPDATED_TADIG = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.ofEpochMilli(1792198539903L);

    private static final String ENTITY_API_URL = "/api/tadig-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TadigCodeRepository tadigCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTadigCodeMockMvc;

    private TadigCode tadigCode;

    private TadigCode insertedTadigCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TadigCode createEntity() {
        return new TadigCode().tadig(DEFAULT_TADIG).createdDate(DEFAULT_CREATED_DATE).modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TadigCode createUpdatedEntity() {
        return new TadigCode().tadig(UPDATED_TADIG).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        tadigCode = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTadigCode != null) {
            tadigCodeRepository.delete(insertedTadigCode);
            insertedTadigCode = null;
        }
    }

    @Test
    @Transactional
    void createTadigCode() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TadigCode
        var returnedTadigCode = om.readValue(
            restTadigCodeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tadigCode)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TadigCode.class
        );

        // Validate the TadigCode in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTadigCodeUpdatableFieldsEquals(returnedTadigCode, getPersistedTadigCode(returnedTadigCode));

        insertedTadigCode = returnedTadigCode;
    }

    @Test
    @Transactional
    void createTadigCodeWithExistingId() throws Exception {
        // Create the TadigCode with an existing ID
        tadigCode.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTadigCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tadigCode)))
            .andExpect(status().isBadRequest());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTadigCodes() throws Exception {
        // Initialize the database
        insertedTadigCode = tadigCodeRepository.saveAndFlush(tadigCode);

        // Get all the tadigCodeList
        restTadigCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tadigCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].tadig").value(hasItem(DEFAULT_TADIG)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTadigCode() throws Exception {
        // Initialize the database
        insertedTadigCode = tadigCodeRepository.saveAndFlush(tadigCode);

        // Get the tadigCode
        restTadigCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, tadigCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tadigCode.getId().intValue()))
            .andExpect(jsonPath("$.tadig").value(DEFAULT_TADIG))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTadigCode() throws Exception {
        // Get the tadigCode
        restTadigCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTadigCode() throws Exception {
        // Initialize the database
        insertedTadigCode = tadigCodeRepository.saveAndFlush(tadigCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tadigCode
        TadigCode updatedTadigCode = tadigCodeRepository.findById(tadigCode.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTadigCode are not directly saved in db
        em.detach(updatedTadigCode);
        updatedTadigCode.tadig(UPDATED_TADIG).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restTadigCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTadigCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTadigCode))
            )
            .andExpect(status().isOk());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTadigCodeToMatchAllProperties(updatedTadigCode);
    }

    @Test
    @Transactional
    void putNonExistingTadigCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tadigCode.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTadigCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tadigCode.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tadigCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTadigCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tadigCode.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTadigCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tadigCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTadigCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tadigCode.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTadigCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tadigCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTadigCodeWithPatch() throws Exception {
        // Initialize the database
        insertedTadigCode = tadigCodeRepository.saveAndFlush(tadigCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tadigCode using partial update
        TadigCode partialUpdatedTadigCode = new TadigCode();
        partialUpdatedTadigCode.setId(tadigCode.getId());

        restTadigCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTadigCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTadigCode))
            )
            .andExpect(status().isOk());

        // Validate the TadigCode in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTadigCodeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTadigCode, tadigCode),
            getPersistedTadigCode(tadigCode)
        );
    }

    @Test
    @Transactional
    void fullUpdateTadigCodeWithPatch() throws Exception {
        // Initialize the database
        insertedTadigCode = tadigCodeRepository.saveAndFlush(tadigCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tadigCode using partial update
        TadigCode partialUpdatedTadigCode = new TadigCode();
        partialUpdatedTadigCode.setId(tadigCode.getId());

        partialUpdatedTadigCode.tadig(UPDATED_TADIG).createdDate(UPDATED_CREATED_DATE).modifiedDate(UPDATED_MODIFIED_DATE);

        restTadigCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTadigCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTadigCode))
            )
            .andExpect(status().isOk());

        // Validate the TadigCode in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTadigCodeUpdatableFieldsEquals(partialUpdatedTadigCode, getPersistedTadigCode(partialUpdatedTadigCode));
    }

    @Test
    @Transactional
    void patchNonExistingTadigCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tadigCode.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTadigCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tadigCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tadigCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTadigCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tadigCode.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTadigCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tadigCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTadigCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tadigCode.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTadigCodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tadigCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TadigCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTadigCode() throws Exception {
        // Initialize the database
        insertedTadigCode = tadigCodeRepository.saveAndFlush(tadigCode);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tadigCode
        restTadigCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, tadigCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tadigCodeRepository.count();
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

    protected TadigCode getPersistedTadigCode(TadigCode tadigCode) {
        return tadigCodeRepository.findById(tadigCode.getId()).orElseThrow();
    }

    protected void assertPersistedTadigCodeToMatchAllProperties(TadigCode expectedTadigCode) {
        assertTadigCodeAllPropertiesEquals(expectedTadigCode, getPersistedTadigCode(expectedTadigCode));
    }

    protected void assertPersistedTadigCodeToMatchUpdatableProperties(TadigCode expectedTadigCode) {
        assertTadigCodeAllUpdatablePropertiesEquals(expectedTadigCode, getPersistedTadigCode(expectedTadigCode));
    }
}
