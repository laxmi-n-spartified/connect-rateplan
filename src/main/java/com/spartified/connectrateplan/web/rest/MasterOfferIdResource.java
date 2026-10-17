package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.MasterOfferId;
import com.spartified.connectrateplan.repository.MasterOfferIdRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.MasterOfferId}.
 */
@RestController
@RequestMapping("/api/master-offer-ids")
@Transactional(rollbackFor = Exception.class)
public class MasterOfferIdResource {

    private static final Logger LOG = LoggerFactory.getLogger(MasterOfferIdResource.class);

    private static final String ENTITY_NAME = "connectRateplanMasterOfferId";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final MasterOfferIdRepository masterOfferIdRepository;

    public MasterOfferIdResource(MasterOfferIdRepository masterOfferIdRepository) {
        this.masterOfferIdRepository = masterOfferIdRepository;
    }

    /**
     * {@code POST  /master-offer-ids} : Create a new masterOfferId.
     *
     * @param masterOfferId the masterOfferId to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterOfferId, or with status {@code 400 (Bad Request)} if the masterOfferId already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MasterOfferId> createMasterOfferId(@Valid @RequestBody MasterOfferId masterOfferId) throws URISyntaxException {
        LOG.debug("REST request to save MasterOfferId : {}", masterOfferId);
        if (masterOfferId.getId() != null) {
            throw new BadRequestAlertException("A new masterOfferId cannot already have an ID", ENTITY_NAME, "idexists");
        }
        masterOfferId = masterOfferIdRepository.save(masterOfferId);
        return ResponseEntity.created(new URI("/api/master-offer-ids/" + masterOfferId.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, masterOfferId.getId().toString()))
            .body(masterOfferId);
    }

    /**
     * {@code PUT  /master-offer-ids/:id} : Updates an existing masterOfferId.
     *
     * @param id the id of the masterOfferId to save.
     * @param masterOfferId the masterOfferId to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterOfferId,
     * or with status {@code 400 (Bad Request)} if the masterOfferId is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterOfferId couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MasterOfferId> updateMasterOfferId(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MasterOfferId masterOfferId
    ) throws URISyntaxException {
        LOG.debug("REST request to update MasterOfferId : {}, {}", id, masterOfferId);
        if (masterOfferId.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterOfferId.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterOfferIdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        masterOfferId = masterOfferIdRepository.save(masterOfferId);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, masterOfferId.getId().toString()))
            .body(masterOfferId);
    }

    /**
     * {@code PATCH  /master-offer-ids/:id} : Partial updates given fields of an existing masterOfferId, field will ignore if it is null
     *
     * @param id the id of the masterOfferId to save.
     * @param masterOfferId the masterOfferId to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterOfferId,
     * or with status {@code 400 (Bad Request)} if the masterOfferId is not valid,
     * or with status {@code 404 (Not Found)} if the masterOfferId is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterOfferId couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterOfferId> partialUpdateMasterOfferId(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MasterOfferId masterOfferId
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update MasterOfferId : {}, {}", id, masterOfferId);
        if (masterOfferId.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterOfferId.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterOfferIdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterOfferId> result = masterOfferIdRepository
            .findById(masterOfferId.getId())
            .map(existingMasterOfferId -> {
                updateIfPresent(existingMasterOfferId::setOfferId, masterOfferId.getOfferId());
                updateIfPresent(existingMasterOfferId::setOfferName, masterOfferId.getOfferName());
                updateIfPresent(existingMasterOfferId::setCreatedDate, masterOfferId.getCreatedDate());
                updateIfPresent(existingMasterOfferId::setModifiedDate, masterOfferId.getModifiedDate());

                return existingMasterOfferId;
            })
            .map(masterOfferIdRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, masterOfferId.getId().toString())
        );
    }

    /**
     * {@code GET  /master-offer-ids} : get all the Master Offer Ids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Master Offer Ids in body.
     */
    @GetMapping("")
    public List<MasterOfferId> getAllMasterOfferIds() {
        LOG.debug("REST request to get all MasterOfferIds");
        return masterOfferIdRepository.findAll();
    }

    /**
     * {@code GET  /master-offer-ids/:id} : get the "id" masterOfferId.
     *
     * @param id the id of the masterOfferId to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterOfferId, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MasterOfferId> getMasterOfferId(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MasterOfferId : {}", id);
        Optional<MasterOfferId> masterOfferId = masterOfferIdRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(masterOfferId);
    }

    /**
     * {@code DELETE  /master-offer-ids/:id} : delete the "id" masterOfferId.
     *
     * @param id the id of the masterOfferId to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterOfferId(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MasterOfferId : {}", id);
        masterOfferIdRepository.deleteById(id);
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
