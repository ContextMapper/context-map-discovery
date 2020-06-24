package org.contextmapper.generated.referencemanagementcontext.service;

import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperCollectionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperCollection}.
 */
public interface PaperCollectionService {

    /**
     * Save a paperCollection.
     *
     * @param paperCollectionDTO the entity to save.
     * @return the persisted entity.
     */
    PaperCollectionDTO save(PaperCollectionDTO paperCollectionDTO);

    /**
     * Get all the paperCollections.
     *
     * @return the list of entities.
     */
    List<PaperCollectionDTO> findAll();


    /**
     * Get the "id" paperCollection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaperCollectionDTO> findOne(Long id);

    /**
     * Delete the "id" paperCollection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
