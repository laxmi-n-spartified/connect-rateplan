package com.spartified.connectrateplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "country_code")
    private Integer countryCode;

    @Size(max = 255)
    @Column(name = "country_name", length = 255)
    private String countryName;

    @Size(max = 255)
    @Column(name = "iso_3166_code_2", length = 255)
    private String iso3166Code2;

    @Size(max = 255)
    @Column(name = "iso_3166_code_3", length = 255)
    private String iso3166Code3;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Currency currency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountryCode() {
        return this.countryCode;
    }

    public Country countryCode(Integer countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public Country countryName(String countryName) {
        this.setCountryName(countryName);
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIso3166Code2() {
        return this.iso3166Code2;
    }

    public Country iso3166Code2(String iso3166Code2) {
        this.setIso3166Code2(iso3166Code2);
        return this;
    }

    public void setIso3166Code2(String iso3166Code2) {
        this.iso3166Code2 = iso3166Code2;
    }

    public String getIso3166Code3() {
        return this.iso3166Code3;
    }

    public Country iso3166Code3(String iso3166Code3) {
        this.setIso3166Code3(iso3166Code3);
        return this;
    }

    public void setIso3166Code3(String iso3166Code3) {
        this.iso3166Code3 = iso3166Code3;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Country createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public Country modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Country currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", countryCode=" + getCountryCode() +
            ", countryName='" + getCountryName() + "'" +
            ", iso3166Code2='" + getIso3166Code2() + "'" +
            ", iso3166Code3='" + getIso3166Code3() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
