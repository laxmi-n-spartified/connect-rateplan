package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SpecialNumber.
 */
@Entity
@Table(name = "special_number")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialNumber implements Serializable {

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

    @Column(name = "input_num")
    private Long inputNum;

    @Column(name = "translated_num")
    private Long translatedNum;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "networkIdentities" }, allowSetters = true)
    private ZoneLabel zoneLabel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpecialNumber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public SpecialNumber tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getInputNum() {
        return this.inputNum;
    }

    public SpecialNumber inputNum(Long inputNum) {
        this.setInputNum(inputNum);
        return this;
    }

    public void setInputNum(Long inputNum) {
        this.inputNum = inputNum;
    }

    public Long getTranslatedNum() {
        return this.translatedNum;
    }

    public SpecialNumber translatedNum(Long translatedNum) {
        this.setTranslatedNum(translatedNum);
        return this;
    }

    public void setTranslatedNum(Long translatedNum) {
        this.translatedNum = translatedNum;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SpecialNumber createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public SpecialNumber modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ZoneLabel getZoneLabel() {
        return this.zoneLabel;
    }

    public void setZoneLabel(ZoneLabel zoneLabel) {
        this.zoneLabel = zoneLabel;
    }

    public SpecialNumber zoneLabel(ZoneLabel zoneLabel) {
        this.setZoneLabel(zoneLabel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialNumber)) {
            return false;
        }
        return getId() != null && getId().equals(((SpecialNumber) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialNumber{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", inputNum=" + getInputNum() +
            ", translatedNum=" + getTranslatedNum() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
