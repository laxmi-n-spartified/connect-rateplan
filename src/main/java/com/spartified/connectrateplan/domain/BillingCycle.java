package com.spartified.connectrateplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A BillingCycle.
 */
@Entity
@Table(name = "billing_cycle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillingCycle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bill_cycle_date")
    private Integer billCycleDate;

    @Size(max = 255)
    @Column(name = "bill_cycle_name", length = 255)
    private String billCycleName;

    @Size(max = 255)
    @Column(name = "bill_cycle_duration", length = 255)
    private String billCycleDuration;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillingCycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBillCycleDate() {
        return this.billCycleDate;
    }

    public BillingCycle billCycleDate(Integer billCycleDate) {
        this.setBillCycleDate(billCycleDate);
        return this;
    }

    public void setBillCycleDate(Integer billCycleDate) {
        this.billCycleDate = billCycleDate;
    }

    public String getBillCycleName() {
        return this.billCycleName;
    }

    public BillingCycle billCycleName(String billCycleName) {
        this.setBillCycleName(billCycleName);
        return this;
    }

    public void setBillCycleName(String billCycleName) {
        this.billCycleName = billCycleName;
    }

    public String getBillCycleDuration() {
        return this.billCycleDuration;
    }

    public BillingCycle billCycleDuration(String billCycleDuration) {
        this.setBillCycleDuration(billCycleDuration);
        return this;
    }

    public void setBillCycleDuration(String billCycleDuration) {
        this.billCycleDuration = billCycleDuration;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public BillingCycle createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public BillingCycle modifiedDate(Instant modifiedDate) {
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
        if (!(o instanceof BillingCycle)) {
            return false;
        }
        return getId() != null && getId().equals(((BillingCycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillingCycle{" +
            "id=" + getId() +
            ", billCycleDate=" + getBillCycleDate() +
            ", billCycleName='" + getBillCycleName() + "'" +
            ", billCycleDuration='" + getBillCycleDuration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
