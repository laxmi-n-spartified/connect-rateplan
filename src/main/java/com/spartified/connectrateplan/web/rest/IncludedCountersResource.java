package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.IncludedCounters;
import com.spartified.connectrateplan.repository.IncludedCountersRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.IncludedCounters}.
 */
@RestController
@RequestMapping("/api/included-counters")
@Transactional(rollbackFor = Exception.class)
public class IncludedCountersResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncludedCountersResource.class);

    private static final String ENTITY_NAME = "connectRateplanIncludedCounters";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final IncludedCountersRepository includedCountersRepository;

    public IncludedCountersResource(IncludedCountersRepository includedCountersRepository) {
        this.includedCountersRepository = includedCountersRepository;
    }

    /**
     * {@code POST  /included-counters} : Create a new includedCounters.
     *
     * @param includedCounters the includedCounters to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new includedCounters, or with status {@code 400 (Bad Request)} if the includedCounters already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IncludedCounters> createIncludedCounters(@Valid @RequestBody IncludedCounters includedCounters)
        throws URISyntaxException {
        LOG.debug("REST request to save IncludedCounters : {}", includedCounters);
        if (includedCounters.getId() != null) {
            throw new BadRequestAlertException("A new includedCounters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        includedCounters = includedCountersRepository.save(includedCounters);
        return ResponseEntity.created(new URI("/api/included-counters/" + includedCounters.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, includedCounters.getId().toString()))
            .body(includedCounters);
    }

    /**
     * {@code PUT  /included-counters/:id} : Updates an existing includedCounters.
     *
     * @param id the id of the includedCounters to save.
     * @param includedCounters the includedCounters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated includedCounters,
     * or with status {@code 400 (Bad Request)} if the includedCounters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the includedCounters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncludedCounters> updateIncludedCounters(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IncludedCounters includedCounters
    ) throws URISyntaxException {
        LOG.debug("REST request to update IncludedCounters : {}, {}", id, includedCounters);
        if (includedCounters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, includedCounters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!includedCountersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        includedCounters = includedCountersRepository.save(includedCounters);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, includedCounters.getId().toString()))
            .body(includedCounters);
    }

    /**
     * {@code PATCH  /included-counters/:id} : Partial updates given fields of an existing includedCounters, field will ignore if it is null
     *
     * @param id the id of the includedCounters to save.
     * @param includedCounters the includedCounters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated includedCounters,
     * or with status {@code 400 (Bad Request)} if the includedCounters is not valid,
     * or with status {@code 404 (Not Found)} if the includedCounters is not found,
     * or with status {@code 500 (Internal Server Error)} if the includedCounters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IncludedCounters> partialUpdateIncludedCounters(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IncludedCounters includedCounters
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update IncludedCounters : {}, {}", id, includedCounters);
        if (includedCounters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, includedCounters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!includedCountersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IncludedCounters> result = includedCountersRepository
            .findById(includedCounters.getId())
            .map(existingIncludedCounters -> {
                updateIfPresent(existingIncludedCounters::setCounterData, includedCounters.getCounterData());
                updateIfPresent(existingIncludedCounters::setCounterSmsMo, includedCounters.getCounterSmsMo());
                updateIfPresent(existingIncludedCounters::setCounterSmsMt, includedCounters.getCounterSmsMt());
                updateIfPresent(existingIncludedCounters::setCounterVoiceMo, includedCounters.getCounterVoiceMo());
                updateIfPresent(existingIncludedCounters::setCounterVoiceMt, includedCounters.getCounterVoiceMt());
                updateIfPresent(existingIncludedCounters::setRolloverData, includedCounters.getRolloverData());
                updateIfPresent(existingIncludedCounters::setRolloverDataDuration, includedCounters.getRolloverDataDuration());
                updateIfPresent(existingIncludedCounters::setRolloverVoiceMo, includedCounters.getRolloverVoiceMo());
                updateIfPresent(existingIncludedCounters::setRolloverVoiceMoDuration, includedCounters.getRolloverVoiceMoDuration());
                updateIfPresent(existingIncludedCounters::setRolloverSmsMo, includedCounters.getRolloverSmsMo());
                updateIfPresent(existingIncludedCounters::setRolloverSmsMoDuration, includedCounters.getRolloverSmsMoDuration());
                updateIfPresent(existingIncludedCounters::setCounterDataUnit, includedCounters.getCounterDataUnit());
                updateIfPresent(existingIncludedCounters::setCounterSmsMoUnit, includedCounters.getCounterSmsMoUnit());
                updateIfPresent(existingIncludedCounters::setCounterSmsMtUnit, includedCounters.getCounterSmsMtUnit());
                updateIfPresent(existingIncludedCounters::setCounterVoiceMoUnit, includedCounters.getCounterVoiceMoUnit());
                updateIfPresent(existingIncludedCounters::setCounterVoiceMtUnit, includedCounters.getCounterVoiceMtUnit());
                updateIfPresent(existingIncludedCounters::setRenewOnQuotaExhaust, includedCounters.getRenewOnQuotaExhaust());
                updateIfPresent(existingIncludedCounters::setCreatedDate, includedCounters.getCreatedDate());
                updateIfPresent(existingIncludedCounters::setModifiedDate, includedCounters.getModifiedDate());

                return existingIncludedCounters;
            })
            .map(includedCountersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, includedCounters.getId().toString())
        );
    }

    /**
     * {@code GET  /included-counters} : get all the Included Counters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Included Counters in body.
     */
    @GetMapping("")
    public List<IncludedCounters> getAllIncludedCounterses() {
        LOG.debug("REST request to get all IncludedCounterses");
        return includedCountersRepository.findAll();
    }

    /**
     * {@code GET  /included-counters/:id} : get the "id" includedCounters.
     *
     * @param id the id of the includedCounters to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the includedCounters, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncludedCounters> getIncludedCounters(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IncludedCounters : {}", id);
        Optional<IncludedCounters> includedCounters = includedCountersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(includedCounters);
    }

    /**
     * {@code DELETE  /included-counters/:id} : delete the "id" includedCounters.
     *
     * @param id the id of the includedCounters to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncludedCounters(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IncludedCounters : {}", id);
        includedCountersRepository.deleteById(id);
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
