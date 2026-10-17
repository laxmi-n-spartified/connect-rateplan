package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.TierVolumeDetails;
import com.spartified.connectrateplan.repository.TierVolumeDetailsRepository;
import com.spartified.connectrateplan.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.spartified.connectrateplan.domain.TierVolumeDetails}.
 */
@RestController
@RequestMapping("/api/tier-volume-details")
@Transactional(rollbackFor = Exception.class)
public class TierVolumeDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(TierVolumeDetailsResource.class);

    private static final String ENTITY_NAME = "connectRateplanTierVolumeDetails";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final TierVolumeDetailsRepository tierVolumeDetailsRepository;

    public TierVolumeDetailsResource(TierVolumeDetailsRepository tierVolumeDetailsRepository) {
        this.tierVolumeDetailsRepository = tierVolumeDetailsRepository;
    }

    /**
     * {@code POST  /tier-volume-details} : Create a new tierVolumeDetails.
     *
     * @param tierVolumeDetails the tierVolumeDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tierVolumeDetails, or with status {@code 400 (Bad Request)} if the tierVolumeDetails already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TierVolumeDetails> createTierVolumeDetails(@Valid @RequestBody TierVolumeDetails tierVolumeDetails)
        throws URISyntaxException {
        LOG.debug("REST request to save TierVolumeDetails : {}", tierVolumeDetails);
        if (tierVolumeDetails.getId() != null) {
            throw new BadRequestAlertException("A new tierVolumeDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tierVolumeDetails = tierVolumeDetailsRepository.save(tierVolumeDetails);
        return ResponseEntity.created(new URI("/api/tier-volume-details/" + tierVolumeDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tierVolumeDetails.getId().toString()))
            .body(tierVolumeDetails);
    }

    /**
     * {@code PUT  /tier-volume-details/:id} : Updates an existing tierVolumeDetails.
     *
     * @param id the id of the tierVolumeDetails to save.
     * @param tierVolumeDetails the tierVolumeDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tierVolumeDetails,
     * or with status {@code 400 (Bad Request)} if the tierVolumeDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tierVolumeDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TierVolumeDetails> updateTierVolumeDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TierVolumeDetails tierVolumeDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to update TierVolumeDetails : {}, {}", id, tierVolumeDetails);
        if (tierVolumeDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierVolumeDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierVolumeDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tierVolumeDetails = tierVolumeDetailsRepository.save(tierVolumeDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tierVolumeDetails.getId().toString()))
            .body(tierVolumeDetails);
    }

    /**
     * {@code PATCH  /tier-volume-details/:id} : Partial updates given fields of an existing tierVolumeDetails, field will ignore if it is null
     *
     * @param id the id of the tierVolumeDetails to save.
     * @param tierVolumeDetails the tierVolumeDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tierVolumeDetails,
     * or with status {@code 400 (Bad Request)} if the tierVolumeDetails is not valid,
     * or with status {@code 404 (Not Found)} if the tierVolumeDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the tierVolumeDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TierVolumeDetails> partialUpdateTierVolumeDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TierVolumeDetails tierVolumeDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TierVolumeDetails : {}, {}", id, tierVolumeDetails);
        if (tierVolumeDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierVolumeDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierVolumeDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TierVolumeDetails> result = tierVolumeDetailsRepository
            .findById(tierVolumeDetails.getId())
            .map(existingTierVolumeDetails -> {
                updateIfPresent(existingTierVolumeDetails::setMrc, tierVolumeDetails.getMrc());
                updateIfPresent(existingTierVolumeDetails::setRatingTypeMrc, tierVolumeDetails.getRatingTypeMrc());
                updateIfPresent(existingTierVolumeDetails::setRatingTypeUsage, tierVolumeDetails.getRatingTypeUsage());
                updateIfPresent(existingTierVolumeDetails::setTenantId, tierVolumeDetails.getTenantId());
                updateIfPresent(existingTierVolumeDetails::setTierLimit, tierVolumeDetails.getTierLimit());
                updateIfPresent(existingTierVolumeDetails::setCreatedDate, tierVolumeDetails.getCreatedDate());
                updateIfPresent(existingTierVolumeDetails::setModifiedDate, tierVolumeDetails.getModifiedDate());

                return existingTierVolumeDetails;
            })
            .map(tierVolumeDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tierVolumeDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /tier-volume-details} : get all the Tier Volume Details.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tier Volume Details in body.
     */
    @GetMapping("")
    public List<TierVolumeDetails> getAllTierVolumeDetailses() {
        LOG.debug("REST request to get all TierVolumeDetailses");
        return tierVolumeDetailsRepository.findAll();
    }

    /**
     * {@code GET  /tier-volume-details/:id} : get the "id" tierVolumeDetails.
     *
     * @param id the id of the tierVolumeDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tierVolumeDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TierVolumeDetails> getTierVolumeDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TierVolumeDetails : {}", id);
        Optional<TierVolumeDetails> tierVolumeDetails = tierVolumeDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tierVolumeDetails);
    }

    /**
     * {@code DELETE  /tier-volume-details/:id} : delete the "id" tierVolumeDetails.
     *
     * @param id the id of the tierVolumeDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTierVolumeDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TierVolumeDetails : {}", id);
        tierVolumeDetailsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
