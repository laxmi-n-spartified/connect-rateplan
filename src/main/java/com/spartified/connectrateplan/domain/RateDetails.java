package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A RateDetails.
 */
@Entity
@Table(name = "rate_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RateDetails implements Serializable {

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

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "data_overage_rate", precision = 32, scale = 12)
    private BigDecimal dataOverageRate;

    @Size(max = 255)
    @Column(name = "data_overage_rate_unit", length = 255)
    private String dataOverageRateUnit;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "sms_mo_overage_rate", precision = 32, scale = 12)
    private BigDecimal smsMoOverageRate;

    @Size(max = 255)
    @Column(name = "sms_mo_overage_rate_unit", length = 255)
    private String smsMoOverageRateUnit;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "sms_mt_overage_rate", precision = 32, scale = 12)
    private BigDecimal smsMtOverageRate;

    @Size(max = 255)
    @Column(name = "sms_mt_overage_rate_unit", length = 255)
    private String smsMtOverageRateUnit;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "voice_mo_overage_rate", precision = 32, scale = 12)
    private BigDecimal voiceMoOverageRate;

    @Size(max = 255)
    @Column(name = "voice_mo_overage_rate_unit", length = 255)
    private String voiceMoOverageRateUnit;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "voice_mt_overage_rate", precision = 32, scale = 12)
    private BigDecimal voiceMtOverageRate;

    @Size(max = 255)
    @Column(name = "voice_mt_overage_rate_unit", length = 255)
    private String voiceMtOverageRateUnit;

    @Column(name = "is_default")
    private Boolean isDefault;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "data_overage_rate_unit_factor", precision = 32, scale = 12)
    private BigDecimal dataOverageRateUnitFactor;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "sms_mo_overage_rate_unit_factor", precision = 32, scale = 12)
    private BigDecimal smsMoOverageRateUnitFactor;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "sms_mt_overage_rate_unit_factor", precision = 32, scale = 12)
    private BigDecimal smsMtOverageRateUnitFactor;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "voice_mo_overage_rate_unit_factor", precision = 32, scale = 12)
    private BigDecimal voiceMoOverageRateUnitFactor;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "voice_mt_overage_rate_unit_factor", precision = 32, scale = 12)
    private BigDecimal voiceMtOverageRateUnitFactor;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "networkIdentities" }, allowSetters = true)
    private ZoneLabel origZoneLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "networkIdentities" }, allowSetters = true)
    private ZoneLabel destZoneLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rateplans" }, allowSetters = true)
    private RatingGroup ratingGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "timeInterval" }, allowSetters = true)
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lifecycle", "timeInterval", "ratingGroups", "rateplanLcStateModel" }, allowSetters = true)
    private Rateplan rateplan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rateplan" }, allowSetters = true)
    private ServiceList serviceList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RateDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public RateDetails tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public BigDecimal getDataOverageRate() {
        return this.dataOverageRate;
    }

    public RateDetails dataOverageRate(BigDecimal dataOverageRate) {
        this.setDataOverageRate(dataOverageRate);
        return this;
    }

    public void setDataOverageRate(BigDecimal dataOverageRate) {
        this.dataOverageRate = dataOverageRate;
    }

    public String getDataOverageRateUnit() {
        return this.dataOverageRateUnit;
    }

    public RateDetails dataOverageRateUnit(String dataOverageRateUnit) {
        this.setDataOverageRateUnit(dataOverageRateUnit);
        return this;
    }

    public void setDataOverageRateUnit(String dataOverageRateUnit) {
        this.dataOverageRateUnit = dataOverageRateUnit;
    }

    public BigDecimal getSmsMoOverageRate() {
        return this.smsMoOverageRate;
    }

    public RateDetails smsMoOverageRate(BigDecimal smsMoOverageRate) {
        this.setSmsMoOverageRate(smsMoOverageRate);
        return this;
    }

    public void setSmsMoOverageRate(BigDecimal smsMoOverageRate) {
        this.smsMoOverageRate = smsMoOverageRate;
    }

    public String getSmsMoOverageRateUnit() {
        return this.smsMoOverageRateUnit;
    }

    public RateDetails smsMoOverageRateUnit(String smsMoOverageRateUnit) {
        this.setSmsMoOverageRateUnit(smsMoOverageRateUnit);
        return this;
    }

    public void setSmsMoOverageRateUnit(String smsMoOverageRateUnit) {
        this.smsMoOverageRateUnit = smsMoOverageRateUnit;
    }

    public BigDecimal getSmsMtOverageRate() {
        return this.smsMtOverageRate;
    }

    public RateDetails smsMtOverageRate(BigDecimal smsMtOverageRate) {
        this.setSmsMtOverageRate(smsMtOverageRate);
        return this;
    }

    public void setSmsMtOverageRate(BigDecimal smsMtOverageRate) {
        this.smsMtOverageRate = smsMtOverageRate;
    }

    public String getSmsMtOverageRateUnit() {
        return this.smsMtOverageRateUnit;
    }

    public RateDetails smsMtOverageRateUnit(String smsMtOverageRateUnit) {
        this.setSmsMtOverageRateUnit(smsMtOverageRateUnit);
        return this;
    }

    public void setSmsMtOverageRateUnit(String smsMtOverageRateUnit) {
        this.smsMtOverageRateUnit = smsMtOverageRateUnit;
    }

    public BigDecimal getVoiceMoOverageRate() {
        return this.voiceMoOverageRate;
    }

    public RateDetails voiceMoOverageRate(BigDecimal voiceMoOverageRate) {
        this.setVoiceMoOverageRate(voiceMoOverageRate);
        return this;
    }

    public void setVoiceMoOverageRate(BigDecimal voiceMoOverageRate) {
        this.voiceMoOverageRate = voiceMoOverageRate;
    }

    public String getVoiceMoOverageRateUnit() {
        return this.voiceMoOverageRateUnit;
    }

    public RateDetails voiceMoOverageRateUnit(String voiceMoOverageRateUnit) {
        this.setVoiceMoOverageRateUnit(voiceMoOverageRateUnit);
        return this;
    }

    public void setVoiceMoOverageRateUnit(String voiceMoOverageRateUnit) {
        this.voiceMoOverageRateUnit = voiceMoOverageRateUnit;
    }

    public BigDecimal getVoiceMtOverageRate() {
        return this.voiceMtOverageRate;
    }

    public RateDetails voiceMtOverageRate(BigDecimal voiceMtOverageRate) {
        this.setVoiceMtOverageRate(voiceMtOverageRate);
        return this;
    }

    public void setVoiceMtOverageRate(BigDecimal voiceMtOverageRate) {
        this.voiceMtOverageRate = voiceMtOverageRate;
    }

    public String getVoiceMtOverageRateUnit() {
        return this.voiceMtOverageRateUnit;
    }

    public RateDetails voiceMtOverageRateUnit(String voiceMtOverageRateUnit) {
        this.setVoiceMtOverageRateUnit(voiceMtOverageRateUnit);
        return this;
    }

    public void setVoiceMtOverageRateUnit(String voiceMtOverageRateUnit) {
        this.voiceMtOverageRateUnit = voiceMtOverageRateUnit;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public RateDetails isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public BigDecimal getDataOverageRateUnitFactor() {
        return this.dataOverageRateUnitFactor;
    }

    public RateDetails dataOverageRateUnitFactor(BigDecimal dataOverageRateUnitFactor) {
        this.setDataOverageRateUnitFactor(dataOverageRateUnitFactor);
        return this;
    }

    public void setDataOverageRateUnitFactor(BigDecimal dataOverageRateUnitFactor) {
        this.dataOverageRateUnitFactor = dataOverageRateUnitFactor;
    }

    public BigDecimal getSmsMoOverageRateUnitFactor() {
        return this.smsMoOverageRateUnitFactor;
    }

    public RateDetails smsMoOverageRateUnitFactor(BigDecimal smsMoOverageRateUnitFactor) {
        this.setSmsMoOverageRateUnitFactor(smsMoOverageRateUnitFactor);
        return this;
    }

    public void setSmsMoOverageRateUnitFactor(BigDecimal smsMoOverageRateUnitFactor) {
        this.smsMoOverageRateUnitFactor = smsMoOverageRateUnitFactor;
    }

    public BigDecimal getSmsMtOverageRateUnitFactor() {
        return this.smsMtOverageRateUnitFactor;
    }

    public RateDetails smsMtOverageRateUnitFactor(BigDecimal smsMtOverageRateUnitFactor) {
        this.setSmsMtOverageRateUnitFactor(smsMtOverageRateUnitFactor);
        return this;
    }

    public void setSmsMtOverageRateUnitFactor(BigDecimal smsMtOverageRateUnitFactor) {
        this.smsMtOverageRateUnitFactor = smsMtOverageRateUnitFactor;
    }

    public BigDecimal getVoiceMoOverageRateUnitFactor() {
        return this.voiceMoOverageRateUnitFactor;
    }

    public RateDetails voiceMoOverageRateUnitFactor(BigDecimal voiceMoOverageRateUnitFactor) {
        this.setVoiceMoOverageRateUnitFactor(voiceMoOverageRateUnitFactor);
        return this;
    }

    public void setVoiceMoOverageRateUnitFactor(BigDecimal voiceMoOverageRateUnitFactor) {
        this.voiceMoOverageRateUnitFactor = voiceMoOverageRateUnitFactor;
    }

    public BigDecimal getVoiceMtOverageRateUnitFactor() {
        return this.voiceMtOverageRateUnitFactor;
    }

    public RateDetails voiceMtOverageRateUnitFactor(BigDecimal voiceMtOverageRateUnitFactor) {
        this.setVoiceMtOverageRateUnitFactor(voiceMtOverageRateUnitFactor);
        return this;
    }

    public void setVoiceMtOverageRateUnitFactor(BigDecimal voiceMtOverageRateUnitFactor) {
        this.voiceMtOverageRateUnitFactor = voiceMtOverageRateUnitFactor;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public RateDetails createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public RateDetails modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ZoneLabel getOrigZoneLabel() {
        return this.origZoneLabel;
    }

    public void setOrigZoneLabel(ZoneLabel zoneLabel) {
        this.origZoneLabel = zoneLabel;
    }

    public RateDetails origZoneLabel(ZoneLabel zoneLabel) {
        this.setOrigZoneLabel(zoneLabel);
        return this;
    }

    public ZoneLabel getDestZoneLabel() {
        return this.destZoneLabel;
    }

    public void setDestZoneLabel(ZoneLabel zoneLabel) {
        this.destZoneLabel = zoneLabel;
    }

    public RateDetails destZoneLabel(ZoneLabel zoneLabel) {
        this.setDestZoneLabel(zoneLabel);
        return this;
    }

    public RatingGroup getRatingGroup() {
        return this.ratingGroup;
    }

    public void setRatingGroup(RatingGroup ratingGroup) {
        this.ratingGroup = ratingGroup;
    }

    public RateDetails ratingGroup(RatingGroup ratingGroup) {
        this.setRatingGroup(ratingGroup);
        return this;
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public RateDetails timeSlot(TimeSlot timeSlot) {
        this.setTimeSlot(timeSlot);
        return this;
    }

    public Rateplan getRateplan() {
        return this.rateplan;
    }

    public void setRateplan(Rateplan rateplan) {
        this.rateplan = rateplan;
    }

    public RateDetails rateplan(Rateplan rateplan) {
        this.setRateplan(rateplan);
        return this;
    }

    public ServiceList getServiceList() {
        return this.serviceList;
    }

    public void setServiceList(ServiceList serviceList) {
        this.serviceList = serviceList;
    }

    public RateDetails serviceList(ServiceList serviceList) {
        this.setServiceList(serviceList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RateDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((RateDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RateDetails{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", dataOverageRate=" + getDataOverageRate() +
            ", dataOverageRateUnit='" + getDataOverageRateUnit() + "'" +
            ", smsMoOverageRate=" + getSmsMoOverageRate() +
            ", smsMoOverageRateUnit='" + getSmsMoOverageRateUnit() + "'" +
            ", smsMtOverageRate=" + getSmsMtOverageRate() +
            ", smsMtOverageRateUnit='" + getSmsMtOverageRateUnit() + "'" +
            ", voiceMoOverageRate=" + getVoiceMoOverageRate() +
            ", voiceMoOverageRateUnit='" + getVoiceMoOverageRateUnit() + "'" +
            ", voiceMtOverageRate=" + getVoiceMtOverageRate() +
            ", voiceMtOverageRateUnit='" + getVoiceMtOverageRateUnit() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", dataOverageRateUnitFactor=" + getDataOverageRateUnitFactor() +
            ", smsMoOverageRateUnitFactor=" + getSmsMoOverageRateUnitFactor() +
            ", smsMtOverageRateUnitFactor=" + getSmsMtOverageRateUnitFactor() +
            ", voiceMoOverageRateUnitFactor=" + getVoiceMoOverageRateUnitFactor() +
            ", voiceMtOverageRateUnitFactor=" + getVoiceMtOverageRateUnitFactor() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
