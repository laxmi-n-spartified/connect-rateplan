package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.RateDetails;
import com.spartified.connectrateplan.repository.RateDetailsRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.RateDetails}.
 */
@RestController
@RequestMapping("/api/rate-details")
@Transactional(rollbackFor = Exception.class)
public class RateDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(RateDetailsResource.class);

    private static final String ENTITY_NAME = "connectRateplanRateDetails";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final RateDetailsRepository rateDetailsRepository;

    public RateDetailsResource(RateDetailsRepository rateDetailsRepository) {
        this.rateDetailsRepository = rateDetailsRepository;
    }

    /**
     * {@code POST  /rate-details} : Create a new rateDetails.
     *
     * @param rateDetails the rateDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rateDetails, or with status {@code 400 (Bad Request)} if the rateDetails already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RateDetails> createRateDetails(@Valid @RequestBody RateDetails rateDetails) throws URISyntaxException {
        LOG.debug("REST request to save RateDetails : {}", rateDetails);
        if (rateDetails.getId() != null) {
            throw new BadRequestAlertException("A new rateDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rateDetails = rateDetailsRepository.save(rateDetails);
        return ResponseEntity.created(new URI("/api/rate-details/" + rateDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, rateDetails.getId().toString()))
            .body(rateDetails);
    }

    /**
     * {@code PUT  /rate-details/:id} : Updates an existing rateDetails.
     *
     * @param id the id of the rateDetails to save.
     * @param rateDetails the rateDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateDetails,
     * or with status {@code 400 (Bad Request)} if the rateDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rateDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RateDetails> updateRateDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RateDetails rateDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to update RateDetails : {}, {}", id, rateDetails);
        if (rateDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rateDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rateDetails = rateDetailsRepository.save(rateDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateDetails.getId().toString()))
            .body(rateDetails);
    }

    /**
     * {@code PATCH  /rate-details/:id} : Partial updates given fields of an existing rateDetails, field will ignore if it is null
     *
     * @param id the id of the rateDetails to save.
     * @param rateDetails the rateDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateDetails,
     * or with status {@code 400 (Bad Request)} if the rateDetails is not valid,
     * or with status {@code 404 (Not Found)} if the rateDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the rateDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RateDetails> partialUpdateRateDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RateDetails rateDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update RateDetails : {}, {}", id, rateDetails);
        if (rateDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rateDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RateDetails> result = rateDetailsRepository
            .findById(rateDetails.getId())
            .map(existingRateDetails -> {
                updateIfPresent(existingRateDetails::setTenantId, rateDetails.getTenantId());
                updateIfPresent(existingRateDetails::setDataOverageRate, rateDetails.getDataOverageRate());
                updateIfPresent(existingRateDetails::setDataOverageRateUnit, rateDetails.getDataOverageRateUnit());
                updateIfPresent(existingRateDetails::setSmsMoOverageRate, rateDetails.getSmsMoOverageRate());
                updateIfPresent(existingRateDetails::setSmsMoOverageRateUnit, rateDetails.getSmsMoOverageRateUnit());
                updateIfPresent(existingRateDetails::setSmsMtOverageRate, rateDetails.getSmsMtOverageRate());
                updateIfPresent(existingRateDetails::setSmsMtOverageRateUnit, rateDetails.getSmsMtOverageRateUnit());
                updateIfPresent(existingRateDetails::setVoiceMoOverageRate, rateDetails.getVoiceMoOverageRate());
                updateIfPresent(existingRateDetails::setVoiceMoOverageRateUnit, rateDetails.getVoiceMoOverageRateUnit());
                updateIfPresent(existingRateDetails::setVoiceMtOverageRate, rateDetails.getVoiceMtOverageRate());
                updateIfPresent(existingRateDetails::setVoiceMtOverageRateUnit, rateDetails.getVoiceMtOverageRateUnit());
                updateIfPresent(existingRateDetails::setIsDefault, rateDetails.getIsDefault());
                updateIfPresent(existingRateDetails::setDataOverageRateUnitFactor, rateDetails.getDataOverageRateUnitFactor());
                updateIfPresent(existingRateDetails::setSmsMoOverageRateUnitFactor, rateDetails.getSmsMoOverageRateUnitFactor());
                updateIfPresent(existingRateDetails::setSmsMtOverageRateUnitFactor, rateDetails.getSmsMtOverageRateUnitFactor());
                updateIfPresent(existingRateDetails::setVoiceMoOverageRateUnitFactor, rateDetails.getVoiceMoOverageRateUnitFactor());
                updateIfPresent(existingRateDetails::setVoiceMtOverageRateUnitFactor, rateDetails.getVoiceMtOverageRateUnitFactor());
                updateIfPresent(existingRateDetails::setCreatedDate, rateDetails.getCreatedDate());
                updateIfPresent(existingRateDetails::setModifiedDate, rateDetails.getModifiedDate());

                return existingRateDetails;
            })
            .map(rateDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /rate-details} : get all the Rate Details.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Rate Details in body.
     */
    @GetMapping("")
    public List<RateDetails> getAllRateDetailses() {
        LOG.debug("REST request to get all RateDetailses");
        return rateDetailsRepository.findAll();
    }

    /**
     * {@code GET  /rate-details/:id} : get the "id" rateDetails.
     *
     * @param id the id of the rateDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rateDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RateDetails> getRateDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RateDetails : {}", id);
        Optional<RateDetails> rateDetails = rateDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rateDetails);
    }

    /**
     * {@code DELETE  /rate-details/:id} : delete the "id" rateDetails.
     *
     * @param id the id of the rateDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRateDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RateDetails : {}", id);
        rateDetailsRepository.deleteById(id);
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
