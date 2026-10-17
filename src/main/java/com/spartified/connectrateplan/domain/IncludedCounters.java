package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A IncludedCounters.
 */
@Entity
@Table(name = "included_counters")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncludedCounters implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "counter_data")
    private Long counterData;

    @Column(name = "counter_sms_mo")
    private Long counterSmsMo;

    @Column(name = "counter_sms_mt")
    private Long counterSmsMt;

    @Column(name = "counter_voice_mo")
    private Long counterVoiceMo;

    @Column(name = "counter_voice_mt")
    private Long counterVoiceMt;

    @Column(name = "rollover_data")
    private Boolean rolloverData;

    @Column(name = "rollover_data_duration")
    private Integer rolloverDataDuration;

    @Column(name = "rollover_voice_mo")
    private Boolean rolloverVoiceMo;

    @Column(name = "rollover_voice_mo_duration")
    private Integer rolloverVoiceMoDuration;

    @Column(name = "rollover_sms_mo")
    private Boolean rolloverSmsMo;

    @Column(name = "rollover_sms_mo_duration")
    private Integer rolloverSmsMoDuration;

    @Size(max = 255)
    @Column(name = "counter_data_unit", length = 255)
    private String counterDataUnit;

    @Size(max = 255)
    @Column(name = "counter_sms_mo_unit", length = 255)
    private String counterSmsMoUnit;

    @Size(max = 255)
    @Column(name = "counter_sms_mt_unit", length = 255)
    private String counterSmsMtUnit;

    @Size(max = 255)
    @Column(name = "counter_voice_mo_unit", length = 255)
    private String counterVoiceMoUnit;

    @Size(max = 255)
    @Column(name = "counter_voice_mt_unit", length = 255)
    private String counterVoiceMtUnit;

    @Column(name = "renew_on_quota_exhaust")
    private Boolean renewOnQuotaExhaust;

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
    @JsonIgnoreProperties(value = { "lifecycle", "timeInterval", "ratingGroups", "rateplanLcStateModel" }, allowSetters = true)
    private Rateplan rateplan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "rateplan" }, allowSetters = true)
    private ServiceList serviceList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "timeInterval" }, allowSetters = true)
    private TimeSlot timeSlot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IncludedCounters id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCounterData() {
        return this.counterData;
    }

    public IncludedCounters counterData(Long counterData) {
        this.setCounterData(counterData);
        return this;
    }

    public void setCounterData(Long counterData) {
        this.counterData = counterData;
    }

    public Long getCounterSmsMo() {
        return this.counterSmsMo;
    }

    public IncludedCounters counterSmsMo(Long counterSmsMo) {
        this.setCounterSmsMo(counterSmsMo);
        return this;
    }

    public void setCounterSmsMo(Long counterSmsMo) {
        this.counterSmsMo = counterSmsMo;
    }

    public Long getCounterSmsMt() {
        return this.counterSmsMt;
    }

    public IncludedCounters counterSmsMt(Long counterSmsMt) {
        this.setCounterSmsMt(counterSmsMt);
        return this;
    }

    public void setCounterSmsMt(Long counterSmsMt) {
        this.counterSmsMt = counterSmsMt;
    }

    public Long getCounterVoiceMo() {
        return this.counterVoiceMo;
    }

    public IncludedCounters counterVoiceMo(Long counterVoiceMo) {
        this.setCounterVoiceMo(counterVoiceMo);
        return this;
    }

    public void setCounterVoiceMo(Long counterVoiceMo) {
        this.counterVoiceMo = counterVoiceMo;
    }

    public Long getCounterVoiceMt() {
        return this.counterVoiceMt;
    }

    public IncludedCounters counterVoiceMt(Long counterVoiceMt) {
        this.setCounterVoiceMt(counterVoiceMt);
        return this;
    }

    public void setCounterVoiceMt(Long counterVoiceMt) {
        this.counterVoiceMt = counterVoiceMt;
    }

    public Boolean getRolloverData() {
        return this.rolloverData;
    }

    public IncludedCounters rolloverData(Boolean rolloverData) {
        this.setRolloverData(rolloverData);
        return this;
    }

    public void setRolloverData(Boolean rolloverData) {
        this.rolloverData = rolloverData;
    }

    public Integer getRolloverDataDuration() {
        return this.rolloverDataDuration;
    }

    public IncludedCounters rolloverDataDuration(Integer rolloverDataDuration) {
        this.setRolloverDataDuration(rolloverDataDuration);
        return this;
    }

    public void setRolloverDataDuration(Integer rolloverDataDuration) {
        this.rolloverDataDuration = rolloverDataDuration;
    }

    public Boolean getRolloverVoiceMo() {
        return this.rolloverVoiceMo;
    }

    public IncludedCounters rolloverVoiceMo(Boolean rolloverVoiceMo) {
        this.setRolloverVoiceMo(rolloverVoiceMo);
        return this;
    }

    public void setRolloverVoiceMo(Boolean rolloverVoiceMo) {
        this.rolloverVoiceMo = rolloverVoiceMo;
    }

    public Integer getRolloverVoiceMoDuration() {
        return this.rolloverVoiceMoDuration;
    }

    public IncludedCounters rolloverVoiceMoDuration(Integer rolloverVoiceMoDuration) {
        this.setRolloverVoiceMoDuration(rolloverVoiceMoDuration);
        return this;
    }

    public void setRolloverVoiceMoDuration(Integer rolloverVoiceMoDuration) {
        this.rolloverVoiceMoDuration = rolloverVoiceMoDuration;
    }

    public Boolean getRolloverSmsMo() {
        return this.rolloverSmsMo;
    }

    public IncludedCounters rolloverSmsMo(Boolean rolloverSmsMo) {
        this.setRolloverSmsMo(rolloverSmsMo);
        return this;
    }

    public void setRolloverSmsMo(Boolean rolloverSmsMo) {
        this.rolloverSmsMo = rolloverSmsMo;
    }

    public Integer getRolloverSmsMoDuration() {
        return this.rolloverSmsMoDuration;
    }

    public IncludedCounters rolloverSmsMoDuration(Integer rolloverSmsMoDuration) {
        this.setRolloverSmsMoDuration(rolloverSmsMoDuration);
        return this;
    }

    public void setRolloverSmsMoDuration(Integer rolloverSmsMoDuration) {
        this.rolloverSmsMoDuration = rolloverSmsMoDuration;
    }

    public String getCounterDataUnit() {
        return this.counterDataUnit;
    }

    public IncludedCounters counterDataUnit(String counterDataUnit) {
        this.setCounterDataUnit(counterDataUnit);
        return this;
    }

    public void setCounterDataUnit(String counterDataUnit) {
        this.counterDataUnit = counterDataUnit;
    }

    public String getCounterSmsMoUnit() {
        return this.counterSmsMoUnit;
    }

    public IncludedCounters counterSmsMoUnit(String counterSmsMoUnit) {
        this.setCounterSmsMoUnit(counterSmsMoUnit);
        return this;
    }

    public void setCounterSmsMoUnit(String counterSmsMoUnit) {
        this.counterSmsMoUnit = counterSmsMoUnit;
    }

    public String getCounterSmsMtUnit() {
        return this.counterSmsMtUnit;
    }

    public IncludedCounters counterSmsMtUnit(String counterSmsMtUnit) {
        this.setCounterSmsMtUnit(counterSmsMtUnit);
        return this;
    }

    public void setCounterSmsMtUnit(String counterSmsMtUnit) {
        this.counterSmsMtUnit = counterSmsMtUnit;
    }

    public String getCounterVoiceMoUnit() {
        return this.counterVoiceMoUnit;
    }

    public IncludedCounters counterVoiceMoUnit(String counterVoiceMoUnit) {
        this.setCounterVoiceMoUnit(counterVoiceMoUnit);
        return this;
    }

    public void setCounterVoiceMoUnit(String counterVoiceMoUnit) {
        this.counterVoiceMoUnit = counterVoiceMoUnit;
    }

    public String getCounterVoiceMtUnit() {
        return this.counterVoiceMtUnit;
    }

    public IncludedCounters counterVoiceMtUnit(String counterVoiceMtUnit) {
        this.setCounterVoiceMtUnit(counterVoiceMtUnit);
        return this;
    }

    public void setCounterVoiceMtUnit(String counterVoiceMtUnit) {
        this.counterVoiceMtUnit = counterVoiceMtUnit;
    }

    public Boolean getRenewOnQuotaExhaust() {
        return this.renewOnQuotaExhaust;
    }

    public IncludedCounters renewOnQuotaExhaust(Boolean renewOnQuotaExhaust) {
        this.setRenewOnQuotaExhaust(renewOnQuotaExhaust);
        return this;
    }

    public void setRenewOnQuotaExhaust(Boolean renewOnQuotaExhaust) {
        this.renewOnQuotaExhaust = renewOnQuotaExhaust;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public IncludedCounters createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public IncludedCounters modifiedDate(Instant modifiedDate) {
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

    public IncludedCounters origZoneLabel(ZoneLabel zoneLabel) {
        this.setOrigZoneLabel(zoneLabel);
        return this;
    }

    public ZoneLabel getDestZoneLabel() {
        return this.destZoneLabel;
    }

    public void setDestZoneLabel(ZoneLabel zoneLabel) {
        this.destZoneLabel = zoneLabel;
    }

    public IncludedCounters destZoneLabel(ZoneLabel zoneLabel) {
        this.setDestZoneLabel(zoneLabel);
        return this;
    }

    public RatingGroup getRatingGroup() {
        return this.ratingGroup;
    }

    public void setRatingGroup(RatingGroup ratingGroup) {
        this.ratingGroup = ratingGroup;
    }

    public IncludedCounters ratingGroup(RatingGroup ratingGroup) {
        this.setRatingGroup(ratingGroup);
        return this;
    }

    public Rateplan getRateplan() {
        return this.rateplan;
    }

    public void setRateplan(Rateplan rateplan) {
        this.rateplan = rateplan;
    }

    public IncludedCounters rateplan(Rateplan rateplan) {
        this.setRateplan(rateplan);
        return this;
    }

    public ServiceList getServiceList() {
        return this.serviceList;
    }

    public void setServiceList(ServiceList serviceList) {
        this.serviceList = serviceList;
    }

    public IncludedCounters serviceList(ServiceList serviceList) {
        this.setServiceList(serviceList);
        return this;
    }

    public TimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public IncludedCounters timeSlot(TimeSlot timeSlot) {
        this.setTimeSlot(timeSlot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncludedCounters)) {
            return false;
        }
        return getId() != null && getId().equals(((IncludedCounters) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncludedCounters{" +
            "id=" + getId() +
            ", counterData=" + getCounterData() +
            ", counterSmsMo=" + getCounterSmsMo() +
            ", counterSmsMt=" + getCounterSmsMt() +
            ", counterVoiceMo=" + getCounterVoiceMo() +
            ", counterVoiceMt=" + getCounterVoiceMt() +
            ", rolloverData='" + getRolloverData() + "'" +
            ", rolloverDataDuration=" + getRolloverDataDuration() +
            ", rolloverVoiceMo='" + getRolloverVoiceMo() + "'" +
            ", rolloverVoiceMoDuration=" + getRolloverVoiceMoDuration() +
            ", rolloverSmsMo='" + getRolloverSmsMo() + "'" +
            ", rolloverSmsMoDuration=" + getRolloverSmsMoDuration() +
            ", counterDataUnit='" + getCounterDataUnit() + "'" +
            ", counterSmsMoUnit='" + getCounterSmsMoUnit() + "'" +
            ", counterSmsMtUnit='" + getCounterSmsMtUnit() + "'" +
            ", counterVoiceMoUnit='" + getCounterVoiceMoUnit() + "'" +
            ", counterVoiceMtUnit='" + getCounterVoiceMtUnit() + "'" +
            ", renewOnQuotaExhaust='" + getRenewOnQuotaExhaust() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
