package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.SgsnIpList;
import com.spartified.connectrateplan.repository.SgsnIpListRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.SgsnIpList}.
 */
@RestController
@RequestMapping("/api/sgsn-ip-lists")
@Transactional(rollbackFor = Exception.class)
public class SgsnIpListResource {

    private static final Logger LOG = LoggerFactory.getLogger(SgsnIpListResource.class);

    private static final String ENTITY_NAME = "connectRateplanSgsnIpList";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final SgsnIpListRepository sgsnIpListRepository;

    public SgsnIpListResource(SgsnIpListRepository sgsnIpListRepository) {
        this.sgsnIpListRepository = sgsnIpListRepository;
    }

    /**
     * {@code POST  /sgsn-ip-lists} : Create a new sgsnIpList.
     *
     * @param sgsnIpList the sgsnIpList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sgsnIpList, or with status {@code 400 (Bad Request)} if the sgsnIpList already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SgsnIpList> createSgsnIpList(@Valid @RequestBody SgsnIpList sgsnIpList) throws URISyntaxException {
        LOG.debug("REST request to save SgsnIpList : {}", sgsnIpList);
        if (sgsnIpList.getId() != null) {
            throw new BadRequestAlertException("A new sgsnIpList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sgsnIpList = sgsnIpListRepository.save(sgsnIpList);
        return ResponseEntity.created(new URI("/api/sgsn-ip-lists/" + sgsnIpList.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sgsnIpList.getId().toString()))
            .body(sgsnIpList);
    }

    /**
     * {@code PUT  /sgsn-ip-lists/:id} : Updates an existing sgsnIpList.
     *
     * @param id the id of the sgsnIpList to save.
     * @param sgsnIpList the sgsnIpList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sgsnIpList,
     * or with status {@code 400 (Bad Request)} if the sgsnIpList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sgsnIpList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SgsnIpList> updateSgsnIpList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SgsnIpList sgsnIpList
    ) throws URISyntaxException {
        LOG.debug("REST request to update SgsnIpList : {}, {}", id, sgsnIpList);
        if (sgsnIpList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sgsnIpList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sgsnIpListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sgsnIpList = sgsnIpListRepository.save(sgsnIpList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sgsnIpList.getId().toString()))
            .body(sgsnIpList);
    }

    /**
     * {@code PATCH  /sgsn-ip-lists/:id} : Partial updates given fields of an existing sgsnIpList, field will ignore if it is null
     *
     * @param id the id of the sgsnIpList to save.
     * @param sgsnIpList the sgsnIpList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sgsnIpList,
     * or with status {@code 400 (Bad Request)} if the sgsnIpList is not valid,
     * or with status {@code 404 (Not Found)} if the sgsnIpList is not found,
     * or with status {@code 500 (Internal Server Error)} if the sgsnIpList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SgsnIpList> partialUpdateSgsnIpList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SgsnIpList sgsnIpList
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update SgsnIpList : {}, {}", id, sgsnIpList);
        if (sgsnIpList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sgsnIpList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sgsnIpListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SgsnIpList> result = sgsnIpListRepository
            .findById(sgsnIpList.getId())
            .map(existingSgsnIpList -> {
                updateIfPresent(existingSgsnIpList::setSgsnIp, sgsnIpList.getSgsnIp());
                updateIfPresent(existingSgsnIpList::setTenantId, sgsnIpList.getTenantId());
                updateIfPresent(existingSgsnIpList::setCreatedDate, sgsnIpList.getCreatedDate());
                updateIfPresent(existingSgsnIpList::setModifiedDate, sgsnIpList.getModifiedDate());

                return existingSgsnIpList;
            })
            .map(sgsnIpListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sgsnIpList.getId().toString())
        );
    }

    /**
     * {@code GET  /sgsn-ip-lists} : get all the Sgsn Ip Lists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Sgsn Ip Lists in body.
     */
    @GetMapping("")
    public List<SgsnIpList> getAllSgsnIpLists() {
        LOG.debug("REST request to get all SgsnIpLists");
        return sgsnIpListRepository.findAll();
    }

    /**
     * {@code GET  /sgsn-ip-lists/:id} : get the "id" sgsnIpList.
     *
     * @param id the id of the sgsnIpList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sgsnIpList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SgsnIpList> getSgsnIpList(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SgsnIpList : {}", id);
        Optional<SgsnIpList> sgsnIpList = sgsnIpListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sgsnIpList);
    }

    /**
     * {@code DELETE  /sgsn-ip-lists/:id} : delete the "id" sgsnIpList.
     *
     * @param id the id of the sgsnIpList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSgsnIpList(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SgsnIpList : {}", id);
        sgsnIpListRepository.deleteById(id);
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
