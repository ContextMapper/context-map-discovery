package org.contextmapper.generated.referencemanagementcontext.web.rest;

import org.contextmapper.generated.referencemanagementcontext.service.PaperItemIDService;
import org.contextmapper.generated.referencemanagementcontext.web.rest.errors.BadRequestAlertException;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemIDDTO;

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
 * REST controller for managing {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperItemID}.
 */
@RestController
@RequestMapping("/api")
public class PaperItemIDResource {

    private final Logger log = LoggerFactory.getLogger(PaperItemIDResource.class);

    private static final String ENTITY_NAME = "referenceManagementContextPaperItemId";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaperItemIDService paperItemIDService;

    public PaperItemIDResource(PaperItemIDService paperItemIDService) {
        this.paperItemIDService = paperItemIDService;
    }

    /**
     * {@code POST  /paper-item-ids} : Create a new paperItemID.
     *
     * @param paperItemIDDTO the paperItemIDDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paperItemIDDTO, or with status {@code 400 (Bad Request)} if the paperItemID has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paper-item-ids")
    public ResponseEntity<PaperItemIDDTO> createPaperItemID(@RequestBody PaperItemIDDTO paperItemIDDTO) throws URISyntaxException {
        log.debug("REST request to save PaperItemID : {}", paperItemIDDTO);
        if (paperItemIDDTO.getId() != null) {
            throw new BadRequestAlertException("A new paperItemID cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaperItemIDDTO result = paperItemIDService.save(paperItemIDDTO);
        return ResponseEntity.created(new URI("/api/paper-item-ids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paper-item-ids} : Updates an existing paperItemID.
     *
     * @param paperItemIDDTO the paperItemIDDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paperItemIDDTO,
     * or with status {@code 400 (Bad Request)} if the paperItemIDDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paperItemIDDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paper-item-ids")
    public ResponseEntity<PaperItemIDDTO> updatePaperItemID(@RequestBody PaperItemIDDTO paperItemIDDTO) throws URISyntaxException {
        log.debug("REST request to update PaperItemID : {}", paperItemIDDTO);
        if (paperItemIDDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaperItemIDDTO result = paperItemIDService.save(paperItemIDDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paperItemIDDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paper-item-ids} : get all the paperItemIDS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paperItemIDS in body.
     */
    @GetMapping("/paper-item-ids")
    public List<PaperItemIDDTO> getAllPaperItemIDS() {
        log.debug("REST request to get all PaperItemIDS");
        return paperItemIDService.findAll();
    }

    /**
     * {@code GET  /paper-item-ids/:id} : get the "id" paperItemID.
     *
     * @param id the id of the paperItemIDDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paperItemIDDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paper-item-ids/{id}")
    public ResponseEntity<PaperItemIDDTO> getPaperItemID(@PathVariable Long id) {
        log.debug("REST request to get PaperItemID : {}", id);
        Optional<PaperItemIDDTO> paperItemIDDTO = paperItemIDService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paperItemIDDTO);
    }

    /**
     * {@code DELETE  /paper-item-ids/:id} : delete the "id" paperItemID.
     *
     * @param id the id of the paperItemIDDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paper-item-ids/{id}")
    public ResponseEntity<Void> deletePaperItemID(@PathVariable Long id) {
        log.debug("REST request to delete PaperItemID : {}", id);
        paperItemIDService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
