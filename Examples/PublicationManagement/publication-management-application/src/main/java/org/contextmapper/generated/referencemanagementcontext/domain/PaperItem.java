package org.contextmapper.generated.referencemanagementcontext.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A PaperItem.
 */
@Entity
@Table(name = "paper_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaperItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column(name = "venue")
    private String venue;

    @OneToOne
    @JoinColumn(unique = true)
    private PaperItemID paperItemId;

    @ManyToOne
    @JsonIgnoreProperties(value = "paperItemLists", allowSetters = true)
    private PaperCollection paperCollection;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public PaperItem title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public PaperItem authors(String authors) {
        this.authors = authors;
        return this;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getVenue() {
        return venue;
    }

    public PaperItem venue(String venue) {
        this.venue = venue;
        return this;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public PaperItemID getPaperItemId() {
        return paperItemId;
    }

    public PaperItem paperItemId(PaperItemID paperItemID) {
        this.paperItemId = paperItemID;
        return this;
    }

    public void setPaperItemId(PaperItemID paperItemID) {
        this.paperItemId = paperItemID;
    }

    public PaperCollection getPaperCollection() {
        return paperCollection;
    }

    public PaperItem paperCollection(PaperCollection paperCollection) {
        this.paperCollection = paperCollection;
        return this;
    }

    public void setPaperCollection(PaperCollection paperCollection) {
        this.paperCollection = paperCollection;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaperItem)) {
            return false;
        }
        return id != null && id.equals(((PaperItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaperItem{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", venue='" + getVenue() + "'" +
            "}";
    }
}
