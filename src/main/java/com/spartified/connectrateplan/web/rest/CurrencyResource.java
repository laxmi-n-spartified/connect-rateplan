package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.Currency;
import com.spartified.connectrateplan.repository.CurrencyRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.Currency}.
 */
@RestController
@RequestMapping("/api/currencies")
@Transactional(rollbackFor = Exception.class)
public class CurrencyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "connectRateplanCurrency";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final CurrencyRepository currencyRepository;

    public CurrencyResource(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     * @param currency the currency to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currency, or with status {@code 400 (Bad Request)} if the currency already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) throws URISyntaxException {
        LOG.debug("REST request to save Currency : {}", currency);
        if (currency.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        currency = currencyRepository.save(currency);
        return ResponseEntity.created(new URI("/api/currencies/" + currency.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, currency.getId().toString()))
            .body(currency);
    }

    /**
     * {@code PUT  /currencies/:id} : Updates an existing currency.
     *
     * @param id the id of the currency to save.
     * @param currency the currency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currency,
     * or with status {@code 400 (Bad Request)} if the currency is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Currency currency
    ) throws URISyntaxException {
        LOG.debug("REST request to update Currency : {}, {}", id, currency);
        if (currency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        currency = currencyRepository.save(currency);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currency.getId().toString()))
            .body(currency);
    }

    /**
     * {@code PATCH  /currencies/:id} : Partial updates given fields of an existing currency, field will ignore if it is null
     *
     * @param id the id of the currency to save.
     * @param currency the currency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currency,
     * or with status {@code 400 (Bad Request)} if the currency is not valid,
     * or with status {@code 404 (Not Found)} if the currency is not found,
     * or with status {@code 500 (Internal Server Error)} if the currency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Currency> partialUpdateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Currency currency
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Currency : {}, {}", id, currency);
        if (currency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Currency> result = currencyRepository
            .findById(currency.getId())
            .map(existingCurrency -> {
                updateIfPresent(existingCurrency::setCurrencyName, currency.getCurrencyName());
                updateIfPresent(existingCurrency::setCurrencyCode, currency.getCurrencyCode());
                updateIfPresent(existingCurrency::setCreatedDate, currency.getCreatedDate());
                updateIfPresent(existingCurrency::setModifiedDate, currency.getModifiedDate());

                return existingCurrency;
            })
            .map(currencyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currency.getId().toString())
        );
    }

    /**
     * {@code GET  /currencies} : get all the Currencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Currencies in body.
     */
    @GetMapping("")
    public List<Currency> getAllCurrencies() {
        LOG.debug("REST request to get all Currencies");
        return currencyRepository.findAll();
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Currency : {}", id);
        Optional<Currency> currency = currencyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(currency);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currency.
     *
     * @param id the id of the currency to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Currency : {}", id);
        currencyRepository.deleteById(id);
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
