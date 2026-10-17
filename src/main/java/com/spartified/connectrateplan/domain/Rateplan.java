package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Rateplan.
 */
@Entity
@Table(name = "rateplan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rateplan implements Serializable {

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

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Size(max = 255)
    @Column(name = "organization_name", length = 255)
    private String organizationName;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "activation_fee", precision = 32, scale = 12)
    private BigDecimal activationFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "network_access_fee", precision = 32, scale = 12)
    private BigDecimal networkAccessFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "subscription_fee", precision = 32, scale = 12)
    private BigDecimal subscriptionFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "cancellation_fee", precision = 32, scale = 12)
    private BigDecimal cancellationFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "additional_fees", precision = 32, scale = 12)
    private BigDecimal additionalFees;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "re_activation_fee", precision = 32, scale = 12)
    private BigDecimal reActivationFee;

    @Column(name = "contract_period")
    private Integer contractPeriod;

    @Column(name = "notice_period")
    private Integer noticePeriod;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 255)
    @Column(name = "status", length = 255)
    private String status;

    @Size(max = 255)
    @Column(name = "plan_type", length = 255)
    private String planType;

    @Column(name = "version")
    private Integer version;

    @Size(max = 255)
    @Column(name = "subscription_type", length = 255)
    private String subscriptionType;

    @Column(name = "included_test_data")
    private Long includedTestData;

    @Column(name = "included_test_sms")
    private Long includedTestSms;

    @Column(name = "included_test_voice")
    private Long includedTestVoice;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "trial_duration")
    private Integer trialDuration;

    @Size(max = 255)
    @Column(name = "pool_tariff", length = 255)
    private String poolTariff;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "pool_activation_fee", precision = 32, scale = 12)
    private BigDecimal poolActivationFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "pool_subscription_fee", precision = 32, scale = 12)
    private BigDecimal poolSubscriptionFee;

    @Size(max = 255)
    @Column(name = "rounding_for_data", length = 255)
    private String roundingForData;

    @Size(max = 255)
    @Column(name = "rounding_for_voice", length = 255)
    private String roundingForVoice;

    @Size(max = 255)
    @Column(name = "included_test_data_unit", length = 255)
    private String includedTestDataUnit;

    @Size(max = 255)
    @Column(name = "included_test_sms_unit", length = 255)
    private String includedTestSmsUnit;

    @Size(max = 255)
    @Column(name = "included_test_voice_unit", length = 255)
    private String includedTestVoiceUnit;

    @Column(name = "renew_on_expiry")
    private Boolean renewOnExpiry;

    @Size(max = 255)
    @Column(name = "tiered_volume_type", length = 255)
    private String tieredVolumeType;

    @Column(name = "validity_period")
    private Integer validityPeriod;

    @Size(max = 255)
    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "quota_profile_id")
    private Long quotaProfileId;

    @Size(max = 255)
    @Column(name = "quota_profile_name", length = 255)
    private String quotaProfileName;

    @Size(max = 255)
    @Column(name = "ext_id", length = 255)
    private String extId;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "fixed_active_fee", precision = 32, scale = 12)
    private BigDecimal fixedActiveFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "suspend_fee", precision = 32, scale = 12)
    private BigDecimal suspendFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "suspended_sim_mrc", precision = 32, scale = 12)
    private BigDecimal suspendedSimMrc;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "unsuspend_fee", precision = 32, scale = 12)
    private BigDecimal unsuspendFee;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "deactivation_fee", precision = 32, scale = 12)
    private BigDecimal deactivationFee;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lifecycle lifecycle;

    @ManyToOne(fetch = FetchType.LAZY)
    private TimeInterval timeInterval;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_rateplan__rating_group",
        joinColumns = @JoinColumn(name = "rateplan_id"),
        inverseJoinColumns = @JoinColumn(name = "rating_group_id")
    )
    @JsonIgnoreProperties(value = { "rateplans" }, allowSetters = true)
    private Set<RatingGroup> ratingGroups = new HashSet<>();

    @JsonIgnoreProperties(value = { "rateplan", "lifecycle" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "rateplan")
    private RateplanLcStateModel rateplanLcStateModel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rateplan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public Rateplan tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return this.name;
    }

    public Rateplan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public Rateplan organizationName(String organizationName) {
        this.setOrganizationName(organizationName);
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public BigDecimal getActivationFee() {
        return this.activationFee;
    }

    public Rateplan activationFee(BigDecimal activationFee) {
        this.setActivationFee(activationFee);
        return this;
    }

    public void setActivationFee(BigDecimal activationFee) {
        this.activationFee = activationFee;
    }

    public BigDecimal getNetworkAccessFee() {
        return this.networkAccessFee;
    }

    public Rateplan networkAccessFee(BigDecimal networkAccessFee) {
        this.setNetworkAccessFee(networkAccessFee);
        return this;
    }

    public void setNetworkAccessFee(BigDecimal networkAccessFee) {
        this.networkAccessFee = networkAccessFee;
    }

    public BigDecimal getSubscriptionFee() {
        return this.subscriptionFee;
    }

    public Rateplan subscriptionFee(BigDecimal subscriptionFee) {
        this.setSubscriptionFee(subscriptionFee);
        return this;
    }

    public void setSubscriptionFee(BigDecimal subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    public BigDecimal getCancellationFee() {
        return this.cancellationFee;
    }

    public Rateplan cancellationFee(BigDecimal cancellationFee) {
        this.setCancellationFee(cancellationFee);
        return this;
    }

    public void setCancellationFee(BigDecimal cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public BigDecimal getAdditionalFees() {
        return this.additionalFees;
    }

    public Rateplan additionalFees(BigDecimal additionalFees) {
        this.setAdditionalFees(additionalFees);
        return this;
    }

    public void setAdditionalFees(BigDecimal additionalFees) {
        this.additionalFees = additionalFees;
    }

    public BigDecimal getReActivationFee() {
        return this.reActivationFee;
    }

    public Rateplan reActivationFee(BigDecimal reActivationFee) {
        this.setReActivationFee(reActivationFee);
        return this;
    }

    public void setReActivationFee(BigDecimal reActivationFee) {
        this.reActivationFee = reActivationFee;
    }

    public Integer getContractPeriod() {
        return this.contractPeriod;
    }

    public Rateplan contractPeriod(Integer contractPeriod) {
        this.setContractPeriod(contractPeriod);
        return this;
    }

    public void setContractPeriod(Integer contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public Integer getNoticePeriod() {
        return this.noticePeriod;
    }

    public Rateplan noticePeriod(Integer noticePeriod) {
        this.setNoticePeriod(noticePeriod);
        return this;
    }

    public void setNoticePeriod(Integer noticePeriod) {
        this.noticePeriod = noticePeriod;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Rateplan startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Rateplan endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return this.status;
    }

    public Rateplan status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlanType() {
        return this.planType;
    }

    public Rateplan planType(String planType) {
        this.setPlanType(planType);
        return this;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Rateplan version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSubscriptionType() {
        return this.subscriptionType;
    }

    public Rateplan subscriptionType(String subscriptionType) {
        this.setSubscriptionType(subscriptionType);
        return this;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Long getIncludedTestData() {
        return this.includedTestData;
    }

    public Rateplan includedTestData(Long includedTestData) {
        this.setIncludedTestData(includedTestData);
        return this;
    }

    public void setIncludedTestData(Long includedTestData) {
        this.includedTestData = includedTestData;
    }

    public Long getIncludedTestSms() {
        return this.includedTestSms;
    }

    public Rateplan includedTestSms(Long includedTestSms) {
        this.setIncludedTestSms(includedTestSms);
        return this;
    }

    public void setIncludedTestSms(Long includedTestSms) {
        this.includedTestSms = includedTestSms;
    }

    public Long getIncludedTestVoice() {
        return this.includedTestVoice;
    }

    public Rateplan includedTestVoice(Long includedTestVoice) {
        this.setIncludedTestVoice(includedTestVoice);
        return this;
    }

    public void setIncludedTestVoice(Long includedTestVoice) {
        this.includedTestVoice = includedTestVoice;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public Rateplan expiryDate(LocalDate expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getTrialDuration() {
        return this.trialDuration;
    }

    public Rateplan trialDuration(Integer trialDuration) {
        this.setTrialDuration(trialDuration);
        return this;
    }

    public void setTrialDuration(Integer trialDuration) {
        this.trialDuration = trialDuration;
    }

    public String getPoolTariff() {
        return this.poolTariff;
    }

    public Rateplan poolTariff(String poolTariff) {
        this.setPoolTariff(poolTariff);
        return this;
    }

    public void setPoolTariff(String poolTariff) {
        this.poolTariff = poolTariff;
    }

    public BigDecimal getPoolActivationFee() {
        return this.poolActivationFee;
    }

    public Rateplan poolActivationFee(BigDecimal poolActivationFee) {
        this.setPoolActivationFee(poolActivationFee);
        return this;
    }

    public void setPoolActivationFee(BigDecimal poolActivationFee) {
        this.poolActivationFee = poolActivationFee;
    }

    public BigDecimal getPoolSubscriptionFee() {
        return this.poolSubscriptionFee;
    }

    public Rateplan poolSubscriptionFee(BigDecimal poolSubscriptionFee) {
        this.setPoolSubscriptionFee(poolSubscriptionFee);
        return this;
    }

    public void setPoolSubscriptionFee(BigDecimal poolSubscriptionFee) {
        this.poolSubscriptionFee = poolSubscriptionFee;
    }

    public String getRoundingForData() {
        return this.roundingForData;
    }

    public Rateplan roundingForData(String roundingForData) {
        this.setRoundingForData(roundingForData);
        return this;
    }

    public void setRoundingForData(String roundingForData) {
        this.roundingForData = roundingForData;
    }

    public String getRoundingForVoice() {
        return this.roundingForVoice;
    }

    public Rateplan roundingForVoice(String roundingForVoice) {
        this.setRoundingForVoice(roundingForVoice);
        return this;
    }

    public void setRoundingForVoice(String roundingForVoice) {
        this.roundingForVoice = roundingForVoice;
    }

    public String getIncludedTestDataUnit() {
        return this.includedTestDataUnit;
    }

    public Rateplan includedTestDataUnit(String includedTestDataUnit) {
        this.setIncludedTestDataUnit(includedTestDataUnit);
        return this;
    }

    public void setIncludedTestDataUnit(String includedTestDataUnit) {
        this.includedTestDataUnit = includedTestDataUnit;
    }

    public String getIncludedTestSmsUnit() {
        return this.includedTestSmsUnit;
    }

    public Rateplan includedTestSmsUnit(String includedTestSmsUnit) {
        this.setIncludedTestSmsUnit(includedTestSmsUnit);
        return this;
    }

    public void setIncludedTestSmsUnit(String includedTestSmsUnit) {
        this.includedTestSmsUnit = includedTestSmsUnit;
    }

    public String getIncludedTestVoiceUnit() {
        return this.includedTestVoiceUnit;
    }

    public Rateplan includedTestVoiceUnit(String includedTestVoiceUnit) {
        this.setIncludedTestVoiceUnit(includedTestVoiceUnit);
        return this;
    }

    public void setIncludedTestVoiceUnit(String includedTestVoiceUnit) {
        this.includedTestVoiceUnit = includedTestVoiceUnit;
    }

    public Boolean getRenewOnExpiry() {
        return this.renewOnExpiry;
    }

    public Rateplan renewOnExpiry(Boolean renewOnExpiry) {
        this.setRenewOnExpiry(renewOnExpiry);
        return this;
    }

    public void setRenewOnExpiry(Boolean renewOnExpiry) {
        this.renewOnExpiry = renewOnExpiry;
    }

    public String getTieredVolumeType() {
        return this.tieredVolumeType;
    }

    public Rateplan tieredVolumeType(String tieredVolumeType) {
        this.setTieredVolumeType(tieredVolumeType);
        return this;
    }

    public void setTieredVolumeType(String tieredVolumeType) {
        this.tieredVolumeType = tieredVolumeType;
    }

    public Integer getValidityPeriod() {
        return this.validityPeriod;
    }

    public Rateplan validityPeriod(Integer validityPeriod) {
        this.setValidityPeriod(validityPeriod);
        return this;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getCategory() {
        return this.category;
    }

    public Rateplan category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getQuotaProfileId() {
        return this.quotaProfileId;
    }

    public Rateplan quotaProfileId(Long quotaProfileId) {
        this.setQuotaProfileId(quotaProfileId);
        return this;
    }

    public void setQuotaProfileId(Long quotaProfileId) {
        this.quotaProfileId = quotaProfileId;
    }

    public String getQuotaProfileName() {
        return this.quotaProfileName;
    }

    public Rateplan quotaProfileName(String quotaProfileName) {
        this.setQuotaProfileName(quotaProfileName);
        return this;
    }

    public void setQuotaProfileName(String quotaProfileName) {
        this.quotaProfileName = quotaProfileName;
    }

    public String getExtId() {
        return this.extId;
    }

    public Rateplan extId(String extId) {
        this.setExtId(extId);
        return this;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public BigDecimal getFixedActiveFee() {
        return this.fixedActiveFee;
    }

    public Rateplan fixedActiveFee(BigDecimal fixedActiveFee) {
        this.setFixedActiveFee(fixedActiveFee);
        return this;
    }

    public void setFixedActiveFee(BigDecimal fixedActiveFee) {
        this.fixedActiveFee = fixedActiveFee;
    }

    public BigDecimal getSuspendFee() {
        return this.suspendFee;
    }

    public Rateplan suspendFee(BigDecimal suspendFee) {
        this.setSuspendFee(suspendFee);
        return this;
    }

    public void setSuspendFee(BigDecimal suspendFee) {
        this.suspendFee = suspendFee;
    }

    public BigDecimal getSuspendedSimMrc() {
        return this.suspendedSimMrc;
    }

    public Rateplan suspendedSimMrc(BigDecimal suspendedSimMrc) {
        this.setSuspendedSimMrc(suspendedSimMrc);
        return this;
    }

    public void setSuspendedSimMrc(BigDecimal suspendedSimMrc) {
        this.suspendedSimMrc = suspendedSimMrc;
    }

    public BigDecimal getUnsuspendFee() {
        return this.unsuspendFee;
    }

    public Rateplan unsuspendFee(BigDecimal unsuspendFee) {
        this.setUnsuspendFee(unsuspendFee);
        return this;
    }

    public void setUnsuspendFee(BigDecimal unsuspendFee) {
        this.unsuspendFee = unsuspendFee;
    }

    public BigDecimal getDeactivationFee() {
        return this.deactivationFee;
    }

    public Rateplan deactivationFee(BigDecimal deactivationFee) {
        this.setDeactivationFee(deactivationFee);
        return this;
    }

    public void setDeactivationFee(BigDecimal deactivationFee) {
        this.deactivationFee = deactivationFee;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Rateplan createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Rateplan modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Rateplan lifecycle(Lifecycle lifecycle) {
        this.setLifecycle(lifecycle);
        return this;
    }

    public TimeInterval getTimeInterval() {
        return this.timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Rateplan timeInterval(TimeInterval timeInterval) {
        this.setTimeInterval(timeInterval);
        return this;
    }

    public Set<RatingGroup> getRatingGroups() {
        return this.ratingGroups;
    }

    public void setRatingGroups(Set<RatingGroup> ratingGroups) {
        this.ratingGroups = ratingGroups;
    }

    public Rateplan ratingGroups(Set<RatingGroup> ratingGroups) {
        this.setRatingGroups(ratingGroups);
        return this;
    }

    public Rateplan addRatingGroup(RatingGroup ratingGroup) {
        this.ratingGroups.add(ratingGroup);
        return this;
    }

    public Rateplan removeRatingGroup(RatingGroup ratingGroup) {
        this.ratingGroups.remove(ratingGroup);
        return this;
    }

    public RateplanLcStateModel getRateplanLcStateModel() {
        return this.rateplanLcStateModel;
    }

    public void setRateplanLcStateModel(RateplanLcStateModel rateplanLcStateModel) {
        if (this.rateplanLcStateModel != null) {
            this.rateplanLcStateModel.setRateplan(null);
        }
        if (rateplanLcStateModel != null) {
            rateplanLcStateModel.setRateplan(this);
        }
        this.rateplanLcStateModel = rateplanLcStateModel;
    }

    public Rateplan rateplanLcStateModel(RateplanLcStateModel rateplanLcStateModel) {
        this.setRateplanLcStateModel(rateplanLcStateModel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rateplan)) {
            return false;
        }
        return getId() != null && getId().equals(((Rateplan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rateplan{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", name='" + getName() + "'" +
            ", organizationName='" + getOrganizationName() + "'" +
            ", activationFee=" + getActivationFee() +
            ", networkAccessFee=" + getNetworkAccessFee() +
            ", subscriptionFee=" + getSubscriptionFee() +
            ", cancellationFee=" + getCancellationFee() +
            ", additionalFees=" + getAdditionalFees() +
            ", reActivationFee=" + getReActivationFee() +
            ", contractPeriod=" + getContractPeriod() +
            ", noticePeriod=" + getNoticePeriod() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", planType='" + getPlanType() + "'" +
            ", version=" + getVersion() +
            ", subscriptionType='" + getSubscriptionType() + "'" +
            ", includedTestData=" + getIncludedTestData() +
            ", includedTestSms=" + getIncludedTestSms() +
            ", includedTestVoice=" + getIncludedTestVoice() +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", trialDuration=" + getTrialDuration() +
            ", poolTariff='" + getPoolTariff() + "'" +
            ", poolActivationFee=" + getPoolActivationFee() +
            ", poolSubscriptionFee=" + getPoolSubscriptionFee() +
            ", roundingForData='" + getRoundingForData() + "'" +
            ", roundingForVoice='" + getRoundingForVoice() + "'" +
            ", includedTestDataUnit='" + getIncludedTestDataUnit() + "'" +
            ", includedTestSmsUnit='" + getIncludedTestSmsUnit() + "'" +
            ", includedTestVoiceUnit='" + getIncludedTestVoiceUnit() + "'" +
            ", renewOnExpiry='" + getRenewOnExpiry() + "'" +
            ", tieredVolumeType='" + getTieredVolumeType() + "'" +
            ", validityPeriod=" + getValidityPeriod() +
            ", category='" + getCategory() + "'" +
            ", quotaProfileId=" + getQuotaProfileId() +
            ", quotaProfileName='" + getQuotaProfileName() + "'" +
            ", extId='" + getExtId() + "'" +
            ", fixedActiveFee=" + getFixedActiveFee() +
            ", suspendFee=" + getSuspendFee() +
            ", suspendedSimMrc=" + getSuspendedSimMrc() +
            ", unsuspendFee=" + getUnsuspendFee() +
            ", deactivationFee=" + getDeactivationFee() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
