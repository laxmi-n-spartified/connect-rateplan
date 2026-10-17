package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.Rateplan;
import com.spartified.connectrateplan.repository.RateplanRepository;
import com.spartified.connectrateplan.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.spartified.connectrateplan.domain.Rateplan}.
 */
@RestController
@RequestMapping("/api/rateplans")
@Transactional(rollbackFor = Exception.class)
public class RateplanResource {

    private static final Logger LOG = LoggerFactory.getLogger(RateplanResource.class);

    private static final String ENTITY_NAME = "connectRateplanRateplan";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final RateplanRepository rateplanRepository;

    public RateplanResource(RateplanRepository rateplanRepository) {
        this.rateplanRepository = rateplanRepository;
    }

    /**
     * {@code POST  /rateplans} : Create a new rateplan.
     *
     * @param rateplan the rateplan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rateplan, or with status {@code 400 (Bad Request)} if the rateplan already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Rateplan> createRateplan(@Valid @RequestBody Rateplan rateplan) throws URISyntaxException {
        LOG.debug("REST request to save Rateplan : {}", rateplan);
        if (rateplan.getId() != null) {
            throw new BadRequestAlertException("A new rateplan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rateplan = rateplanRepository.save(rateplan);
        return ResponseEntity.created(new URI("/api/rateplans/" + rateplan.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, rateplan.getId().toString()))
            .body(rateplan);
    }

    /**
     * {@code PUT  /rateplans/:id} : Updates an existing rateplan.
     *
     * @param id the id of the rateplan to save.
     * @param rateplan the rateplan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateplan,
     * or with status {@code 400 (Bad Request)} if the rateplan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rateplan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rateplan> updateRateplan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Rateplan rateplan
    ) throws URISyntaxException {
        LOG.debug("REST request to update Rateplan : {}, {}", id, rateplan);
        if (rateplan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rateplan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateplanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rateplan = rateplanRepository.save(rateplan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateplan.getId().toString()))
            .body(rateplan);
    }

    /**
     * {@code PATCH  /rateplans/:id} : Partial updates given fields of an existing rateplan, field will ignore if it is null
     *
     * @param id the id of the rateplan to save.
     * @param rateplan the rateplan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateplan,
     * or with status {@code 400 (Bad Request)} if the rateplan is not valid,
     * or with status {@code 404 (Not Found)} if the rateplan is not found,
     * or with status {@code 500 (Internal Server Error)} if the rateplan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rateplan> partialUpdateRateplan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rateplan rateplan
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Rateplan : {}, {}", id, rateplan);
        if (rateplan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rateplan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateplanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rateplan> result = rateplanRepository
            .findById(rateplan.getId())
            .map(existingRateplan -> {
                updateIfPresent(existingRateplan::setTenantId, rateplan.getTenantId());
                updateIfPresent(existingRateplan::setName, rateplan.getName());
                updateIfPresent(existingRateplan::setOrganizationName, rateplan.getOrganizationName());
                updateIfPresent(existingRateplan::setActivationFee, rateplan.getActivationFee());
                updateIfPresent(existingRateplan::setNetworkAccessFee, rateplan.getNetworkAccessFee());
                updateIfPresent(existingRateplan::setSubscriptionFee, rateplan.getSubscriptionFee());
                updateIfPresent(existingRateplan::setCancellationFee, rateplan.getCancellationFee());
                updateIfPresent(existingRateplan::setAdditionalFees, rateplan.getAdditionalFees());
                updateIfPresent(existingRateplan::setReActivationFee, rateplan.getReActivationFee());
                updateIfPresent(existingRateplan::setContractPeriod, rateplan.getContractPeriod());
                updateIfPresent(existingRateplan::setNoticePeriod, rateplan.getNoticePeriod());
                updateIfPresent(existingRateplan::setStartDate, rateplan.getStartDate());
                updateIfPresent(existingRateplan::setEndDate, rateplan.getEndDate());
                updateIfPresent(existingRateplan::setStatus, rateplan.getStatus());
                updateIfPresent(existingRateplan::setPlanType, rateplan.getPlanType());
                updateIfPresent(existingRateplan::setVersion, rateplan.getVersion());
                updateIfPresent(existingRateplan::setSubscriptionType, rateplan.getSubscriptionType());
                updateIfPresent(existingRateplan::setIncludedTestData, rateplan.getIncludedTestData());
                updateIfPresent(existingRateplan::setIncludedTestSms, rateplan.getIncludedTestSms());
                updateIfPresent(existingRateplan::setIncludedTestVoice, rateplan.getIncludedTestVoice());
                updateIfPresent(existingRateplan::setExpiryDate, rateplan.getExpiryDate());
                updateIfPresent(existingRateplan::setTrialDuration, rateplan.getTrialDuration());
                updateIfPresent(existingRateplan::setPoolTariff, rateplan.getPoolTariff());
                updateIfPresent(existingRateplan::setPoolActivationFee, rateplan.getPoolActivationFee());
                updateIfPresent(existingRateplan::setPoolSubscriptionFee, rateplan.getPoolSubscriptionFee());
                updateIfPresent(existingRateplan::setRoundingForData, rateplan.getRoundingForData());
                updateIfPresent(existingRateplan::setRoundingForVoice, rateplan.getRoundingForVoice());
                updateIfPresent(existingRateplan::setIncludedTestDataUnit, rateplan.getIncludedTestDataUnit());
                updateIfPresent(existingRateplan::setIncludedTestSmsUnit, rateplan.getIncludedTestSmsUnit());
                updateIfPresent(existingRateplan::setIncludedTestVoiceUnit, rateplan.getIncludedTestVoiceUnit());
                updateIfPresent(existingRateplan::setRenewOnExpiry, rateplan.getRenewOnExpiry());
                updateIfPresent(existingRateplan::setTieredVolumeType, rateplan.getTieredVolumeType());
                updateIfPresent(existingRateplan::setValidityPeriod, rateplan.getValidityPeriod());
                updateIfPresent(existingRateplan::setCategory, rateplan.getCategory());
                updateIfPresent(existingRateplan::setQuotaProfileId, rateplan.getQuotaProfileId());
                updateIfPresent(existingRateplan::setQuotaProfileName, rateplan.getQuotaProfileName());
                updateIfPresent(existingRateplan::setExtId, rateplan.getExtId());
                updateIfPresent(existingRateplan::setFixedActiveFee, rateplan.getFixedActiveFee());
                updateIfPresent(existingRateplan::setSuspendFee, rateplan.getSuspendFee());
                updateIfPresent(existingRateplan::setSuspendedSimMrc, rateplan.getSuspendedSimMrc());
                updateIfPresent(existingRateplan::setUnsuspendFee, rateplan.getUnsuspendFee());
                updateIfPresent(existingRateplan::setDeactivationFee, rateplan.getDeactivationFee());
                updateIfPresent(existingRateplan::setCreatedDate, rateplan.getCreatedDate());
                updateIfPresent(existingRateplan::setModifiedDate, rateplan.getModifiedDate());

                return existingRateplan;
            })
            .map(rateplanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateplan.getId().toString())
        );
    }

    /**
     * {@code GET  /rateplans} : get all the Rateplans.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Rateplans in body.
     */
    @GetMapping("")
    public List<Rateplan> getAllRateplans(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("rateplanlcstatemodel-is-null".equals(filter)) {
            LOG.debug("REST request to get all Rateplans where rateplanLcStateModel is null");
            return StreamSupport.stream(rateplanRepository.findAll().spliterator(), false)
                .filter(rateplan -> rateplan.getRateplanLcStateModel() == null)
                .toList();
        }
        LOG.debug("REST request to get all Rateplans");
        if (eagerload) {
            return rateplanRepository.findAllWithEagerRelationships();
        } else {
            return rateplanRepository.findAll();
        }
    }

    /**
     * {@code GET  /rateplans/:id} : get the "id" rateplan.
     *
     * @param id the id of the rateplan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rateplan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rateplan> getRateplan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Rateplan : {}", id);
        Optional<Rateplan> rateplan = rateplanRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(rateplan);
    }

    /**
     * {@code DELETE  /rateplans/:id} : delete the "id" rateplan.
     *
     * @param id the id of the rateplan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRateplan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Rateplan : {}", id);
        rateplanRepository.deleteById(id);
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
