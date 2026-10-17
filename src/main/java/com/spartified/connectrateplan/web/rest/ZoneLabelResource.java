package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.ZoneLabel;
import com.spartified.connectrateplan.repository.ZoneLabelRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.ZoneLabel}.
 */
@RestController
@RequestMapping("/api/zone-labels")
@Transactional(rollbackFor = Exception.class)
public class ZoneLabelResource {

    private static final Logger LOG = LoggerFactory.getLogger(ZoneLabelResource.class);

    private static final String ENTITY_NAME = "connectRateplanZoneLabel";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final ZoneLabelRepository zoneLabelRepository;

    public ZoneLabelResource(ZoneLabelRepository zoneLabelRepository) {
        this.zoneLabelRepository = zoneLabelRepository;
    }

    /**
     * {@code POST  /zone-labels} : Create a new zoneLabel.
     *
     * @param zoneLabel the zoneLabel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zoneLabel, or with status {@code 400 (Bad Request)} if the zoneLabel already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ZoneLabel> createZoneLabel(@Valid @RequestBody ZoneLabel zoneLabel) throws URISyntaxException {
        LOG.debug("REST request to save ZoneLabel : {}", zoneLabel);
        if (zoneLabel.getId() != null) {
            throw new BadRequestAlertException("A new zoneLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        zoneLabel = zoneLabelRepository.save(zoneLabel);
        return ResponseEntity.created(new URI("/api/zone-labels/" + zoneLabel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, zoneLabel.getId().toString()))
            .body(zoneLabel);
    }

    /**
     * {@code PUT  /zone-labels/:id} : Updates an existing zoneLabel.
     *
     * @param id the id of the zoneLabel to save.
     * @param zoneLabel the zoneLabel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zoneLabel,
     * or with status {@code 400 (Bad Request)} if the zoneLabel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zoneLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ZoneLabel> updateZoneLabel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ZoneLabel zoneLabel
    ) throws URISyntaxException {
        LOG.debug("REST request to update ZoneLabel : {}, {}", id, zoneLabel);
        if (zoneLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zoneLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zoneLabelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        zoneLabel = zoneLabelRepository.save(zoneLabel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zoneLabel.getId().toString()))
            .body(zoneLabel);
    }

    /**
     * {@code PATCH  /zone-labels/:id} : Partial updates given fields of an existing zoneLabel, field will ignore if it is null
     *
     * @param id the id of the zoneLabel to save.
     * @param zoneLabel the zoneLabel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zoneLabel,
     * or with status {@code 400 (Bad Request)} if the zoneLabel is not valid,
     * or with status {@code 404 (Not Found)} if the zoneLabel is not found,
     * or with status {@code 500 (Internal Server Error)} if the zoneLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ZoneLabel> partialUpdateZoneLabel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ZoneLabel zoneLabel
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ZoneLabel : {}, {}", id, zoneLabel);
        if (zoneLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zoneLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zoneLabelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ZoneLabel> result = zoneLabelRepository
            .findById(zoneLabel.getId())
            .map(existingZoneLabel -> {
                updateIfPresent(existingZoneLabel::setTenantId, zoneLabel.getTenantId());
                updateIfPresent(existingZoneLabel::setName, zoneLabel.getName());
                updateIfPresent(existingZoneLabel::setDescription, zoneLabel.getDescription());
                updateIfPresent(existingZoneLabel::setIsRestOfWorld, zoneLabel.getIsRestOfWorld());
                updateIfPresent(existingZoneLabel::setStatus, zoneLabel.getStatus());
                updateIfPresent(existingZoneLabel::setCreatedDate, zoneLabel.getCreatedDate());
                updateIfPresent(existingZoneLabel::setModifiedDate, zoneLabel.getModifiedDate());

                return existingZoneLabel;
            })
            .map(zoneLabelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zoneLabel.getId().toString())
        );
    }

    /**
     * {@code GET  /zone-labels} : get all the Zone Labels.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Zone Labels in body.
     */
    @GetMapping("")
    public List<ZoneLabel> getAllZoneLabels(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all ZoneLabels");
        if (eagerload) {
            return zoneLabelRepository.findAllWithEagerRelationships();
        } else {
            return zoneLabelRepository.findAll();
        }
    }

    /**
     * {@code GET  /zone-labels/:id} : get the "id" zoneLabel.
     *
     * @param id the id of the zoneLabel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zoneLabel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ZoneLabel> getZoneLabel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ZoneLabel : {}", id);
        Optional<ZoneLabel> zoneLabel = zoneLabelRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(zoneLabel);
    }

    /**
     * {@code DELETE  /zone-labels/:id} : delete the "id" zoneLabel.
     *
     * @param id the id of the zoneLabel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZoneLabel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ZoneLabel : {}", id);
        zoneLabelRepository.deleteById(id);
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
