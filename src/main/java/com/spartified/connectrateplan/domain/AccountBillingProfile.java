package com.spartified.connectrateplan.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A AccountBillingProfile.
 */
@Entity
@Table(name = "account_billing_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountBillingProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "mrc_charge_type", length = 255)
    private String mrcChargeType;

    @Size(max = 255)
    @Column(name = "rating_type", length = 255)
    private String ratingType;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "credit_limit", precision = 32, scale = 12)
    private BigDecimal creditLimit;

    @Size(max = 255)
    @Column(name = "mrc_rating_type", length = 255)
    private String mrcRatingType;

    @Column(name = "billable")
    private Boolean billable;

    @Size(max = 255)
    @Column(name = "fan_id", length = 255)
    private String fanId;

    @Size(max = 255)
    @Column(name = "ext_id", length = 255)
    private String extId;

    @Size(max = 255)
    @Column(name = "account_prefix", length = 255)
    private String accountPrefix;

    @Size(max = 6)
    @Column(name = "saart_id", length = 6)
    private String saartId;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(optional = false)
    @NotNull
    private BillingCycle billCycle;

    @ManyToOne(optional = false)
    @NotNull
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountBillingProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public AccountBillingProfile tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getMrcChargeType() {
        return this.mrcChargeType;
    }

    public AccountBillingProfile mrcChargeType(String mrcChargeType) {
        this.setMrcChargeType(mrcChargeType);
        return this;
    }

    public void setMrcChargeType(String mrcChargeType) {
        this.mrcChargeType = mrcChargeType;
    }

    public String getRatingType() {
        return this.ratingType;
    }

    public AccountBillingProfile ratingType(String ratingType) {
        this.setRatingType(ratingType);
        return this;
    }

    public void setRatingType(String ratingType) {
        this.ratingType = ratingType;
    }

    public BigDecimal getCreditLimit() {
        return this.creditLimit;
    }

    public AccountBillingProfile creditLimit(BigDecimal creditLimit) {
        this.setCreditLimit(creditLimit);
        return this;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getMrcRatingType() {
        return this.mrcRatingType;
    }

    public AccountBillingProfile mrcRatingType(String mrcRatingType) {
        this.setMrcRatingType(mrcRatingType);
        return this;
    }

    public void setMrcRatingType(String mrcRatingType) {
        this.mrcRatingType = mrcRatingType;
    }

    public Boolean getBillable() {
        return this.billable;
    }

    public AccountBillingProfile billable(Boolean billable) {
        this.setBillable(billable);
        return this;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public String getFanId() {
        return this.fanId;
    }

    public AccountBillingProfile fanId(String fanId) {
        this.setFanId(fanId);
        return this;
    }

    public void setFanId(String fanId) {
        this.fanId = fanId;
    }

    public String getExtId() {
        return this.extId;
    }

    public AccountBillingProfile extId(String extId) {
        this.setExtId(extId);
        return this;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getAccountPrefix() {
        return this.accountPrefix;
    }

    public AccountBillingProfile accountPrefix(String accountPrefix) {
        this.setAccountPrefix(accountPrefix);
        return this;
    }

    public void setAccountPrefix(String accountPrefix) {
        this.accountPrefix = accountPrefix;
    }

    public String getSaartId() {
        return this.saartId;
    }

    public AccountBillingProfile saartId(String saartId) {
        this.setSaartId(saartId);
        return this;
    }

    public void setSaartId(String saartId) {
        this.saartId = saartId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public AccountBillingProfile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public AccountBillingProfile modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public BillingCycle getBillCycle() {
        return this.billCycle;
    }

    public void setBillCycle(BillingCycle billingCycle) {
        this.billCycle = billingCycle;
    }

    public AccountBillingProfile billCycle(BillingCycle billingCycle) {
        this.setBillCycle(billingCycle);
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public AccountBillingProfile currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBillingProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountBillingProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBillingProfile{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", mrcChargeType='" + getMrcChargeType() + "'" +
            ", ratingType='" + getRatingType() + "'" +
            ", creditLimit=" + getCreditLimit() +
            ", mrcRatingType='" + getMrcRatingType() + "'" +
            ", billable='" + getBillable() + "'" +
            ", fanId='" + getFanId() + "'" +
            ", extId='" + getExtId() + "'" +
            ", accountPrefix='" + getAccountPrefix() + "'" +
            ", saartId='" + getSaartId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
