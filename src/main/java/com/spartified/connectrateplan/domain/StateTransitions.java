package com.spartified.connectrateplan.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A StateTransitions.
 */
@Entity
@Table(name = "state_transitions")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StateTransitions implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "from_state", length = 255)
    private String fromState;

    @Size(max = 255)
    @Column(name = "to_state", length = 255)
    private String toState;

    @Column(name = "is_allowed")
    private Boolean isAllowed;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lifecycle lifecycle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StateTransitions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public StateTransitions name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromState() {
        return this.fromState;
    }

    public StateTransitions fromState(String fromState) {
        this.setFromState(fromState);
        return this;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getToState() {
        return this.toState;
    }

    public StateTransitions toState(String toState) {
        this.setToState(toState);
        return this;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public Boolean getIsAllowed() {
        return this.isAllowed;
    }

    public StateTransitions isAllowed(Boolean isAllowed) {
        this.setIsAllowed(isAllowed);
        return this;
    }

    public void setIsAllowed(Boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public StateTransitions createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public StateTransitions modifiedDate(Instant modifiedDate) {
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

    public StateTransitions lifecycle(Lifecycle lifecycle) {
        this.setLifecycle(lifecycle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StateTransitions)) {
            return false;
        }
        return getId() != null && getId().equals(((StateTransitions) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateTransitions{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fromState='" + getFromState() + "'" +
            ", toState='" + getToState() + "'" +
            ", isAllowed='" + getIsAllowed() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
