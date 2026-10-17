package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.TimeInterval;
import com.spartified.connectrateplan.repository.TimeIntervalRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.TimeInterval}.
 */
@RestController
@RequestMapping("/api/time-intervals")
@Transactional(rollbackFor = Exception.class)
public class TimeIntervalResource {

    private static final Logger LOG = LoggerFactory.getLogger(TimeIntervalResource.class);

    private static final String ENTITY_NAME = "connectRateplanTimeInterval";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final TimeIntervalRepository timeIntervalRepository;

    public TimeIntervalResource(TimeIntervalRepository timeIntervalRepository) {
        this.timeIntervalRepository = timeIntervalRepository;
    }

    /**
     * {@code POST  /time-intervals} : Create a new timeInterval.
     *
     * @param timeInterval the timeInterval to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeInterval, or with status {@code 400 (Bad Request)} if the timeInterval already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimeInterval> createTimeInterval(@Valid @RequestBody TimeInterval timeInterval) throws URISyntaxException {
        LOG.debug("REST request to save TimeInterval : {}", timeInterval);
        if (timeInterval.getId() != null) {
            throw new BadRequestAlertException("A new timeInterval cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timeInterval = timeIntervalRepository.save(timeInterval);
        return ResponseEntity.created(new URI("/api/time-intervals/" + timeInterval.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, timeInterval.getId().toString()))
            .body(timeInterval);
    }

    /**
     * {@code PUT  /time-intervals/:id} : Updates an existing timeInterval.
     *
     * @param id the id of the timeInterval to save.
     * @param timeInterval the timeInterval to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeInterval,
     * or with status {@code 400 (Bad Request)} if the timeInterval is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeInterval couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimeInterval> updateTimeInterval(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimeInterval timeInterval
    ) throws URISyntaxException {
        LOG.debug("REST request to update TimeInterval : {}, {}", id, timeInterval);
        if (timeInterval.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeInterval.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeIntervalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timeInterval = timeIntervalRepository.save(timeInterval);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeInterval.getId().toString()))
            .body(timeInterval);
    }

    /**
     * {@code PATCH  /time-intervals/:id} : Partial updates given fields of an existing timeInterval, field will ignore if it is null
     *
     * @param id the id of the timeInterval to save.
     * @param timeInterval the timeInterval to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeInterval,
     * or with status {@code 400 (Bad Request)} if the timeInterval is not valid,
     * or with status {@code 404 (Not Found)} if the timeInterval is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeInterval couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimeInterval> partialUpdateTimeInterval(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimeInterval timeInterval
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TimeInterval : {}, {}", id, timeInterval);
        if (timeInterval.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeInterval.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeIntervalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeInterval> result = timeIntervalRepository
            .findById(timeInterval.getId())
            .map(existingTimeInterval -> {
                updateIfPresent(existingTimeInterval::setTenantId, timeInterval.getTenantId());
                updateIfPresent(existingTimeInterval::setName, timeInterval.getName());
                updateIfPresent(existingTimeInterval::setLastUpdated, timeInterval.getLastUpdated());
                updateIfPresent(existingTimeInterval::setIsAssignedRateplan, timeInterval.getIsAssignedRateplan());
                updateIfPresent(existingTimeInterval::setCreatedDate, timeInterval.getCreatedDate());
                updateIfPresent(existingTimeInterval::setModifiedDate, timeInterval.getModifiedDate());

                return existingTimeInterval;
            })
            .map(timeIntervalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeInterval.getId().toString())
        );
    }

    /**
     * {@code GET  /time-intervals} : get all the Time Intervals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Time Intervals in body.
     */
    @GetMapping("")
    public List<TimeInterval> getAllTimeIntervals() {
        LOG.debug("REST request to get all TimeIntervals");
        return timeIntervalRepository.findAll();
    }

    /**
     * {@code GET  /time-intervals/:id} : get the "id" timeInterval.
     *
     * @param id the id of the timeInterval to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeInterval, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimeInterval> getTimeInterval(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TimeInterval : {}", id);
        Optional<TimeInterval> timeInterval = timeIntervalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(timeInterval);
    }

    /**
     * {@code DELETE  /time-intervals/:id} : delete the "id" timeInterval.
     *
     * @param id the id of the timeInterval to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeInterval(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TimeInterval : {}", id);
        timeIntervalRepository.deleteById(id);
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
