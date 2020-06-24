package org.contextmapper.generated.referencemanagementcontext.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PaperCollection.
 */
@Entity
@Table(name = "paper_collection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaperCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paper_collection_id")
    private Integer paperCollectionId;

    @OneToMany(mappedBy = "paperCollection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PaperItem> paperItemLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPaperCollectionId() {
        return paperCollectionId;
    }

    public PaperCollection paperCollectionId(Integer paperCollectionId) {
        this.paperCollectionId = paperCollectionId;
        return this;
    }

    public void setPaperCollectionId(Integer paperCollectionId) {
        this.paperCollectionId = paperCollectionId;
    }

    public Set<PaperItem> getPaperItemLists() {
        return paperItemLists;
    }

    public PaperCollection paperItemLists(Set<PaperItem> paperItems) {
        this.paperItemLists = paperItems;
        return this;
    }

    public PaperCollection addPaperItemList(PaperItem paperItem) {
        this.paperItemLists.add(paperItem);
        paperItem.setPaperCollection(this);
        return this;
    }

    public PaperCollection removePaperItemList(PaperItem paperItem) {
        this.paperItemLists.remove(paperItem);
        paperItem.setPaperCollection(null);
        return this;
    }

    public void setPaperItemLists(Set<PaperItem> paperItems) {
        this.paperItemLists = paperItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaperCollection)) {
            return false;
        }
        return id != null && id.equals(((PaperCollection) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaperCollection{" +
            "id=" + getId() +
            ", paperCollectionId=" + getPaperCollectionId() +
            "}";
    }
}
