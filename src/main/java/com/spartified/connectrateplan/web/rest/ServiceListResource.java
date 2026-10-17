package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.ServiceList;
import com.spartified.connectrateplan.repository.ServiceListRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.ServiceList}.
 */
@RestController
@RequestMapping("/api/service-lists")
@Transactional(rollbackFor = Exception.class)
public class ServiceListResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceListResource.class);

    private static final String ENTITY_NAME = "connectRateplanServiceList";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final ServiceListRepository serviceListRepository;

    public ServiceListResource(ServiceListRepository serviceListRepository) {
        this.serviceListRepository = serviceListRepository;
    }

    /**
     * {@code POST  /service-lists} : Create a new serviceList.
     *
     * @param serviceList the serviceList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceList, or with status {@code 400 (Bad Request)} if the serviceList already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceList> createServiceList(@Valid @RequestBody ServiceList serviceList) throws URISyntaxException {
        LOG.debug("REST request to save ServiceList : {}", serviceList);
        if (serviceList.getId() != null) {
            throw new BadRequestAlertException("A new serviceList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceList = serviceListRepository.save(serviceList);
        return ResponseEntity.created(new URI("/api/service-lists/" + serviceList.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, serviceList.getId().toString()))
            .body(serviceList);
    }

    /**
     * {@code PUT  /service-lists/:id} : Updates an existing serviceList.
     *
     * @param id the id of the serviceList to save.
     * @param serviceList the serviceList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceList,
     * or with status {@code 400 (Bad Request)} if the serviceList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceList> updateServiceList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceList serviceList
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceList : {}, {}", id, serviceList);
        if (serviceList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceList = serviceListRepository.save(serviceList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceList.getId().toString()))
            .body(serviceList);
    }

    /**
     * {@code PATCH  /service-lists/:id} : Partial updates given fields of an existing serviceList, field will ignore if it is null
     *
     * @param id the id of the serviceList to save.
     * @param serviceList the serviceList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceList,
     * or with status {@code 400 (Bad Request)} if the serviceList is not valid,
     * or with status {@code 404 (Not Found)} if the serviceList is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceList> partialUpdateServiceList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceList serviceList
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ServiceList : {}, {}", id, serviceList);
        if (serviceList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceList> result = serviceListRepository
            .findById(serviceList.getId())
            .map(existingServiceList -> {
                updateIfPresent(existingServiceList::setTenantId, serviceList.getTenantId());
                updateIfPresent(existingServiceList::setServicetype, serviceList.getServicetype());
                updateIfPresent(existingServiceList::setCreatedDate, serviceList.getCreatedDate());
                updateIfPresent(existingServiceList::setModifiedDate, serviceList.getModifiedDate());

                return existingServiceList;
            })
            .map(serviceListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceList.getId().toString())
        );
    }

    /**
     * {@code GET  /service-lists} : get all the Service Lists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Service Lists in body.
     */
    @GetMapping("")
    public List<ServiceList> getAllServiceLists() {
        LOG.debug("REST request to get all ServiceLists");
        return serviceListRepository.findAll();
    }

    /**
     * {@code GET  /service-lists/:id} : get the "id" serviceList.
     *
     * @param id the id of the serviceList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceList> getServiceList(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceList : {}", id);
        Optional<ServiceList> serviceList = serviceListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceList);
    }

    /**
     * {@code DELETE  /service-lists/:id} : delete the "id" serviceList.
     *
     * @param id the id of the serviceList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceList(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceList : {}", id);
        serviceListRepository.deleteById(id);
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
