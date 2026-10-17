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
 * A TierVolumeDetails.
 */
@Entity
@Table(name = "tier_volume_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TierVolumeDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * numeric(32,12)
     */
    @Schema(description = "numeric(32,12)")
    @Column(name = "mrc", precision = 32, scale = 12)
    private BigDecimal mrc;

    @Size(max = 255)
    @Column(name = "rating_type_mrc", length = 255)
    private String ratingTypeMrc;

    @Size(max = 255)
    @Column(name = "rating_type_usage", length = 255)
    private String ratingTypeUsage;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "tier_limit", length = 255)
    private String tierLimit;

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
    @JsonIgnoreProperties(
        value = { "origZoneLabel", "destZoneLabel", "ratingGroup", "timeSlot", "rateplan", "serviceList" },
        allowSetters = true
    )
    private RateDetails rateDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lifecycle", "timeInterval", "ratingGroups", "rateplanLcStateModel" }, allowSetters = true)
    private Rateplan rateplan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rateplans" }, allowSetters = true)
    private RatingGroup ratingGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rateplan" }, allowSetters = true)
    private ServiceList serviceList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TierVolumeDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMrc() {
        return this.mrc;
    }

    public TierVolumeDetails mrc(BigDecimal mrc) {
        this.setMrc(mrc);
        return this;
    }

    public void setMrc(BigDecimal mrc) {
        this.mrc = mrc;
    }

    public String getRatingTypeMrc() {
        return this.ratingTypeMrc;
    }

    public TierVolumeDetails ratingTypeMrc(String ratingTypeMrc) {
        this.setRatingTypeMrc(ratingTypeMrc);
        return this;
    }

    public void setRatingTypeMrc(String ratingTypeMrc) {
        this.ratingTypeMrc = ratingTypeMrc;
    }

    public String getRatingTypeUsage() {
        return this.ratingTypeUsage;
    }

    public TierVolumeDetails ratingTypeUsage(String ratingTypeUsage) {
        this.setRatingTypeUsage(ratingTypeUsage);
        return this;
    }

    public void setRatingTypeUsage(String ratingTypeUsage) {
        this.ratingTypeUsage = ratingTypeUsage;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TierVolumeDetails tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTierLimit() {
        return this.tierLimit;
    }

    public TierVolumeDetails tierLimit(String tierLimit) {
        this.setTierLimit(tierLimit);
        return this;
    }

    public void setTierLimit(String tierLimit) {
        this.tierLimit = tierLimit;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public TierVolumeDetails createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public TierVolumeDetails modifiedDate(Instant modifiedDate) {
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

    public TierVolumeDetails origZoneLabel(ZoneLabel zoneLabel) {
        this.setOrigZoneLabel(zoneLabel);
        return this;
    }

    public ZoneLabel getDestZoneLabel() {
        return this.destZoneLabel;
    }

    public void setDestZoneLabel(ZoneLabel zoneLabel) {
        this.destZoneLabel = zoneLabel;
    }

    public TierVolumeDetails destZoneLabel(ZoneLabel zoneLabel) {
        this.setDestZoneLabel(zoneLabel);
        return this;
    }

    public RateDetails getRateDetail() {
        return this.rateDetail;
    }

    public void setRateDetail(RateDetails rateDetails) {
        this.rateDetail = rateDetails;
    }

    public TierVolumeDetails rateDetail(RateDetails rateDetails) {
        this.setRateDetail(rateDetails);
        return this;
    }

    public Rateplan getRateplan() {
        return this.rateplan;
    }

    public void setRateplan(Rateplan rateplan) {
        this.rateplan = rateplan;
    }

    public TierVolumeDetails rateplan(Rateplan rateplan) {
        this.setRateplan(rateplan);
        return this;
    }

    public RatingGroup getRatingGroup() {
        return this.ratingGroup;
    }

    public void setRatingGroup(RatingGroup ratingGroup) {
        this.ratingGroup = ratingGroup;
    }

    public TierVolumeDetails ratingGroup(RatingGroup ratingGroup) {
        this.setRatingGroup(ratingGroup);
        return this;
    }

    public ServiceList getServiceList() {
        return this.serviceList;
    }

    public void setServiceList(ServiceList serviceList) {
        this.serviceList = serviceList;
    }

    public TierVolumeDetails serviceList(ServiceList serviceList) {
        this.setServiceList(serviceList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TierVolumeDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((TierVolumeDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TierVolumeDetails{" +
            "id=" + getId() +
            ", mrc=" + getMrc() +
            ", ratingTypeMrc='" + getRatingTypeMrc() + "'" +
            ", ratingTypeUsage='" + getRatingTypeUsage() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", tierLimit='" + getTierLimit() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
