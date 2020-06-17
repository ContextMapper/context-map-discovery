package org.contextmapper.generated.referencemanagementcontext.service.dto;

import java.io.Serializable;
import java.util.Objects;

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaperItemIDDTO paperItemIDDTO = (PaperItemIDDTO) o;
        if (paperItemIDDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paperItemIDDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaperItemIDDTO{" +
            "id=" + getId() +
            ", doi='" + getDoi() + "'" +
            "}";
    }
}
