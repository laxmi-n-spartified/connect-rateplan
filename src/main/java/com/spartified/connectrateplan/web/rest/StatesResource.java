package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.States;
import com.spartified.connectrateplan.repository.StatesRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.States}.
 */
@RestController
@RequestMapping("/api/states")
@Transactional(rollbackFor = Exception.class)
public class StatesResource {

    private static final Logger LOG = LoggerFactory.getLogger(StatesResource.class);

    private static final String ENTITY_NAME = "connectRateplanStates";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final StatesRepository statesRepository;

    public StatesResource(StatesRepository statesRepository) {
        this.statesRepository = statesRepository;
    }

    /**
     * {@code POST  /states} : Create a new states.
     *
     * @param states the states to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new states, or with status {@code 400 (Bad Request)} if the states already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<States> createStates(@Valid @RequestBody States states) throws URISyntaxException {
        LOG.debug("REST request to save States : {}", states);
        if (states.getId() != null) {
            throw new BadRequestAlertException("A new states cannot already have an ID", ENTITY_NAME, "idexists");
        }
        states = statesRepository.save(states);
        return ResponseEntity.created(new URI("/api/states/" + states.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, states.getId().toString()))
            .body(states);
    }

    /**
     * {@code PUT  /states/:id} : Updates an existing states.
     *
     * @param id the id of the states to save.
     * @param states the states to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated states,
     * or with status {@code 400 (Bad Request)} if the states is not valid,
     * or with status {@code 500 (Internal Server Error)} if the states couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<States> updateStates(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody States states
    ) throws URISyntaxException {
        LOG.debug("REST request to update States : {}, {}", id, states);
        if (states.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, states.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        states = statesRepository.save(states);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, states.getId().toString()))
            .body(states);
    }

    /**
     * {@code PATCH  /states/:id} : Partial updates given fields of an existing states, field will ignore if it is null
     *
     * @param id the id of the states to save.
     * @param states the states to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated states,
     * or with status {@code 400 (Bad Request)} if the states is not valid,
     * or with status {@code 404 (Not Found)} if the states is not found,
     * or with status {@code 500 (Internal Server Error)} if the states couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<States> partialUpdateStates(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody States states
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update States : {}, {}", id, states);
        if (states.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, states.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<States> result = statesRepository
            .findById(states.getId())
            .map(existingStates -> {
                updateIfPresent(existingStates::setLcStateName, states.getLcStateName());
                updateIfPresent(existingStates::setCreatedDate, states.getCreatedDate());
                updateIfPresent(existingStates::setModifiedDate, states.getModifiedDate());

                return existingStates;
            })
            .map(statesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, states.getId().toString())
        );
    }

    /**
     * {@code GET  /states} : get all the States.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of States in body.
     */
    @GetMapping("")
    public List<States> getAllStateses() {
        LOG.debug("REST request to get all Stateses");
        return statesRepository.findAll();
    }

    /**
     * {@code GET  /states/:id} : get the "id" states.
     *
     * @param id the id of the states to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the states, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<States> getStates(@PathVariable("id") Long id) {
        LOG.debug("REST request to get States : {}", id);
        Optional<States> states = statesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(states);
    }

    /**
     * {@code DELETE  /states/:id} : delete the "id" states.
     *
     * @param id the id of the states to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStates(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete States : {}", id);
        statesRepository.deleteById(id);
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
