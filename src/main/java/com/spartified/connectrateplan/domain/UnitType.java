package com.spartified.connectrateplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A UnitType.
 */
@Entity
@Table(name = "unit_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "conversion_factor")
    private Integer conversionFactor;

    @Size(max = 255)
    @Column(name = "lowest_unit", length = 255)
    private String lowestUnit;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UnitType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConversionFactor() {
        return this.conversionFactor;
    }

    public UnitType conversionFactor(Integer conversionFactor) {
        this.setConversionFactor(conversionFactor);
        return this;
    }

    public void setConversionFactor(Integer conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public String getLowestUnit() {
        return this.lowestUnit;
    }

    public UnitType lowestUnit(String lowestUnit) {
        this.setLowestUnit(lowestUnit);
        return this;
    }

    public void setLowestUnit(String lowestUnit) {
        this.lowestUnit = lowestUnit;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UnitType createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public UnitType modifiedDate(Instant modifiedDate) {
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
        if (!(o instanceof UnitType)) {
            return false;
        }
        return getId() != null && getId().equals(((UnitType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", conversionFactor=" + getConversionFactor() +
            ", lowestUnit='" + getLowestUnit() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
