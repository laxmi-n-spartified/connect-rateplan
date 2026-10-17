package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.TadigCode;
import com.spartified.connectrateplan.repository.TadigCodeRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.TadigCode}.
 */
@RestController
@RequestMapping("/api/tadig-codes")
@Transactional(rollbackFor = Exception.class)
public class TadigCodeResource {

    private static final Logger LOG = LoggerFactory.getLogger(TadigCodeResource.class);

    private static final String ENTITY_NAME = "connectRateplanTadigCode";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final TadigCodeRepository tadigCodeRepository;

    public TadigCodeResource(TadigCodeRepository tadigCodeRepository) {
        this.tadigCodeRepository = tadigCodeRepository;
    }

    /**
     * {@code POST  /tadig-codes} : Create a new tadigCode.
     *
     * @param tadigCode the tadigCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tadigCode, or with status {@code 400 (Bad Request)} if the tadigCode already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TadigCode> createTadigCode(@Valid @RequestBody TadigCode tadigCode) throws URISyntaxException {
        LOG.debug("REST request to save TadigCode : {}", tadigCode);
        if (tadigCode.getId() != null) {
            throw new BadRequestAlertException("A new tadigCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tadigCode = tadigCodeRepository.save(tadigCode);
        return ResponseEntity.created(new URI("/api/tadig-codes/" + tadigCode.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tadigCode.getId().toString()))
            .body(tadigCode);
    }

    /**
     * {@code PUT  /tadig-codes/:id} : Updates an existing tadigCode.
     *
     * @param id the id of the tadigCode to save.
     * @param tadigCode the tadigCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tadigCode,
     * or with status {@code 400 (Bad Request)} if the tadigCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tadigCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TadigCode> updateTadigCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TadigCode tadigCode
    ) throws URISyntaxException {
        LOG.debug("REST request to update TadigCode : {}, {}", id, tadigCode);
        if (tadigCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tadigCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tadigCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tadigCode = tadigCodeRepository.save(tadigCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tadigCode.getId().toString()))
            .body(tadigCode);
    }

    /**
     * {@code PATCH  /tadig-codes/:id} : Partial updates given fields of an existing tadigCode, field will ignore if it is null
     *
     * @param id the id of the tadigCode to save.
     * @param tadigCode the tadigCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tadigCode,
     * or with status {@code 400 (Bad Request)} if the tadigCode is not valid,
     * or with status {@code 404 (Not Found)} if the tadigCode is not found,
     * or with status {@code 500 (Internal Server Error)} if the tadigCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TadigCode> partialUpdateTadigCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TadigCode tadigCode
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update TadigCode : {}, {}", id, tadigCode);
        if (tadigCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tadigCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tadigCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TadigCode> result = tadigCodeRepository
            .findById(tadigCode.getId())
            .map(existingTadigCode -> {
                updateIfPresent(existingTadigCode::setTadig, tadigCode.getTadig());
                updateIfPresent(existingTadigCode::setCreatedDate, tadigCode.getCreatedDate());
                updateIfPresent(existingTadigCode::setModifiedDate, tadigCode.getModifiedDate());

                return existingTadigCode;
            })
            .map(tadigCodeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tadigCode.getId().toString())
        );
    }

    /**
     * {@code GET  /tadig-codes} : get all the Tadig Codes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tadig Codes in body.
     */
    @GetMapping("")
    public List<TadigCode> getAllTadigCodes() {
        LOG.debug("REST request to get all TadigCodes");
        return tadigCodeRepository.findAll();
    }

    /**
     * {@code GET  /tadig-codes/:id} : get the "id" tadigCode.
     *
     * @param id the id of the tadigCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tadigCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TadigCode> getTadigCode(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TadigCode : {}", id);
        Optional<TadigCode> tadigCode = tadigCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tadigCode);
    }

    /**
     * {@code DELETE  /tadig-codes/:id} : delete the "id" tadigCode.
     *
     * @param id the id of the tadigCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTadigCode(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TadigCode : {}", id);
        tadigCodeRepository.deleteById(id);
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
