package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.NetworkIdentity;
import com.spartified.connectrateplan.repository.NetworkIdentityRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.NetworkIdentity}.
 */
@RestController
@RequestMapping("/api/network-identities")
@Transactional(rollbackFor = Exception.class)
public class NetworkIdentityResource {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkIdentityResource.class);

    private static final String ENTITY_NAME = "connectRateplanNetworkIdentity";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final NetworkIdentityRepository networkIdentityRepository;

    public NetworkIdentityResource(NetworkIdentityRepository networkIdentityRepository) {
        this.networkIdentityRepository = networkIdentityRepository;
    }

    /**
     * {@code POST  /network-identities} : Create a new networkIdentity.
     *
     * @param networkIdentity the networkIdentity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new networkIdentity, or with status {@code 400 (Bad Request)} if the networkIdentity already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NetworkIdentity> createNetworkIdentity(@Valid @RequestBody NetworkIdentity networkIdentity)
        throws URISyntaxException {
        LOG.debug("REST request to save NetworkIdentity : {}", networkIdentity);
        if (networkIdentity.getId() != null) {
            throw new BadRequestAlertException("A new networkIdentity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        networkIdentity = networkIdentityRepository.save(networkIdentity);
        return ResponseEntity.created(new URI("/api/network-identities/" + networkIdentity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, networkIdentity.getId().toString()))
            .body(networkIdentity);
    }

    /**
     * {@code PUT  /network-identities/:id} : Updates an existing networkIdentity.
     *
     * @param id the id of the networkIdentity to save.
     * @param networkIdentity the networkIdentity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated networkIdentity,
     * or with status {@code 400 (Bad Request)} if the networkIdentity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the networkIdentity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NetworkIdentity> updateNetworkIdentity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NetworkIdentity networkIdentity
    ) throws URISyntaxException {
        LOG.debug("REST request to update NetworkIdentity : {}, {}", id, networkIdentity);
        if (networkIdentity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, networkIdentity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!networkIdentityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        networkIdentity = networkIdentityRepository.save(networkIdentity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, networkIdentity.getId().toString()))
            .body(networkIdentity);
    }

    /**
     * {@code PATCH  /network-identities/:id} : Partial updates given fields of an existing networkIdentity, field will ignore if it is null
     *
     * @param id the id of the networkIdentity to save.
     * @param networkIdentity the networkIdentity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated networkIdentity,
     * or with status {@code 400 (Bad Request)} if the networkIdentity is not valid,
     * or with status {@code 404 (Not Found)} if the networkIdentity is not found,
     * or with status {@code 500 (Internal Server Error)} if the networkIdentity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NetworkIdentity> partialUpdateNetworkIdentity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NetworkIdentity networkIdentity
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update NetworkIdentity : {}, {}", id, networkIdentity);
        if (networkIdentity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, networkIdentity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!networkIdentityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NetworkIdentity> result = networkIdentityRepository
            .findById(networkIdentity.getId())
            .map(existingNetworkIdentity -> {
                updateIfPresent(existingNetworkIdentity::setMcc, networkIdentity.getMcc());
                updateIfPresent(existingNetworkIdentity::setMnc, networkIdentity.getMnc());
                updateIfPresent(existingNetworkIdentity::setOperator, networkIdentity.getOperator());
                updateIfPresent(existingNetworkIdentity::setCreatedDate, networkIdentity.getCreatedDate());
                updateIfPresent(existingNetworkIdentity::setModifiedDate, networkIdentity.getModifiedDate());

                return existingNetworkIdentity;
            })
            .map(networkIdentityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, networkIdentity.getId().toString())
        );
    }

    /**
     * {@code GET  /network-identities} : get all the Network Identities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Network Identities in body.
     */
    @GetMapping("")
    public List<NetworkIdentity> getAllNetworkIdentities() {
        LOG.debug("REST request to get all NetworkIdentities");
        return networkIdentityRepository.findAll();
    }

    /**
     * {@code GET  /network-identities/:id} : get the "id" networkIdentity.
     *
     * @param id the id of the networkIdentity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the networkIdentity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NetworkIdentity> getNetworkIdentity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NetworkIdentity : {}", id);
        Optional<NetworkIdentity> networkIdentity = networkIdentityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(networkIdentity);
    }

    /**
     * {@code DELETE  /network-identities/:id} : delete the "id" networkIdentity.
     *
     * @param id the id of the networkIdentity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNetworkIdentity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NetworkIdentity : {}", id);
        networkIdentityRepository.deleteById(id);
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
