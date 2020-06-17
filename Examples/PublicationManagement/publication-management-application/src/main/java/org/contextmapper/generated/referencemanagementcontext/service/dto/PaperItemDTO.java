package org.contextmapper.generated.referencemanagementcontext.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperItem} entity.
 */
public class PaperItemDTO implements Serializable {
    
    private Long id;

    private String title;

    private String authors;

    private String venue;


    private Long paperItemIdId;

    private Long paperCollectionId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Long getPaperItemIdId() {
        return paperItemIdId;
    }

    public void setPaperItemIdId(Long paperItemIDId) {
        this.paperItemIdId = paperItemIDId;
    }

    public Long getPaperCollectionId() {
        return paperCollectionId;
    }

    public void setPaperCollectionId(Long paperCollectionId) {
        this.paperCollectionId = paperCollectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaperItemDTO paperItemDTO = (PaperItemDTO) o;
        if (paperItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paperItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaperItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", venue='" + getVenue() + "'" +
            ", paperItemIdId=" + getPaperItemIdId() +
            ", paperCollectionId=" + getPaperCollectionId() +
            "}";
    }
}
