package org.contextmapper.generated.referencemanagementcontext.service.impl;

import org.contextmapper.generated.referencemanagementcontext.service.PaperItemIDService;
import org.contextmapper.generated.referencemanagementcontext.domain.PaperItemID;
import org.contextmapper.generated.referencemanagementcontext.repository.PaperItemIDRepository;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemIDDTO;
import org.contextmapper.generated.referencemanagementcontext.service.mapper.PaperItemIDMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PaperItemID}.
 */
@Service
@Transactional
public class PaperItemIDServiceImpl implements PaperItemIDService {

    private final Logger log = LoggerFactory.getLogger(PaperItemIDServiceImpl.class);

    private final PaperItemIDRepository paperItemIDRepository;

    private final PaperItemIDMapper paperItemIDMapper;

    public PaperItemIDServiceImpl(PaperItemIDRepository paperItemIDRepository, PaperItemIDMapper paperItemIDMapper) {
        this.paperItemIDRepository = paperItemIDRepository;
        this.paperItemIDMapper = paperItemIDMapper;
    }

    /**
     * Save a paperItemID.
     *
     * @param paperItemIDDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaperItemIDDTO save(PaperItemIDDTO paperItemIDDTO) {
        log.debug("Request to save PaperItemID : {}", paperItemIDDTO);
        PaperItemID paperItemID = paperItemIDMapper.toEntity(paperItemIDDTO);
        paperItemID = paperItemIDRepository.save(paperItemID);
        return paperItemIDMapper.toDto(paperItemID);
    }

    /**
     * Get all the paperItemIDS.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaperItemIDDTO> findAll() {
        log.debug("Request to get all PaperItemIDS");
        return paperItemIDRepository.findAll().stream()
            .map(paperItemIDMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paperItemID by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaperItemIDDTO> findOne(Long id) {
        log.debug("Request to get PaperItemID : {}", id);
        return paperItemIDRepository.findById(id)
            .map(paperItemIDMapper::toDto);
    }

    /**
     * Delete the paperItemID by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaperItemID : {}", id);
        paperItemIDRepository.deleteById(id);
    }
}
