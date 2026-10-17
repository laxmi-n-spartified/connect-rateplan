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
 * A NetworkIdentity.
 */
@Entity
@Table(name = "network_identity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NetworkIdentity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "mcc", length = 255)
    private String mcc;

    @Size(max = 255)
    @Column(name = "mnc", length = 255)
    private String mnc;

    @Size(max = 255)
    @Column(name = "operator", length = 255)
    private String operator;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "currency" }, allowSetters = true)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    private TadigCode tadigCode;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "networkIdentities")
    @JsonIgnoreProperties(value = { "networkIdentities" }, allowSetters = true)
    private Set<ZoneLabel> zoneLabels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NetworkIdentity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMcc() {
        return this.mcc;
    }

    public NetworkIdentity mcc(String mcc) {
        this.setMcc(mcc);
        return this;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return this.mnc;
    }

    public NetworkIdentity mnc(String mnc) {
        this.setMnc(mnc);
        return this;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getOperator() {
        return this.operator;
    }

    public NetworkIdentity operator(String operator) {
        this.setOperator(operator);
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public NetworkIdentity createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public NetworkIdentity modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public NetworkIdentity country(Country country) {
        this.setCountry(country);
        return this;
    }

    public TadigCode getTadigCode() {
        return this.tadigCode;
    }

    public void setTadigCode(TadigCode tadigCode) {
        this.tadigCode = tadigCode;
    }

    public NetworkIdentity tadigCode(TadigCode tadigCode) {
        this.setTadigCode(tadigCode);
        return this;
    }

    public Set<ZoneLabel> getZoneLabels() {
        return this.zoneLabels;
    }

    public void setZoneLabels(Set<ZoneLabel> zoneLabels) {
        if (this.zoneLabels != null) {
            this.zoneLabels.forEach(i -> i.removeNetworkIdentity(this));
        }
        if (zoneLabels != null) {
            zoneLabels.forEach(i -> i.addNetworkIdentity(this));
        }
        this.zoneLabels = zoneLabels;
    }

    public NetworkIdentity zoneLabels(Set<ZoneLabel> zoneLabels) {
        this.setZoneLabels(zoneLabels);
        return this;
    }

    public NetworkIdentity addZoneLabel(ZoneLabel zoneLabel) {
        this.zoneLabels.add(zoneLabel);
        zoneLabel.getNetworkIdentities().add(this);
        return this;
    }

    public NetworkIdentity removeZoneLabel(ZoneLabel zoneLabel) {
        this.zoneLabels.remove(zoneLabel);
        zoneLabel.getNetworkIdentities().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkIdentity)) {
            return false;
        }
        return getId() != null && getId().equals(((NetworkIdentity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetworkIdentity{" +
            "id=" + getId() +
            ", mcc='" + getMcc() + "'" +
            ", mnc='" + getMnc() + "'" +
            ", operator='" + getOperator() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
