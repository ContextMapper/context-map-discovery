package org.contextmapper.generated.referencemanagementcontext.service;

import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperItem}.
 */
public interface PaperItemService {

    /**
     * Save a paperItem.
     *
     * @param paperItemDTO the entity to save.
     * @return the persisted entity.
     */
    PaperItemDTO save(PaperItemDTO paperItemDTO);

    /**
     * Get all the paperItems.
     *
     * @return the list of entities.
     */
    List<PaperItemDTO> findAll();


    /**
     * Get the "id" paperItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaperItemDTO> findOne(Long id);

    /**
     * Delete the "id" paperItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
