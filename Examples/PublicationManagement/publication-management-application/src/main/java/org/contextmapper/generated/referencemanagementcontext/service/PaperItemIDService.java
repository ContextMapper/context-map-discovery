package org.contextmapper.generated.referencemanagementcontext.service;

import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemIDDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperItemID}.
 */
public interface PaperItemIDService {

    /**
     * Save a paperItemID.
     *
     * @param paperItemIDDTO the entity to save.
     * @return the persisted entity.
     */
    PaperItemIDDTO save(PaperItemIDDTO paperItemIDDTO);

    /**
     * Get all the paperItemIDS.
     *
     * @return the list of entities.
     */
    List<PaperItemIDDTO> findAll();


    /**
     * Get the "id" paperItemID.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaperItemIDDTO> findOne(Long id);

    /**
     * Delete the "id" paperItemID.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
