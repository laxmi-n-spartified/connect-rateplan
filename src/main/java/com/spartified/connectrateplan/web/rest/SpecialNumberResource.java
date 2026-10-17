package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.SpecialNumber;
import com.spartified.connectrateplan.repository.SpecialNumberRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.SpecialNumber}.
 */
@RestController
@RequestMapping("/api/special-numbers")
@Transactional(rollbackFor = Exception.class)
public class SpecialNumberResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialNumberResource.class);

    private static final String ENTITY_NAME = "connectRateplanSpecialNumber";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final SpecialNumberRepository specialNumberRepository;

    public SpecialNumberResource(SpecialNumberRepository specialNumberRepository) {
        this.specialNumberRepository = specialNumberRepository;
    }

    /**
     * {@code POST  /special-numbers} : Create a new specialNumber.
     *
     * @param specialNumber the specialNumber to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialNumber, or with status {@code 400 (Bad Request)} if the specialNumber already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialNumber> createSpecialNumber(@Valid @RequestBody SpecialNumber specialNumber) throws URISyntaxException {
        LOG.debug("REST request to save SpecialNumber : {}", specialNumber);
        if (specialNumber.getId() != null) {
            throw new BadRequestAlertException("A new specialNumber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        specialNumber = specialNumberRepository.save(specialNumber);
        return ResponseEntity.created(new URI("/api/special-numbers/" + specialNumber.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, specialNumber.getId().toString()))
            .body(specialNumber);
    }

    /**
     * {@code PUT  /special-numbers/:id} : Updates an existing specialNumber.
     *
     * @param id the id of the specialNumber to save.
     * @param specialNumber the specialNumber to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialNumber,
     * or with status {@code 400 (Bad Request)} if the specialNumber is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialNumber couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialNumber> updateSpecialNumber(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialNumber specialNumber
    ) throws URISyntaxException {
        LOG.debug("REST request to update SpecialNumber : {}, {}", id, specialNumber);
        if (specialNumber.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialNumber.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialNumberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        specialNumber = specialNumberRepository.save(specialNumber);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialNumber.getId().toString()))
            .body(specialNumber);
    }

    /**
     * {@code PATCH  /special-numbers/:id} : Partial updates given fields of an existing specialNumber, field will ignore if it is null
     *
     * @param id the id of the specialNumber to save.
     * @param specialNumber the specialNumber to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialNumber,
     * or with status {@code 400 (Bad Request)} if the specialNumber is not valid,
     * or with status {@code 404 (Not Found)} if the specialNumber is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialNumber couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialNumber> partialUpdateSpecialNumber(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialNumber specialNumber
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update SpecialNumber : {}, {}", id, specialNumber);
        if (specialNumber.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialNumber.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialNumberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialNumber> result = specialNumberRepository
            .findById(specialNumber.getId())
            .map(existingSpecialNumber -> {
                updateIfPresent(existingSpecialNumber::setTenantId, specialNumber.getTenantId());
                updateIfPresent(existingSpecialNumber::setInputNum, specialNumber.getInputNum());
                updateIfPresent(existingSpecialNumber::setTranslatedNum, specialNumber.getTranslatedNum());
                updateIfPresent(existingSpecialNumber::setCreatedDate, specialNumber.getCreatedDate());
                updateIfPresent(existingSpecialNumber::setModifiedDate, specialNumber.getModifiedDate());

                return existingSpecialNumber;
            })
            .map(specialNumberRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, specialNumber.getId().toString())
        );
    }

    /**
     * {@code GET  /special-numbers} : get all the Special Numbers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Special Numbers in body.
     */
    @GetMapping("")
    public List<SpecialNumber> getAllSpecialNumbers() {
        LOG.debug("REST request to get all SpecialNumbers");
        return specialNumberRepository.findAll();
    }

    /**
     * {@code GET  /special-numbers/:id} : get the "id" specialNumber.
     *
     * @param id the id of the specialNumber to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialNumber, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialNumber> getSpecialNumber(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SpecialNumber : {}", id);
        Optional<SpecialNumber> specialNumber = specialNumberRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specialNumber);
    }

    /**
     * {@code DELETE  /special-numbers/:id} : delete the "id" specialNumber.
     *
     * @param id the id of the specialNumber to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialNumber(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SpecialNumber : {}", id);
        specialNumberRepository.deleteById(id);
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
