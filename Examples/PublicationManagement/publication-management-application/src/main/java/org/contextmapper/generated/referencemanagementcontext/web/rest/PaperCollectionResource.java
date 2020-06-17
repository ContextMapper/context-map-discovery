package org.contextmapper.generated.referencemanagementcontext.web.rest;

import org.contextmapper.generated.referencemanagementcontext.service.PaperCollectionService;
import org.contextmapper.generated.referencemanagementcontext.web.rest.errors.BadRequestAlertException;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperCollectionDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperCollection}.
 */
@RestController
@RequestMapping("/api")
public class PaperCollectionResource {

    private final Logger log = LoggerFactory.getLogger(PaperCollectionResource.class);

    private static final String ENTITY_NAME = "referenceManagementContextPaperCollection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaperCollectionService paperCollectionService;

    public PaperCollectionResource(PaperCollectionService paperCollectionService) {
        this.paperCollectionService = paperCollectionService;
    }

    /**
     * {@code POST  /paper-collections} : Create a new paperCollection.
     *
     * @param paperCollectionDTO the paperCollectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paperCollectionDTO, or with status {@code 400 (Bad Request)} if the paperCollection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paper-collections")
    public ResponseEntity<PaperCollectionDTO> createPaperCollection(@RequestBody PaperCollectionDTO paperCollectionDTO) throws URISyntaxException {
        log.debug("REST request to save PaperCollection : {}", paperCollectionDTO);
        if (paperCollectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paperCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaperCollectionDTO result = paperCollectionService.save(paperCollectionDTO);
        return ResponseEntity.created(new URI("/api/paper-collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paper-collections} : Updates an existing paperCollection.
     *
     * @param paperCollectionDTO the paperCollectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paperCollectionDTO,
     * or with status {@code 400 (Bad Request)} if the paperCollectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paperCollectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paper-collections")
    public ResponseEntity<PaperCollectionDTO> updatePaperCollection(@RequestBody PaperCollectionDTO paperCollectionDTO) throws URISyntaxException {
        log.debug("REST request to update PaperCollection : {}", paperCollectionDTO);
        if (paperCollectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaperCollectionDTO result = paperCollectionService.save(paperCollectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paperCollectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paper-collections} : get all the paperCollections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paperCollections in body.
     */
    @GetMapping("/paper-collections")
    public List<PaperCollectionDTO> getAllPaperCollections() {
        log.debug("REST request to get all PaperCollections");
        return paperCollectionService.findAll();
    }

    /**
     * {@code GET  /paper-collections/:id} : get the "id" paperCollection.
     *
     * @param id the id of the paperCollectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paperCollectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paper-collections/{id}")
    public ResponseEntity<PaperCollectionDTO> getPaperCollection(@PathVariable Long id) {
        log.debug("REST request to get PaperCollection : {}", id);
        Optional<PaperCollectionDTO> paperCollectionDTO = paperCollectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paperCollectionDTO);
    }

    /**
     * {@code DELETE  /paper-collections/:id} : delete the "id" paperCollection.
     *
     * @param id the id of the paperCollectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paper-collections/{id}")
    public ResponseEntity<Void> deletePaperCollection(@PathVariable Long id) {
        log.debug("REST request to delete PaperCollection : {}", id);
        paperCollectionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
