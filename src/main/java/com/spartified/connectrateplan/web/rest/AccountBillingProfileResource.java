package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.AccountBillingProfile;
import com.spartified.connectrateplan.repository.AccountBillingProfileRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.AccountBillingProfile}.
 */
@RestController
@RequestMapping("/api/account-billing-profiles")
@Transactional(rollbackFor = Exception.class)
public class AccountBillingProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountBillingProfileResource.class);

    private static final String ENTITY_NAME = "connectRateplanAccountBillingProfile";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final AccountBillingProfileRepository accountBillingProfileRepository;

    public AccountBillingProfileResource(AccountBillingProfileRepository accountBillingProfileRepository) {
        this.accountBillingProfileRepository = accountBillingProfileRepository;
    }

    /**
     * {@code POST  /account-billing-profiles} : Create a new accountBillingProfile.
     *
     * @param accountBillingProfile the accountBillingProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountBillingProfile, or with status {@code 400 (Bad Request)} if the accountBillingProfile already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountBillingProfile> createAccountBillingProfile(
        @Valid @RequestBody AccountBillingProfile accountBillingProfile
    ) throws URISyntaxException {
        LOG.debug("REST request to save AccountBillingProfile : {}", accountBillingProfile);
        if (accountBillingProfile.getId() != null) {
            throw new BadRequestAlertException("A new accountBillingProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accountBillingProfile = accountBillingProfileRepository.save(accountBillingProfile);
        return ResponseEntity.created(new URI("/api/account-billing-profiles/" + accountBillingProfile.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, accountBillingProfile.getId().toString()))
            .body(accountBillingProfile);
    }

    /**
     * {@code PUT  /account-billing-profiles/:id} : Updates an existing accountBillingProfile.
     *
     * @param id the id of the accountBillingProfile to save.
     * @param accountBillingProfile the accountBillingProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBillingProfile,
     * or with status {@code 400 (Bad Request)} if the accountBillingProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountBillingProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountBillingProfile> updateAccountBillingProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountBillingProfile accountBillingProfile
    ) throws URISyntaxException {
        LOG.debug("REST request to update AccountBillingProfile : {}, {}", id, accountBillingProfile);
        if (accountBillingProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBillingProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBillingProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accountBillingProfile = accountBillingProfileRepository.save(accountBillingProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountBillingProfile.getId().toString()))
            .body(accountBillingProfile);
    }

    /**
     * {@code PATCH  /account-billing-profiles/:id} : Partial updates given fields of an existing accountBillingProfile, field will ignore if it is null
     *
     * @param id the id of the accountBillingProfile to save.
     * @param accountBillingProfile the accountBillingProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBillingProfile,
     * or with status {@code 400 (Bad Request)} if the accountBillingProfile is not valid,
     * or with status {@code 404 (Not Found)} if the accountBillingProfile is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountBillingProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountBillingProfile> partialUpdateAccountBillingProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountBillingProfile accountBillingProfile
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update AccountBillingProfile : {}, {}", id, accountBillingProfile);
        if (accountBillingProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBillingProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBillingProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountBillingProfile> result = accountBillingProfileRepository
            .findById(accountBillingProfile.getId())
            .map(existingAccountBillingProfile -> {
                updateIfPresent(existingAccountBillingProfile::setTenantId, accountBillingProfile.getTenantId());
                updateIfPresent(existingAccountBillingProfile::setMrcChargeType, accountBillingProfile.getMrcChargeType());
                updateIfPresent(existingAccountBillingProfile::setRatingType, accountBillingProfile.getRatingType());
                updateIfPresent(existingAccountBillingProfile::setCreditLimit, accountBillingProfile.getCreditLimit());
                updateIfPresent(existingAccountBillingProfile::setMrcRatingType, accountBillingProfile.getMrcRatingType());
                updateIfPresent(existingAccountBillingProfile::setBillable, accountBillingProfile.getBillable());
                updateIfPresent(existingAccountBillingProfile::setFanId, accountBillingProfile.getFanId());
                updateIfPresent(existingAccountBillingProfile::setExtId, accountBillingProfile.getExtId());
                updateIfPresent(existingAccountBillingProfile::setAccountPrefix, accountBillingProfile.getAccountPrefix());
                updateIfPresent(existingAccountBillingProfile::setSaartId, accountBillingProfile.getSaartId());
                updateIfPresent(existingAccountBillingProfile::setCreatedDate, accountBillingProfile.getCreatedDate());
                updateIfPresent(existingAccountBillingProfile::setModifiedDate, accountBillingProfile.getModifiedDate());

                return existingAccountBillingProfile;
            })
            .map(accountBillingProfileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accountBillingProfile.getId().toString())
        );
    }

    /**
     * {@code GET  /account-billing-profiles} : get all the Account Billing Profiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Account Billing Profiles in body.
     */
    @GetMapping("")
    public List<AccountBillingProfile> getAllAccountBillingProfiles() {
        LOG.debug("REST request to get all AccountBillingProfiles");
        return accountBillingProfileRepository.findAll();
    }

    /**
     * {@code GET  /account-billing-profiles/:id} : get the "id" accountBillingProfile.
     *
     * @param id the id of the accountBillingProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountBillingProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountBillingProfile> getAccountBillingProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AccountBillingProfile : {}", id);
        Optional<AccountBillingProfile> accountBillingProfile = accountBillingProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountBillingProfile);
    }

    /**
     * {@code DELETE  /account-billing-profiles/:id} : delete the "id" accountBillingProfile.
     *
     * @param id the id of the accountBillingProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountBillingProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AccountBillingProfile : {}", id);
        accountBillingProfileRepository.deleteById(id);
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
