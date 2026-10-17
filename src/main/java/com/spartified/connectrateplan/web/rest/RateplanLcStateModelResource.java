package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.RateplanLcStateModel;
import com.spartified.connectrateplan.repository.RateplanLcStateModelRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.RateplanLcStateModel}.
 */
@RestController
@RequestMapping("/api/rateplan-lc-state-models")
@Transactional(rollbackFor = Exception.class)
public class RateplanLcStateModelResource {

    private static final Logger LOG = LoggerFactory.getLogger(RateplanLcStateModelResource.class);

    private static final String ENTITY_NAME = "connectRateplanRateplanLcStateModel";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final RateplanLcStateModelRepository rateplanLcStateModelRepository;

    public RateplanLcStateModelResource(RateplanLcStateModelRepository rateplanLcStateModelRepository) {
        this.rateplanLcStateModelRepository = rateplanLcStateModelRepository;
    }

    /**
     * {@code POST  /rateplan-lc-state-models} : Create a new rateplanLcStateModel.
     *
     * @param rateplanLcStateModel the rateplanLcStateModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rateplanLcStateModel, or with status {@code 400 (Bad Request)} if the rateplanLcStateModel already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RateplanLcStateModel> createRateplanLcStateModel(@Valid @RequestBody RateplanLcStateModel rateplanLcStateModel)
        throws URISyntaxException {
        LOG.debug("REST request to save RateplanLcStateModel : {}", rateplanLcStateModel);
        if (rateplanLcStateModel.getId() != null) {
            throw new BadRequestAlertException("A new rateplanLcStateModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rateplanLcStateModel = rateplanLcStateModelRepository.save(rateplanLcStateModel);
        return ResponseEntity.created(new URI("/api/rateplan-lc-state-models/" + rateplanLcStateModel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, rateplanLcStateModel.getId().toString()))
            .body(rateplanLcStateModel);
    }

    /**
     * {@code PUT  /rateplan-lc-state-models/:id} : Updates an existing rateplanLcStateModel.
     *
     * @param id the id of the rateplanLcStateModel to save.
     * @param rateplanLcStateModel the rateplanLcStateModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateplanLcStateModel,
     * or with status {@code 400 (Bad Request)} if the rateplanLcStateModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rateplanLcStateModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RateplanLcStateModel> updateRateplanLcStateModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RateplanLcStateModel rateplanLcStateModel
    ) throws URISyntaxException {
        LOG.debug("REST request to update RateplanLcStateModel : {}, {}", id, rateplanLcStateModel);
        if (rateplanLcStateModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rateplanLcStateModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateplanLcStateModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rateplanLcStateModel = rateplanLcStateModelRepository.save(rateplanLcStateModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateplanLcStateModel.getId().toString()))
            .body(rateplanLcStateModel);
    }

    /**
     * {@code PATCH  /rateplan-lc-state-models/:id} : Partial updates given fields of an existing rateplanLcStateModel, field will ignore if it is null
     *
     * @param id the id of the rateplanLcStateModel to save.
     * @param rateplanLcStateModel the rateplanLcStateModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateplanLcStateModel,
     * or with status {@code 400 (Bad Request)} if the rateplanLcStateModel is not valid,
     * or with status {@code 404 (Not Found)} if the rateplanLcStateModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the rateplanLcStateModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RateplanLcStateModel> partialUpdateRateplanLcStateModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RateplanLcStateModel rateplanLcStateModel
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update RateplanLcStateModel : {}, {}", id, rateplanLcStateModel);
        if (rateplanLcStateModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rateplanLcStateModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateplanLcStateModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RateplanLcStateModel> result = rateplanLcStateModelRepository
            .findById(rateplanLcStateModel.getId())
            .map(existingRateplanLcStateModel -> {
                updateIfPresent(existingRateplanLcStateModel::setRateplanName, rateplanLcStateModel.getRateplanName());
                updateIfPresent(existingRateplanLcStateModel::setTenantId, rateplanLcStateModel.getTenantId());
                updateIfPresent(existingRateplanLcStateModel::setCurrentLcState, rateplanLcStateModel.getCurrentLcState());
                updateIfPresent(existingRateplanLcStateModel::setCreatedDate, rateplanLcStateModel.getCreatedDate());
                updateIfPresent(existingRateplanLcStateModel::setModifiedDate, rateplanLcStateModel.getModifiedDate());

                return existingRateplanLcStateModel;
            })
            .map(rateplanLcStateModelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateplanLcStateModel.getId().toString())
        );
    }

    /**
     * {@code GET  /rateplan-lc-state-models} : get all the Rateplan Lc State Models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Rateplan Lc State Models in body.
     */
    @GetMapping("")
    public List<RateplanLcStateModel> getAllRateplanLcStateModels() {
        LOG.debug("REST request to get all RateplanLcStateModels");
        return rateplanLcStateModelRepository.findAll();
    }

    /**
     * {@code GET  /rateplan-lc-state-models/:id} : get the "id" rateplanLcStateModel.
     *
     * @param id the id of the rateplanLcStateModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rateplanLcStateModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RateplanLcStateModel> getRateplanLcStateModel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RateplanLcStateModel : {}", id);
        Optional<RateplanLcStateModel> rateplanLcStateModel = rateplanLcStateModelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rateplanLcStateModel);
    }

    /**
     * {@code DELETE  /rateplan-lc-state-models/:id} : delete the "id" rateplanLcStateModel.
     *
     * @param id the id of the rateplanLcStateModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRateplanLcStateModel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RateplanLcStateModel : {}", id);
        rateplanLcStateModelRepository.deleteById(id);
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
