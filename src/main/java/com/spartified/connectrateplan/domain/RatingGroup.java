package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A RatingGroup.
 */
@Entity
@Table(name = "rating_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RatingGroup implements Serializable {

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
    @Column(name = "rg_value", length = 255)
    private String rgValue;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "ratingGroups")
    @JsonIgnoreProperties(value = { "lifecycle", "timeInterval", "ratingGroups", "rateplanLcStateModel" }, allowSetters = true)
    private Set<Rateplan> rateplans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RatingGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public RatingGroup tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRgValue() {
        return this.rgValue;
    }

    public RatingGroup rgValue(String rgValue) {
        this.setRgValue(rgValue);
        return this;
    }

    public void setRgValue(String rgValue) {
        this.rgValue = rgValue;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public RatingGroup isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public RatingGroup createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public RatingGroup modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<Rateplan> getRateplans() {
        return this.rateplans;
    }

    public void setRateplans(Set<Rateplan> rateplans) {
        if (this.rateplans != null) {
            this.rateplans.forEach(i -> i.removeRatingGroup(this));
        }
        if (rateplans != null) {
            rateplans.forEach(i -> i.addRatingGroup(this));
        }
        this.rateplans = rateplans;
    }

    public RatingGroup rateplans(Set<Rateplan> rateplans) {
        this.setRateplans(rateplans);
        return this;
    }

    public RatingGroup addRateplan(Rateplan rateplan) {
        this.rateplans.add(rateplan);
        rateplan.getRatingGroups().add(this);
        return this;
    }

    public RatingGroup removeRateplan(Rateplan rateplan) {
        this.rateplans.remove(rateplan);
        rateplan.getRatingGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RatingGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((RatingGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RatingGroup{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", rgValue='" + getRgValue() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
