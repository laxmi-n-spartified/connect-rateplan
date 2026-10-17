package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.Lifecycle;
import com.spartified.connectrateplan.repository.LifecycleRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.Lifecycle}.
 */
@RestController
@RequestMapping("/api/lifecycles")
@Transactional(rollbackFor = Exception.class)
public class LifecycleResource {

    private static final Logger LOG = LoggerFactory.getLogger(LifecycleResource.class);

    private static final String ENTITY_NAME = "connectRateplanLifecycle";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final LifecycleRepository lifecycleRepository;

    public LifecycleResource(LifecycleRepository lifecycleRepository) {
        this.lifecycleRepository = lifecycleRepository;
    }

    /**
     * {@code POST  /lifecycles} : Create a new lifecycle.
     *
     * @param lifecycle the lifecycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lifecycle, or with status {@code 400 (Bad Request)} if the lifecycle already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Lifecycle> createLifecycle(@Valid @RequestBody Lifecycle lifecycle) throws URISyntaxException {
        LOG.debug("REST request to save Lifecycle : {}", lifecycle);
        if (lifecycle.getId() != null) {
            throw new BadRequestAlertException("A new lifecycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lifecycle = lifecycleRepository.save(lifecycle);
        return ResponseEntity.created(new URI("/api/lifecycles/" + lifecycle.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, lifecycle.getId().toString()))
            .body(lifecycle);
    }

    /**
     * {@code PUT  /lifecycles/:id} : Updates an existing lifecycle.
     *
     * @param id the id of the lifecycle to save.
     * @param lifecycle the lifecycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lifecycle,
     * or with status {@code 400 (Bad Request)} if the lifecycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lifecycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lifecycle> updateLifecycle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Lifecycle lifecycle
    ) throws URISyntaxException {
        LOG.debug("REST request to update Lifecycle : {}, {}", id, lifecycle);
        if (lifecycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lifecycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lifecycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lifecycle = lifecycleRepository.save(lifecycle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lifecycle.getId().toString()))
            .body(lifecycle);
    }

    /**
     * {@code PATCH  /lifecycles/:id} : Partial updates given fields of an existing lifecycle, field will ignore if it is null
     *
     * @param id the id of the lifecycle to save.
     * @param lifecycle the lifecycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lifecycle,
     * or with status {@code 400 (Bad Request)} if the lifecycle is not valid,
     * or with status {@code 404 (Not Found)} if the lifecycle is not found,
     * or with status {@code 500 (Internal Server Error)} if the lifecycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lifecycle> partialUpdateLifecycle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Lifecycle lifecycle
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Lifecycle : {}, {}", id, lifecycle);
        if (lifecycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lifecycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lifecycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lifecycle> result = lifecycleRepository
            .findById(lifecycle.getId())
            .map(existingLifecycle -> {
                updateIfPresent(existingLifecycle::setName, lifecycle.getName());
                updateIfPresent(existingLifecycle::setCreatedDate, lifecycle.getCreatedDate());
                updateIfPresent(existingLifecycle::setModifiedDate, lifecycle.getModifiedDate());

                return existingLifecycle;
            })
            .map(lifecycleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lifecycle.getId().toString())
        );
    }

    /**
     * {@code GET  /lifecycles} : get all the Lifecycles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Lifecycles in body.
     */
    @GetMapping("")
    public List<Lifecycle> getAllLifecycles() {
        LOG.debug("REST request to get all Lifecycles");
        return lifecycleRepository.findAll();
    }

    /**
     * {@code GET  /lifecycles/:id} : get the "id" lifecycle.
     *
     * @param id the id of the lifecycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lifecycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lifecycle> getLifecycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Lifecycle : {}", id);
        Optional<Lifecycle> lifecycle = lifecycleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lifecycle);
    }

    /**
     * {@code DELETE  /lifecycles/:id} : delete the "id" lifecycle.
     *
     * @param id the id of the lifecycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLifecycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Lifecycle : {}", id);
        lifecycleRepository.deleteById(id);
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
