package org.contextmapper.generated.referencemanagementcontext.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperItemID} entity.
 */
public class PaperItemIDDTO implements Serializable {
    
    private Long id;

    private String doi;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaperItemIDDTO)) {
            return false;
        }

        return id != null && id.equals(((PaperItemIDDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaperItemIDDTO{" +
            "id=" + getId() +
            ", doi='" + getDoi() + "'" +
            "}";
    }
}
