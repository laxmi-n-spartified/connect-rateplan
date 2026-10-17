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
 * A ZoneLabel.
 */
@Entity
@Table(name = "zone_label")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ZoneLabel implements Serializable {

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

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_rest_of_world")
    private Boolean isRestOfWorld;

    @Size(max = 255)
    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_zone_label__network_identity",
        joinColumns = @JoinColumn(name = "zone_label_id"),
        inverseJoinColumns = @JoinColumn(name = "network_identity_id")
    )
    @JsonIgnoreProperties(value = { "country", "tadigCode", "zoneLabels" }, allowSetters = true)
    private Set<NetworkIdentity> networkIdentities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ZoneLabel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public ZoneLabel tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return this.name;
    }

    public ZoneLabel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ZoneLabel description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsRestOfWorld() {
        return this.isRestOfWorld;
    }

    public ZoneLabel isRestOfWorld(Boolean isRestOfWorld) {
        this.setIsRestOfWorld(isRestOfWorld);
        return this;
    }

    public void setIsRestOfWorld(Boolean isRestOfWorld) {
        this.isRestOfWorld = isRestOfWorld;
    }

    public String getStatus() {
        return this.status;
    }

    public ZoneLabel status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ZoneLabel createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public ZoneLabel modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<NetworkIdentity> getNetworkIdentities() {
        return this.networkIdentities;
    }

    public void setNetworkIdentities(Set<NetworkIdentity> networkIdentities) {
        this.networkIdentities = networkIdentities;
    }

    public ZoneLabel networkIdentities(Set<NetworkIdentity> networkIdentities) {
        this.setNetworkIdentities(networkIdentities);
        return this;
    }

    public ZoneLabel addNetworkIdentity(NetworkIdentity networkIdentity) {
        this.networkIdentities.add(networkIdentity);
        return this;
    }

    public ZoneLabel removeNetworkIdentity(NetworkIdentity networkIdentity) {
        this.networkIdentities.remove(networkIdentity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZoneLabel)) {
            return false;
        }
        return getId() != null && getId().equals(((ZoneLabel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZoneLabel{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isRestOfWorld='" + getIsRestOfWorld() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
