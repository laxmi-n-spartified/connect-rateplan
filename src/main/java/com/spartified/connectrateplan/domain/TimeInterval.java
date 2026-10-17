package com.spartified.connectrateplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A TimeInterval.
 */
@Entity
@Table(name = "time_interval")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeInterval implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "is_assigned_rateplan")
    private Boolean isAssignedRateplan;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeInterval id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TimeInterval tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return this.name;
    }

    public TimeInterval name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public TimeInterval lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getIsAssignedRateplan() {
        return this.isAssignedRateplan;
    }

    public TimeInterval isAssignedRateplan(Boolean isAssignedRateplan) {
        this.setIsAssignedRateplan(isAssignedRateplan);
        return this;
    }

    public void setIsAssignedRateplan(Boolean isAssignedRateplan) {
        this.isAssignedRateplan = isAssignedRateplan;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public TimeInterval createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public TimeInterval modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeInterval)) {
            return false;
        }
        return getId() != null && getId().equals(((TimeInterval) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeInterval{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", name='" + getName() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", isAssignedRateplan='" + getIsAssignedRateplan() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
