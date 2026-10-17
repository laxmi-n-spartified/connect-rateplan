package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.BillingCycle;
import com.spartified.connectrateplan.repository.BillingCycleRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.BillingCycle}.
 */
@RestController
@RequestMapping("/api/billing-cycles")
@Transactional(rollbackFor = Exception.class)
public class BillingCycleResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillingCycleResource.class);

    private static final String ENTITY_NAME = "connectRateplanBillingCycle";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final BillingCycleRepository billingCycleRepository;

    public BillingCycleResource(BillingCycleRepository billingCycleRepository) {
        this.billingCycleRepository = billingCycleRepository;
    }

    /**
     * {@code POST  /billing-cycles} : Create a new billingCycle.
     *
     * @param billingCycle the billingCycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billingCycle, or with status {@code 400 (Bad Request)} if the billingCycle already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillingCycle> createBillingCycle(@Valid @RequestBody BillingCycle billingCycle) throws URISyntaxException {
        LOG.debug("REST request to save BillingCycle : {}", billingCycle);
        if (billingCycle.getId() != null) {
            throw new BadRequestAlertException("A new billingCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billingCycle = billingCycleRepository.save(billingCycle);
        return ResponseEntity.created(new URI("/api/billing-cycles/" + billingCycle.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, billingCycle.getId().toString()))
            .body(billingCycle);
    }

    /**
     * {@code PUT  /billing-cycles/:id} : Updates an existing billingCycle.
     *
     * @param id the id of the billingCycle to save.
     * @param billingCycle the billingCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingCycle,
     * or with status {@code 400 (Bad Request)} if the billingCycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billingCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillingCycle> updateBillingCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillingCycle billingCycle
    ) throws URISyntaxException {
        LOG.debug("REST request to update BillingCycle : {}, {}", id, billingCycle);
        if (billingCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingCycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billingCycle = billingCycleRepository.save(billingCycle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billingCycle.getId().toString()))
            .body(billingCycle);
    }

    /**
     * {@code PATCH  /billing-cycles/:id} : Partial updates given fields of an existing billingCycle, field will ignore if it is null
     *
     * @param id the id of the billingCycle to save.
     * @param billingCycle the billingCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billingCycle,
     * or with status {@code 400 (Bad Request)} if the billingCycle is not valid,
     * or with status {@code 404 (Not Found)} if the billingCycle is not found,
     * or with status {@code 500 (Internal Server Error)} if the billingCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillingCycle> partialUpdateBillingCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillingCycle billingCycle
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update BillingCycle : {}, {}", id, billingCycle);
        if (billingCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billingCycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billingCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillingCycle> result = billingCycleRepository
            .findById(billingCycle.getId())
            .map(existingBillingCycle -> {
                updateIfPresent(existingBillingCycle::setBillCycleDate, billingCycle.getBillCycleDate());
                updateIfPresent(existingBillingCycle::setBillCycleName, billingCycle.getBillCycleName());
                updateIfPresent(existingBillingCycle::setBillCycleDuration, billingCycle.getBillCycleDuration());
                updateIfPresent(existingBillingCycle::setCreatedDate, billingCycle.getCreatedDate());
                updateIfPresent(existingBillingCycle::setModifiedDate, billingCycle.getModifiedDate());

                return existingBillingCycle;
            })
            .map(billingCycleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billingCycle.getId().toString())
        );
    }

    /**
     * {@code GET  /billing-cycles} : get all the Billing Cycles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Billing Cycles in body.
     */
    @GetMapping("")
    public List<BillingCycle> getAllBillingCycles() {
        LOG.debug("REST request to get all BillingCycles");
        return billingCycleRepository.findAll();
    }

    /**
     * {@code GET  /billing-cycles/:id} : get the "id" billingCycle.
     *
     * @param id the id of the billingCycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billingCycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillingCycle> getBillingCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BillingCycle : {}", id);
        Optional<BillingCycle> billingCycle = billingCycleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(billingCycle);
    }

    /**
     * {@code DELETE  /billing-cycles/:id} : delete the "id" billingCycle.
     *
     * @param id the id of the billingCycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillingCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BillingCycle : {}", id);
        billingCycleRepository.deleteById(id);
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
