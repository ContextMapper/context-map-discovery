package org.contextmapper.generated.referencemanagementcontext.web.rest;

import org.contextmapper.generated.referencemanagementcontext.service.PaperItemService;
import org.contextmapper.generated.referencemanagementcontext.web.rest.errors.BadRequestAlertException;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemDTO;

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
 * REST controller for managing {@link org.contextmapper.generated.referencemanagementcontext.domain.PaperItem}.
 */
@RestController
@RequestMapping("/api")
public class PaperItemResource {

    private final Logger log = LoggerFactory.getLogger(PaperItemResource.class);

    private static final String ENTITY_NAME = "referenceManagementContextPaperItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaperItemService paperItemService;

    public PaperItemResource(PaperItemService paperItemService) {
        this.paperItemService = paperItemService;
    }

    /**
     * {@code POST  /paper-items} : Create a new paperItem.
     *
     * @param paperItemDTO the paperItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paperItemDTO, or with status {@code 400 (Bad Request)} if the paperItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paper-items")
    public ResponseEntity<PaperItemDTO> createPaperItem(@RequestBody PaperItemDTO paperItemDTO) throws URISyntaxException {
        log.debug("REST request to save PaperItem : {}", paperItemDTO);
        if (paperItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new paperItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaperItemDTO result = paperItemService.save(paperItemDTO);
        return ResponseEntity.created(new URI("/api/paper-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paper-items} : Updates an existing paperItem.
     *
     * @param paperItemDTO the paperItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paperItemDTO,
     * or with status {@code 400 (Bad Request)} if the paperItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paperItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paper-items")
    public ResponseEntity<PaperItemDTO> updatePaperItem(@RequestBody PaperItemDTO paperItemDTO) throws URISyntaxException {
        log.debug("REST request to update PaperItem : {}", paperItemDTO);
        if (paperItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaperItemDTO result = paperItemService.save(paperItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paperItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paper-items} : get all the paperItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paperItems in body.
     */
    @GetMapping("/paper-items")
    public List<PaperItemDTO> getAllPaperItems() {
        log.debug("REST request to get all PaperItems");
        return paperItemService.findAll();
    }

    /**
     * {@code GET  /paper-items/:id} : get the "id" paperItem.
     *
     * @param id the id of the paperItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paperItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paper-items/{id}")
    public ResponseEntity<PaperItemDTO> getPaperItem(@PathVariable Long id) {
        log.debug("REST request to get PaperItem : {}", id);
        Optional<PaperItemDTO> paperItemDTO = paperItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paperItemDTO);
    }

    /**
     * {@code DELETE  /paper-items/:id} : delete the "id" paperItem.
     *
     * @param id the id of the paperItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paper-items/{id}")
    public ResponseEntity<Void> deletePaperItem(@PathVariable Long id) {
        log.debug("REST request to delete PaperItem : {}", id);
        paperItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
