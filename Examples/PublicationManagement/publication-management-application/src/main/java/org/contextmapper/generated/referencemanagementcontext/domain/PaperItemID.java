package org.contextmapper.generated.referencemanagementcontext.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PaperItemID.
 */
@Entity
@Table(name = "paper_item_id")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaperItemID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doi")
    private String doi;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    public PaperItemID doi(String doi) {
        this.doi = doi;
        return this;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaperItemID)) {
            return false;
        }
        return id != null && id.equals(((PaperItemID) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PaperItemID{" +
            "id=" + getId() +
            ", doi='" + getDoi() + "'" +
            "}";
    }
}
