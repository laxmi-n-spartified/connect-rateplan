package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ZoneLabelPriority.
 */
@Entity
@Table(name = "zone_label_priority")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ZoneLabelPriority implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "label_type", length = 255)
    private String labelType;

    @Column(name = "priority")
    private Integer priority;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lifecycle", "timeInterval", "ratingGroups", "rateplanLcStateModel" }, allowSetters = true)
    private Rateplan rateplan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "networkIdentities" }, allowSetters = true)
    private ZoneLabel zoneLabel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ZoneLabelPriority id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabelType() {
        return this.labelType;
    }

    public ZoneLabelPriority labelType(String labelType) {
        this.setLabelType(labelType);
        return this;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public ZoneLabelPriority priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public ZoneLabelPriority tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ZoneLabelPriority createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public ZoneLabelPriority modifiedDate(Instant modifiedDate) {
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

    public ZoneLabelPriority rateplan(Rateplan rateplan) {
        this.setRateplan(rateplan);
        return this;
    }

    public ZoneLabel getZoneLabel() {
        return this.zoneLabel;
    }

    public void setZoneLabel(ZoneLabel zoneLabel) {
        this.zoneLabel = zoneLabel;
    }

    public ZoneLabelPriority zoneLabel(ZoneLabel zoneLabel) {
        this.setZoneLabel(zoneLabel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZoneLabelPriority)) {
            return false;
        }
        return getId() != null && getId().equals(((ZoneLabelPriority) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZoneLabelPriority{" +
            "id=" + getId() +
            ", labelType='" + getLabelType() + "'" +
            ", priority=" + getPriority() +
            ", tenantId='" + getTenantId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
