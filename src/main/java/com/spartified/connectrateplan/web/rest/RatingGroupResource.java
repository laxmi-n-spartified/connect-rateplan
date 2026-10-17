package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.RatingGroup;
import com.spartified.connectrateplan.repository.RatingGroupRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.RatingGroup}.
 */
@RestController
@RequestMapping("/api/rating-groups")
@Transactional(rollbackFor = Exception.class)
public class RatingGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(RatingGroupResource.class);

    private static final String ENTITY_NAME = "connectRateplanRatingGroup";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final RatingGroupRepository ratingGroupRepository;

    public RatingGroupResource(RatingGroupRepository ratingGroupRepository) {
        this.ratingGroupRepository = ratingGroupRepository;
    }

    /**
     * {@code POST  /rating-groups} : Create a new ratingGroup.
     *
     * @param ratingGroup the ratingGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ratingGroup, or with status {@code 400 (Bad Request)} if the ratingGroup already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RatingGroup> createRatingGroup(@Valid @RequestBody RatingGroup ratingGroup) throws URISyntaxException {
        LOG.debug("REST request to save RatingGroup : {}", ratingGroup);
        if (ratingGroup.getId() != null) {
            throw new BadRequestAlertException("A new ratingGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ratingGroup = ratingGroupRepository.save(ratingGroup);
        return ResponseEntity.created(new URI("/api/rating-groups/" + ratingGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ratingGroup.getId().toString()))
            .body(ratingGroup);
    }

    /**
     * {@code PUT  /rating-groups/:id} : Updates an existing ratingGroup.
     *
     * @param id the id of the ratingGroup to save.
     * @param ratingGroup the ratingGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingGroup,
     * or with status {@code 400 (Bad Request)} if the ratingGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ratingGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RatingGroup> updateRatingGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RatingGroup ratingGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to update RatingGroup : {}, {}", id, ratingGroup);
        if (ratingGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ratingGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ratingGroup = ratingGroupRepository.save(ratingGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ratingGroup.getId().toString()))
            .body(ratingGroup);
    }

    /**
     * {@code PATCH  /rating-groups/:id} : Partial updates given fields of an existing ratingGroup, field will ignore if it is null
     *
     * @param id the id of the ratingGroup to save.
     * @param ratingGroup the ratingGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingGroup,
     * or with status {@code 400 (Bad Request)} if the ratingGroup is not valid,
     * or with status {@code 404 (Not Found)} if the ratingGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the ratingGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RatingGroup> partialUpdateRatingGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RatingGroup ratingGroup
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update RatingGroup : {}, {}", id, ratingGroup);
        if (ratingGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ratingGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RatingGroup> result = ratingGroupRepository
            .findById(ratingGroup.getId())
            .map(existingRatingGroup -> {
                updateIfPresent(existingRatingGroup::setTenantId, ratingGroup.getTenantId());
                updateIfPresent(existingRatingGroup::setRgValue, ratingGroup.getRgValue());
                updateIfPresent(existingRatingGroup::setIsDefault, ratingGroup.getIsDefault());
                updateIfPresent(existingRatingGroup::setCreatedDate, ratingGroup.getCreatedDate());
                updateIfPresent(existingRatingGroup::setModifiedDate, ratingGroup.getModifiedDate());

                return existingRatingGroup;
            })
            .map(ratingGroupRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ratingGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /rating-groups} : get all the Rating Groups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Rating Groups in body.
     */
    @GetMapping("")
    public List<RatingGroup> getAllRatingGroups() {
        LOG.debug("REST request to get all RatingGroups");
        return ratingGroupRepository.findAll();
    }

    /**
     * {@code GET  /rating-groups/:id} : get the "id" ratingGroup.
     *
     * @param id the id of the ratingGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ratingGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RatingGroup> getRatingGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RatingGroup : {}", id);
        Optional<RatingGroup> ratingGroup = ratingGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ratingGroup);
    }

    /**
     * {@code DELETE  /rating-groups/:id} : delete the "id" ratingGroup.
     *
     * @param id the id of the ratingGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRatingGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RatingGroup : {}", id);
        ratingGroupRepository.deleteById(id);
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
