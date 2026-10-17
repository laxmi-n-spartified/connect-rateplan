package com.spartified.connectrateplan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A SgsnIpList.
 */
@Entity
@Table(name = "sgsn_ip_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SgsnIpList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "sgsn_ip", length = 255)
    private String sgsnIp;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country", "tadigCode", "zoneLabels" }, allowSetters = true)
    private NetworkIdentity networkIdentity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SgsnIpList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSgsnIp() {
        return this.sgsnIp;
    }

    public SgsnIpList sgsnIp(String sgsnIp) {
        this.setSgsnIp(sgsnIp);
        return this;
    }

    public void setSgsnIp(String sgsnIp) {
        this.sgsnIp = sgsnIp;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public SgsnIpList tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SgsnIpList createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public SgsnIpList modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public NetworkIdentity getNetworkIdentity() {
        return this.networkIdentity;
    }

    public void setNetworkIdentity(NetworkIdentity networkIdentity) {
        this.networkIdentity = networkIdentity;
    }

    public SgsnIpList networkIdentity(NetworkIdentity networkIdentity) {
        this.setNetworkIdentity(networkIdentity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SgsnIpList)) {
            return false;
        }
        return getId() != null && getId().equals(((SgsnIpList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SgsnIpList{" +
            "id=" + getId() +
            ", sgsnIp='" + getSgsnIp() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
