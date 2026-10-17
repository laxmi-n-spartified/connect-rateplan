package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.UnitType;
import com.spartified.connectrateplan.repository.UnitTypeRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.UnitType}.
 */
@RestController
@RequestMapping("/api/unit-types")
@Transactional(rollbackFor = Exception.class)
public class UnitTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(UnitTypeResource.class);

    private static final String ENTITY_NAME = "connectRateplanUnitType";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final UnitTypeRepository unitTypeRepository;

    public UnitTypeResource(UnitTypeRepository unitTypeRepository) {
        this.unitTypeRepository = unitTypeRepository;
    }

    /**
     * {@code POST  /unit-types} : Create a new unitType.
     *
     * @param unitType the unitType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitType, or with status {@code 400 (Bad Request)} if the unitType already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UnitType> createUnitType(@Valid @RequestBody UnitType unitType) throws URISyntaxException {
        LOG.debug("REST request to save UnitType : {}", unitType);
        if (unitType.getId() != null) {
            throw new BadRequestAlertException("A new unitType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        unitType = unitTypeRepository.save(unitType);
        return ResponseEntity.created(new URI("/api/unit-types/" + unitType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, unitType.getId().toString()))
            .body(unitType);
    }

    /**
     * {@code PUT  /unit-types/:id} : Updates an existing unitType.
     *
     * @param id the id of the unitType to save.
     * @param unitType the unitType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitType,
     * or with status {@code 400 (Bad Request)} if the unitType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnitType> updateUnitType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitType unitType
    ) throws URISyntaxException {
        LOG.debug("REST request to update UnitType : {}, {}", id, unitType);
        if (unitType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        unitType = unitTypeRepository.save(unitType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unitType.getId().toString()))
            .body(unitType);
    }

    /**
     * {@code PATCH  /unit-types/:id} : Partial updates given fields of an existing unitType, field will ignore if it is null
     *
     * @param id the id of the unitType to save.
     * @param unitType the unitType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitType,
     * or with status {@code 400 (Bad Request)} if the unitType is not valid,
     * or with status {@code 404 (Not Found)} if the unitType is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitType> partialUpdateUnitType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitType unitType
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update UnitType : {}, {}", id, unitType);
        if (unitType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitType> result = unitTypeRepository
            .findById(unitType.getId())
            .map(existingUnitType -> {
                updateIfPresent(existingUnitType::setName, unitType.getName());
                updateIfPresent(existingUnitType::setConversionFactor, unitType.getConversionFactor());
                updateIfPresent(existingUnitType::setLowestUnit, unitType.getLowestUnit());
                updateIfPresent(existingUnitType::setCreatedDate, unitType.getCreatedDate());
                updateIfPresent(existingUnitType::setModifiedDate, unitType.getModifiedDate());

                return existingUnitType;
            })
            .map(unitTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unitType.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-types} : get all the Unit Types.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Unit Types in body.
     */
    @GetMapping("")
    public List<UnitType> getAllUnitTypes() {
        LOG.debug("REST request to get all UnitTypes");
        return unitTypeRepository.findAll();
    }

    /**
     * {@code GET  /unit-types/:id} : get the "id" unitType.
     *
     * @param id the id of the unitType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnitType> getUnitType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UnitType : {}", id);
        Optional<UnitType> unitType = unitTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(unitType);
    }

    /**
     * {@code DELETE  /unit-types/:id} : delete the "id" unitType.
     *
     * @param id the id of the unitType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnitType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UnitType : {}", id);
        unitTypeRepository.deleteById(id);
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
