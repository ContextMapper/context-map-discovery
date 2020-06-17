package org.contextmapper.generated.referencemanagementcontext.web.rest;

import org.contextmapper.generated.referencemanagementcontext.ReferenceManagementContextApp;
import org.contextmapper.generated.referencemanagementcontext.domain.PaperItemID;
import org.contextmapper.generated.referencemanagementcontext.repository.PaperItemIDRepository;
import org.contextmapper.generated.referencemanagementcontext.service.PaperItemIDService;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemIDDTO;
import org.contextmapper.generated.referencemanagementcontext.service.mapper.PaperItemIDMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaperItemIDResource} REST controller.
 */
@SpringBootTest(classes = ReferenceManagementContextApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PaperItemIDResourceIT {

    private static final String DEFAULT_DOI = "AAAAAAAAAA";
    private static final String UPDATED_DOI = "BBBBBBBBBB";

    @Autowired
    private PaperItemIDRepository paperItemIDRepository;

    @Autowired
    private PaperItemIDMapper paperItemIDMapper;

    @Autowired
    private PaperItemIDService paperItemIDService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaperItemIDMockMvc;

    private PaperItemID paperItemID;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperItemID createEntity(EntityManager em) {
        PaperItemID paperItemID = new PaperItemID()
            .doi(DEFAULT_DOI);
        return paperItemID;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperItemID createUpdatedEntity(EntityManager em) {
        PaperItemID paperItemID = new PaperItemID()
            .doi(UPDATED_DOI);
        return paperItemID;
    }

    @BeforeEach
    public void initTest() {
        paperItemID = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaperItemID() throws Exception {
        int databaseSizeBeforeCreate = paperItemIDRepository.findAll().size();

        // Create the PaperItemID
        PaperItemIDDTO paperItemIDDTO = paperItemIDMapper.toDto(paperItemID);
        restPaperItemIDMockMvc.perform(post("/api/paper-item-ids")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemIDDTO)))
            .andExpect(status().isCreated());

        // Validate the PaperItemID in the database
        List<PaperItemID> paperItemIDList = paperItemIDRepository.findAll();
        assertThat(paperItemIDList).hasSize(databaseSizeBeforeCreate + 1);
        PaperItemID testPaperItemID = paperItemIDList.get(paperItemIDList.size() - 1);
        assertThat(testPaperItemID.getDoi()).isEqualTo(DEFAULT_DOI);
    }

    @Test
    @Transactional
    public void createPaperItemIDWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paperItemIDRepository.findAll().size();

        // Create the PaperItemID with an existing ID
        paperItemID.setId(1L);
        PaperItemIDDTO paperItemIDDTO = paperItemIDMapper.toDto(paperItemID);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaperItemIDMockMvc.perform(post("/api/paper-item-ids")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemIDDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaperItemID in the database
        List<PaperItemID> paperItemIDList = paperItemIDRepository.findAll();
        assertThat(paperItemIDList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPaperItemIDS() throws Exception {
        // Initialize the database
        paperItemIDRepository.saveAndFlush(paperItemID);

        // Get all the paperItemIDList
        restPaperItemIDMockMvc.perform(get("/api/paper-item-ids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paperItemID.getId().intValue())))
            .andExpect(jsonPath("$.[*].doi").value(hasItem(DEFAULT_DOI)));
    }
    
    @Test
    @Transactional
    public void getPaperItemID() throws Exception {
        // Initialize the database
        paperItemIDRepository.saveAndFlush(paperItemID);

        // Get the paperItemID
        restPaperItemIDMockMvc.perform(get("/api/paper-item-ids/{id}", paperItemID.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paperItemID.getId().intValue()))
            .andExpect(jsonPath("$.doi").value(DEFAULT_DOI));
    }

    @Test
    @Transactional
    public void getNonExistingPaperItemID() throws Exception {
        // Get the paperItemID
        restPaperItemIDMockMvc.perform(get("/api/paper-item-ids/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaperItemID() throws Exception {
        // Initialize the database
        paperItemIDRepository.saveAndFlush(paperItemID);

        int databaseSizeBeforeUpdate = paperItemIDRepository.findAll().size();

        // Update the paperItemID
        PaperItemID updatedPaperItemID = paperItemIDRepository.findById(paperItemID.getId()).get();
        // Disconnect from session so that the updates on updatedPaperItemID are not directly saved in db
        em.detach(updatedPaperItemID);
        updatedPaperItemID
            .doi(UPDATED_DOI);
        PaperItemIDDTO paperItemIDDTO = paperItemIDMapper.toDto(updatedPaperItemID);

        restPaperItemIDMockMvc.perform(put("/api/paper-item-ids")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemIDDTO)))
            .andExpect(status().isOk());

        // Validate the PaperItemID in the database
        List<PaperItemID> paperItemIDList = paperItemIDRepository.findAll();
        assertThat(paperItemIDList).hasSize(databaseSizeBeforeUpdate);
        PaperItemID testPaperItemID = paperItemIDList.get(paperItemIDList.size() - 1);
        assertThat(testPaperItemID.getDoi()).isEqualTo(UPDATED_DOI);
    }

    @Test
    @Transactional
    public void updateNonExistingPaperItemID() throws Exception {
        int databaseSizeBeforeUpdate = paperItemIDRepository.findAll().size();

        // Create the PaperItemID
        PaperItemIDDTO paperItemIDDTO = paperItemIDMapper.toDto(paperItemID);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaperItemIDMockMvc.perform(put("/api/paper-item-ids")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemIDDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaperItemID in the database
        List<PaperItemID> paperItemIDList = paperItemIDRepository.findAll();
        assertThat(paperItemIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaperItemID() throws Exception {
        // Initialize the database
        paperItemIDRepository.saveAndFlush(paperItemID);

        int databaseSizeBeforeDelete = paperItemIDRepository.findAll().size();

        // Delete the paperItemID
        restPaperItemIDMockMvc.perform(delete("/api/paper-item-ids/{id}", paperItemID.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaperItemID> paperItemIDList = paperItemIDRepository.findAll();
        assertThat(paperItemIDList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
