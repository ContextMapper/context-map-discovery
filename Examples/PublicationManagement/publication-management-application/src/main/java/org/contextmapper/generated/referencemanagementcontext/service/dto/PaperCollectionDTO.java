package org.contextmapper.generated.referencemanagementcontext.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperCollection} entity.
 */
public class PaperCollectionDTO implements Serializable {
    
    private Long id;

    private Integer paperCollectionId;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPaperCollectionId() {
        return paperCollectionId;
    }

    public void setPaperCollectionId(Integer paperCollectionId) {
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

        PaperCollectionDTO paperCollectionDTO = (PaperCollectionDTO) o;
        if (paperCollectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paperCollectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaperCollectionDTO{" +
            "id=" + getId() +
            ", paperCollectionId=" + getPaperCollectionId() +
            "}";
    }
}
