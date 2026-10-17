package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A RateplanLcStateModel.
 */
@Entity
@Table(name = "rateplan_lc_state_model")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RateplanLcStateModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "rateplan_name", length = 255)
    private String rateplanName;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "current_lc_state", length = 255)
    private String currentLcState;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @JsonIgnoreProperties(value = { "lifecycle", "timeInterval", "ratingGroups", "rateplanLcStateModel" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Rateplan rateplan;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lifecycle lifecycle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RateplanLcStateModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRateplanName() {
        return this.rateplanName;
    }

    public RateplanLcStateModel rateplanName(String rateplanName) {
        this.setRateplanName(rateplanName);
        return this;
    }

    public void setRateplanName(String rateplanName) {
        this.rateplanName = rateplanName;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public RateplanLcStateModel tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCurrentLcState() {
        return this.currentLcState;
    }

    public RateplanLcStateModel currentLcState(String currentLcState) {
        this.setCurrentLcState(currentLcState);
        return this;
    }

    public void setCurrentLcState(String currentLcState) {
        this.currentLcState = currentLcState;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public RateplanLcStateModel createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public RateplanLcStateModel modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Rateplan getRateplan() {
        return this.rateplan;
    }

    public void setRateplan(Rateplan rateplan) {
        this.rateplan = rateplan;
    }

    public RateplanLcStateModel rateplan(Rateplan rateplan) {
        this.setRateplan(rateplan);
        return this;
    }

    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public RateplanLcStateModel lifecycle(Lifecycle lifecycle) {
        this.setLifecycle(lifecycle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RateplanLcStateModel)) {
            return false;
        }
        return getId() != null && getId().equals(((RateplanLcStateModel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RateplanLcStateModel{" +
            "id=" + getId() +
            ", rateplanName='" + getRateplanName() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", currentLcState='" + getCurrentLcState() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
