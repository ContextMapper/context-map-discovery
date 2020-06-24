package org.contextmapper.generated.referencemanagementcontext.service.impl;

import org.contextmapper.generated.referencemanagementcontext.service.PaperCollectionService;
import org.contextmapper.generated.referencemanagementcontext.domain.PaperCollection;
import org.contextmapper.generated.referencemanagementcontext.repository.PaperCollectionRepository;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperCollectionDTO;
import org.contextmapper.generated.referencemanagementcontext.service.mapper.PaperCollectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PaperCollection}.
 */
@Service
@Transactional
public class PaperCollectionServiceImpl implements PaperCollectionService {

    private final Logger log = LoggerFactory.getLogger(PaperCollectionServiceImpl.class);

    private final PaperCollectionRepository paperCollectionRepository;

    private final PaperCollectionMapper paperCollectionMapper;

    public PaperCollectionServiceImpl(PaperCollectionRepository paperCollectionRepository, PaperCollectionMapper paperCollectionMapper) {
        this.paperCollectionRepository = paperCollectionRepository;
        this.paperCollectionMapper = paperCollectionMapper;
    }

    /**
     * Save a paperCollection.
     *
     * @param paperCollectionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaperCollectionDTO save(PaperCollectionDTO paperCollectionDTO) {
        log.debug("Request to save PaperCollection : {}", paperCollectionDTO);
        PaperCollection paperCollection = paperCollectionMapper.toEntity(paperCollectionDTO);
        paperCollection = paperCollectionRepository.save(paperCollection);
        return paperCollectionMapper.toDto(paperCollection);
    }

    /**
     * Get all the paperCollections.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaperCollectionDTO> findAll() {
        log.debug("Request to get all PaperCollections");
        return paperCollectionRepository.findAll().stream()
            .map(paperCollectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one paperCollection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaperCollectionDTO> findOne(Long id) {
        log.debug("Request to get PaperCollection : {}", id);
        return paperCollectionRepository.findById(id)
            .map(paperCollectionMapper::toDto);
    }

    /**
     * Delete the paperCollection by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaperCollection : {}", id);
        paperCollectionRepository.deleteById(id);
    }
}
