package org.contextmapper.generated.referencemanagementcontext.service.impl;

import org.contextmapper.generated.referencemanagementcontext.service.PaperItemService;
import org.contextmapper.generated.referencemanagementcontext.domain.PaperItem;
import org.contextmapper.generated.referencemanagementcontext.repository.PaperItemRepository;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemDTO;
import org.contextmapper.generated.referencemanagementcontext.service.mapper.PaperItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PaperItem}.
 */
@Service
@Transactional
public class PaperItemServiceImpl implements PaperItemService {

    private final Logger log = LoggerFactory.getLogger(PaperItemServiceImpl.class);

    private final PaperItemRepository paperItemRepository;

    private final PaperItemMapper paperItemMapper;

    public PaperItemServiceImpl(PaperItemRepository paperItemRepository, PaperItemMapper paperItemMapper) {
        this.paperItemRepository = paperItemRepository;
        this.paperItemMapper = paperItemMapper;
    }

    /**
     * Save a paperItem.
     *
     * @param paperItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PaperItemDTO save(PaperItemDTO paperItemDTO) {
        log.debug("Request to save PaperItem : {}", paperItemDTO);
        PaperItem paperItem = paperItemMapper.toEntity(paperItemDTO);
        paperItem = paperItemRepository.save(paperItem);
        return paperItemMapper.toDto(paperItem);
    }

    /**
     * Get all the paperItems.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaperItemDTO> findAll() {
        log.debug("Request to get all PaperItems");
        return paperItemRepository.findAll().stream()
            .map(paperItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one paperItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaperItemDTO> findOne(Long id) {
        log.debug("Request to get PaperItem : {}", id);
        return paperItemRepository.findById(id)
            .map(paperItemMapper::toDto);
    }

    /**
     * Delete the paperItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaperItem : {}", id);
        paperItemRepository.deleteById(id);
    }
}
