package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.StateTransitions;
import com.spartified.connectrateplan.repository.StateTransitionsRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.StateTransitions}.
 */
@RestController
@RequestMapping("/api/state-transitions")
@Transactional(rollbackFor = Exception.class)
public class StateTransitionsResource {

    private static final Logger LOG = LoggerFactory.getLogger(StateTransitionsResource.class);

    private static final String ENTITY_NAME = "connectRateplanStateTransitions";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final StateTransitionsRepository stateTransitionsRepository;

    public StateTransitionsResource(StateTransitionsRepository stateTransitionsRepository) {
        this.stateTransitionsRepository = stateTransitionsRepository;
    }

    /**
     * {@code POST  /state-transitions} : Create a new stateTransitions.
     *
     * @param stateTransitions the stateTransitions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateTransitions, or with status {@code 400 (Bad Request)} if the stateTransitions already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StateTransitions> createStateTransitions(@Valid @RequestBody StateTransitions stateTransitions)
        throws URISyntaxException {
        LOG.debug("REST request to save StateTransitions : {}", stateTransitions);
        if (stateTransitions.getId() != null) {
            throw new BadRequestAlertException("A new stateTransitions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stateTransitions = stateTransitionsRepository.save(stateTransitions);
        return ResponseEntity.created(new URI("/api/state-transitions/" + stateTransitions.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, stateTransitions.getId().toString()))
            .body(stateTransitions);
    }

    /**
     * {@code PUT  /state-transitions/:id} : Updates an existing stateTransitions.
     *
     * @param id the id of the stateTransitions to save.
     * @param stateTransitions the stateTransitions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateTransitions,
     * or with status {@code 400 (Bad Request)} if the stateTransitions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateTransitions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StateTransitions> updateStateTransitions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StateTransitions stateTransitions
    ) throws URISyntaxException {
        LOG.debug("REST request to update StateTransitions : {}, {}", id, stateTransitions);
        if (stateTransitions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateTransitions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateTransitionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stateTransitions = stateTransitionsRepository.save(stateTransitions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stateTransitions.getId().toString()))
            .body(stateTransitions);
    }

    /**
     * {@code PATCH  /state-transitions/:id} : Partial updates given fields of an existing stateTransitions, field will ignore if it is null
     *
     * @param id the id of the stateTransitions to save.
     * @param stateTransitions the stateTransitions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateTransitions,
     * or with status {@code 400 (Bad Request)} if the stateTransitions is not valid,
     * or with status {@code 404 (Not Found)} if the stateTransitions is not found,
     * or with status {@code 500 (Internal Server Error)} if the stateTransitions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StateTransitions> partialUpdateStateTransitions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StateTransitions stateTransitions
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update StateTransitions : {}, {}", id, stateTransitions);
        if (stateTransitions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateTransitions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateTransitionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StateTransitions> result = stateTransitionsRepository
            .findById(stateTransitions.getId())
            .map(existingStateTransitions -> {
                updateIfPresent(existingStateTransitions::setName, stateTransitions.getName());
                updateIfPresent(existingStateTransitions::setFromState, stateTransitions.getFromState());
                updateIfPresent(existingStateTransitions::setToState, stateTransitions.getToState());
                updateIfPresent(existingStateTransitions::setIsAllowed, stateTransitions.getIsAllowed());
                updateIfPresent(existingStateTransitions::setCreatedDate, stateTransitions.getCreatedDate());
                updateIfPresent(existingStateTransitions::setModifiedDate, stateTransitions.getModifiedDate());

                return existingStateTransitions;
            })
            .map(stateTransitionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stateTransitions.getId().toString())
        );
    }

    /**
     * {@code GET  /state-transitions} : get all the State Transitions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of State Transitions in body.
     */
    @GetMapping("")
    public List<StateTransitions> getAllStateTransitionses() {
        LOG.debug("REST request to get all StateTransitionses");
        return stateTransitionsRepository.findAll();
    }

    /**
     * {@code GET  /state-transitions/:id} : get the "id" stateTransitions.
     *
     * @param id the id of the stateTransitions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateTransitions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StateTransitions> getStateTransitions(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StateTransitions : {}", id);
        Optional<StateTransitions> stateTransitions = stateTransitionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stateTransitions);
    }

    /**
     * {@code DELETE  /state-transitions/:id} : delete the "id" stateTransitions.
     *
     * @param id the id of the stateTransitions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStateTransitions(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StateTransitions : {}", id);
        stateTransitionsRepository.deleteById(id);
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
