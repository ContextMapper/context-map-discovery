package org.contextmapper.generated.referencemanagementcontext.web.rest;

import org.contextmapper.generated.referencemanagementcontext.ReferenceManagementContextApp;
import org.contextmapper.generated.referencemanagementcontext.domain.PaperCollection;
import org.contextmapper.generated.referencemanagementcontext.repository.PaperCollectionRepository;
import org.contextmapper.generated.referencemanagementcontext.service.PaperCollectionService;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperCollectionDTO;
import org.contextmapper.generated.referencemanagementcontext.service.mapper.PaperCollectionMapper;

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
 * Integration tests for the {@link PaperCollectionResource} REST controller.
 */
@SpringBootTest(classes = ReferenceManagementContextApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PaperCollectionResourceIT {

    private static final Integer DEFAULT_PAPER_COLLECTION_ID = 1;
    private static final Integer UPDATED_PAPER_COLLECTION_ID = 2;

    @Autowired
    private PaperCollectionRepository paperCollectionRepository;

    @Autowired
    private PaperCollectionMapper paperCollectionMapper;

    @Autowired
    private PaperCollectionService paperCollectionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaperCollectionMockMvc;

    private PaperCollection paperCollection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperCollection createEntity(EntityManager em) {
        PaperCollection paperCollection = new PaperCollection()
            .paperCollectionId(DEFAULT_PAPER_COLLECTION_ID);
        return paperCollection;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperCollection createUpdatedEntity(EntityManager em) {
        PaperCollection paperCollection = new PaperCollection()
            .paperCollectionId(UPDATED_PAPER_COLLECTION_ID);
        return paperCollection;
    }

    @BeforeEach
    public void initTest() {
        paperCollection = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaperCollection() throws Exception {
        int databaseSizeBeforeCreate = paperCollectionRepository.findAll().size();

        // Create the PaperCollection
        PaperCollectionDTO paperCollectionDTO = paperCollectionMapper.toDto(paperCollection);
        restPaperCollectionMockMvc.perform(post("/api/paper-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperCollectionDTO)))
            .andExpect(status().isCreated());

        // Validate the PaperCollection in the database
        List<PaperCollection> paperCollectionList = paperCollectionRepository.findAll();
        assertThat(paperCollectionList).hasSize(databaseSizeBeforeCreate + 1);
        PaperCollection testPaperCollection = paperCollectionList.get(paperCollectionList.size() - 1);
        assertThat(testPaperCollection.getPaperCollectionId()).isEqualTo(DEFAULT_PAPER_COLLECTION_ID);
    }

    @Test
    @Transactional
    public void createPaperCollectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paperCollectionRepository.findAll().size();

        // Create the PaperCollection with an existing ID
        paperCollection.setId(1L);
        PaperCollectionDTO paperCollectionDTO = paperCollectionMapper.toDto(paperCollection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaperCollectionMockMvc.perform(post("/api/paper-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperCollectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaperCollection in the database
        List<PaperCollection> paperCollectionList = paperCollectionRepository.findAll();
        assertThat(paperCollectionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPaperCollections() throws Exception {
        // Initialize the database
        paperCollectionRepository.saveAndFlush(paperCollection);

        // Get all the paperCollectionList
        restPaperCollectionMockMvc.perform(get("/api/paper-collections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paperCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].paperCollectionId").value(hasItem(DEFAULT_PAPER_COLLECTION_ID)));
    }
    
    @Test
    @Transactional
    public void getPaperCollection() throws Exception {
        // Initialize the database
        paperCollectionRepository.saveAndFlush(paperCollection);

        // Get the paperCollection
        restPaperCollectionMockMvc.perform(get("/api/paper-collections/{id}", paperCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paperCollection.getId().intValue()))
            .andExpect(jsonPath("$.paperCollectionId").value(DEFAULT_PAPER_COLLECTION_ID));
    }

    @Test
    @Transactional
    public void getNonExistingPaperCollection() throws Exception {
        // Get the paperCollection
        restPaperCollectionMockMvc.perform(get("/api/paper-collections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaperCollection() throws Exception {
        // Initialize the database
        paperCollectionRepository.saveAndFlush(paperCollection);

        int databaseSizeBeforeUpdate = paperCollectionRepository.findAll().size();

        // Update the paperCollection
        PaperCollection updatedPaperCollection = paperCollectionRepository.findById(paperCollection.getId()).get();
        // Disconnect from session so that the updates on updatedPaperCollection are not directly saved in db
        em.detach(updatedPaperCollection);
        updatedPaperCollection
            .paperCollectionId(UPDATED_PAPER_COLLECTION_ID);
        PaperCollectionDTO paperCollectionDTO = paperCollectionMapper.toDto(updatedPaperCollection);

        restPaperCollectionMockMvc.perform(put("/api/paper-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperCollectionDTO)))
            .andExpect(status().isOk());

        // Validate the PaperCollection in the database
        List<PaperCollection> paperCollectionList = paperCollectionRepository.findAll();
        assertThat(paperCollectionList).hasSize(databaseSizeBeforeUpdate);
        PaperCollection testPaperCollection = paperCollectionList.get(paperCollectionList.size() - 1);
        assertThat(testPaperCollection.getPaperCollectionId()).isEqualTo(UPDATED_PAPER_COLLECTION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingPaperCollection() throws Exception {
        int databaseSizeBeforeUpdate = paperCollectionRepository.findAll().size();

        // Create the PaperCollection
        PaperCollectionDTO paperCollectionDTO = paperCollectionMapper.toDto(paperCollection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaperCollectionMockMvc.perform(put("/api/paper-collections")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperCollectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaperCollection in the database
        List<PaperCollection> paperCollectionList = paperCollectionRepository.findAll();
        assertThat(paperCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaperCollection() throws Exception {
        // Initialize the database
        paperCollectionRepository.saveAndFlush(paperCollection);

        int databaseSizeBeforeDelete = paperCollectionRepository.findAll().size();

        // Delete the paperCollection
        restPaperCollectionMockMvc.perform(delete("/api/paper-collections/{id}", paperCollection.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaperCollection> paperCollectionList = paperCollectionRepository.findAll();
        assertThat(paperCollectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
