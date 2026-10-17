package com.spartified.connectrateplan.web.rest;

import com.spartified.connectrateplan.domain.ZoneLabelPriority;
import com.spartified.connectrateplan.repository.ZoneLabelPriorityRepository;
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
 * REST controller for managing {@link com.spartified.connectrateplan.domain.ZoneLabelPriority}.
 */
@RestController
@RequestMapping("/api/zone-label-priorities")
@Transactional(rollbackFor = Exception.class)
public class ZoneLabelPriorityResource {

    private static final Logger LOG = LoggerFactory.getLogger(ZoneLabelPriorityResource.class);

    private static final String ENTITY_NAME = "connectRateplanZoneLabelPriority";

    @Value("${jhipster.clientApp.name:connectRateplan}")
    private String applicationName;

    private final ZoneLabelPriorityRepository zoneLabelPriorityRepository;

    public ZoneLabelPriorityResource(ZoneLabelPriorityRepository zoneLabelPriorityRepository) {
        this.zoneLabelPriorityRepository = zoneLabelPriorityRepository;
    }

    /**
     * {@code POST  /zone-label-priorities} : Create a new zoneLabelPriority.
     *
     * @param zoneLabelPriority the zoneLabelPriority to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zoneLabelPriority, or with status {@code 400 (Bad Request)} if the zoneLabelPriority already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ZoneLabelPriority> createZoneLabelPriority(@Valid @RequestBody ZoneLabelPriority zoneLabelPriority)
        throws URISyntaxException {
        LOG.debug("REST request to save ZoneLabelPriority : {}", zoneLabelPriority);
        if (zoneLabelPriority.getId() != null) {
            throw new BadRequestAlertException("A new zoneLabelPriority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        zoneLabelPriority = zoneLabelPriorityRepository.save(zoneLabelPriority);
        return ResponseEntity.created(new URI("/api/zone-label-priorities/" + zoneLabelPriority.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, zoneLabelPriority.getId().toString()))
            .body(zoneLabelPriority);
    }

    /**
     * {@code PUT  /zone-label-priorities/:id} : Updates an existing zoneLabelPriority.
     *
     * @param id the id of the zoneLabelPriority to save.
     * @param zoneLabelPriority the zoneLabelPriority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zoneLabelPriority,
     * or with status {@code 400 (Bad Request)} if the zoneLabelPriority is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zoneLabelPriority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ZoneLabelPriority> updateZoneLabelPriority(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ZoneLabelPriority zoneLabelPriority
    ) throws URISyntaxException {
        LOG.debug("REST request to update ZoneLabelPriority : {}, {}", id, zoneLabelPriority);
        if (zoneLabelPriority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zoneLabelPriority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zoneLabelPriorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        zoneLabelPriority = zoneLabelPriorityRepository.save(zoneLabelPriority);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zoneLabelPriority.getId().toString()))
            .body(zoneLabelPriority);
    }

    /**
     * {@code PATCH  /zone-label-priorities/:id} : Partial updates given fields of an existing zoneLabelPriority, field will ignore if it is null
     *
     * @param id the id of the zoneLabelPriority to save.
     * @param zoneLabelPriority the zoneLabelPriority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zoneLabelPriority,
     * or with status {@code 400 (Bad Request)} if the zoneLabelPriority is not valid,
     * or with status {@code 404 (Not Found)} if the zoneLabelPriority is not found,
     * or with status {@code 500 (Internal Server Error)} if the zoneLabelPriority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ZoneLabelPriority> partialUpdateZoneLabelPriority(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ZoneLabelPriority zoneLabelPriority
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update ZoneLabelPriority : {}, {}", id, zoneLabelPriority);
        if (zoneLabelPriority.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zoneLabelPriority.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zoneLabelPriorityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ZoneLabelPriority> result = zoneLabelPriorityRepository
            .findById(zoneLabelPriority.getId())
            .map(existingZoneLabelPriority -> {
                updateIfPresent(existingZoneLabelPriority::setLabelType, zoneLabelPriority.getLabelType());
                updateIfPresent(existingZoneLabelPriority::setPriority, zoneLabelPriority.getPriority());
                updateIfPresent(existingZoneLabelPriority::setTenantId, zoneLabelPriority.getTenantId());
                updateIfPresent(existingZoneLabelPriority::setCreatedDate, zoneLabelPriority.getCreatedDate());
                updateIfPresent(existingZoneLabelPriority::setModifiedDate, zoneLabelPriority.getModifiedDate());

                return existingZoneLabelPriority;
            })
            .map(zoneLabelPriorityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, zoneLabelPriority.getId().toString())
        );
    }

    /**
     * {@code GET  /zone-label-priorities} : get all the Zone Label Priorities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Zone Label Priorities in body.
     */
    @GetMapping("")
    public List<ZoneLabelPriority> getAllZoneLabelPriorities() {
        LOG.debug("REST request to get all ZoneLabelPriorities");
        return zoneLabelPriorityRepository.findAll();
    }

    /**
     * {@code GET  /zone-label-priorities/:id} : get the "id" zoneLabelPriority.
     *
     * @param id the id of the zoneLabelPriority to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zoneLabelPriority, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ZoneLabelPriority> getZoneLabelPriority(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ZoneLabelPriority : {}", id);
        Optional<ZoneLabelPriority> zoneLabelPriority = zoneLabelPriorityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(zoneLabelPriority);
    }

    /**
     * {@code DELETE  /zone-label-priorities/:id} : delete the "id" zoneLabelPriority.
     *
     * @param id the id of the zoneLabelPriority to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZoneLabelPriority(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ZoneLabelPriority : {}", id);
        zoneLabelPriorityRepository.deleteById(id);
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
