package org.contextmapper.generated.referencemanagementcontext.web.rest;

import org.contextmapper.generated.referencemanagementcontext.ReferenceManagementContextApp;
import org.contextmapper.generated.referencemanagementcontext.domain.PaperItem;
import org.contextmapper.generated.referencemanagementcontext.repository.PaperItemRepository;
import org.contextmapper.generated.referencemanagementcontext.service.PaperItemService;
import org.contextmapper.generated.referencemanagementcontext.service.dto.PaperItemDTO;
import org.contextmapper.generated.referencemanagementcontext.service.mapper.PaperItemMapper;

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
 * Integration tests for the {@link PaperItemResource} REST controller.
 */
@SpringBootTest(classes = ReferenceManagementContextApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaperItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHORS = "AAAAAAAAAA";
    private static final String UPDATED_AUTHORS = "BBBBBBBBBB";

    private static final String DEFAULT_VENUE = "AAAAAAAAAA";
    private static final String UPDATED_VENUE = "BBBBBBBBBB";

    @Autowired
    private PaperItemRepository paperItemRepository;

    @Autowired
    private PaperItemMapper paperItemMapper;

    @Autowired
    private PaperItemService paperItemService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaperItemMockMvc;

    private PaperItem paperItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperItem createEntity(EntityManager em) {
        PaperItem paperItem = new PaperItem()
            .title(DEFAULT_TITLE)
            .authors(DEFAULT_AUTHORS)
            .venue(DEFAULT_VENUE);
        return paperItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaperItem createUpdatedEntity(EntityManager em) {
        PaperItem paperItem = new PaperItem()
            .title(UPDATED_TITLE)
            .authors(UPDATED_AUTHORS)
            .venue(UPDATED_VENUE);
        return paperItem;
    }

    @BeforeEach
    public void initTest() {
        paperItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaperItem() throws Exception {
        int databaseSizeBeforeCreate = paperItemRepository.findAll().size();
        // Create the PaperItem
        PaperItemDTO paperItemDTO = paperItemMapper.toDto(paperItem);
        restPaperItemMockMvc.perform(post("/api/paper-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PaperItem in the database
        List<PaperItem> paperItemList = paperItemRepository.findAll();
        assertThat(paperItemList).hasSize(databaseSizeBeforeCreate + 1);
        PaperItem testPaperItem = paperItemList.get(paperItemList.size() - 1);
        assertThat(testPaperItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPaperItem.getAuthors()).isEqualTo(DEFAULT_AUTHORS);
        assertThat(testPaperItem.getVenue()).isEqualTo(DEFAULT_VENUE);
    }

    @Test
    @Transactional
    public void createPaperItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paperItemRepository.findAll().size();

        // Create the PaperItem with an existing ID
        paperItem.setId(1L);
        PaperItemDTO paperItemDTO = paperItemMapper.toDto(paperItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaperItemMockMvc.perform(post("/api/paper-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaperItem in the database
        List<PaperItem> paperItemList = paperItemRepository.findAll();
        assertThat(paperItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPaperItems() throws Exception {
        // Initialize the database
        paperItemRepository.saveAndFlush(paperItem);

        // Get all the paperItemList
        restPaperItemMockMvc.perform(get("/api/paper-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paperItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].authors").value(hasItem(DEFAULT_AUTHORS)))
            .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE)));
    }
    
    @Test
    @Transactional
    public void getPaperItem() throws Exception {
        // Initialize the database
        paperItemRepository.saveAndFlush(paperItem);

        // Get the paperItem
        restPaperItemMockMvc.perform(get("/api/paper-items/{id}", paperItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paperItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.authors").value(DEFAULT_AUTHORS))
            .andExpect(jsonPath("$.venue").value(DEFAULT_VENUE));
    }
    @Test
    @Transactional
    public void getNonExistingPaperItem() throws Exception {
        // Get the paperItem
        restPaperItemMockMvc.perform(get("/api/paper-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaperItem() throws Exception {
        // Initialize the database
        paperItemRepository.saveAndFlush(paperItem);

        int databaseSizeBeforeUpdate = paperItemRepository.findAll().size();

        // Update the paperItem
        PaperItem updatedPaperItem = paperItemRepository.findById(paperItem.getId()).get();
        // Disconnect from session so that the updates on updatedPaperItem are not directly saved in db
        em.detach(updatedPaperItem);
        updatedPaperItem
            .title(UPDATED_TITLE)
            .authors(UPDATED_AUTHORS)
            .venue(UPDATED_VENUE);
        PaperItemDTO paperItemDTO = paperItemMapper.toDto(updatedPaperItem);

        restPaperItemMockMvc.perform(put("/api/paper-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemDTO)))
            .andExpect(status().isOk());

        // Validate the PaperItem in the database
        List<PaperItem> paperItemList = paperItemRepository.findAll();
        assertThat(paperItemList).hasSize(databaseSizeBeforeUpdate);
        PaperItem testPaperItem = paperItemList.get(paperItemList.size() - 1);
        assertThat(testPaperItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPaperItem.getAuthors()).isEqualTo(UPDATED_AUTHORS);
        assertThat(testPaperItem.getVenue()).isEqualTo(UPDATED_VENUE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaperItem() throws Exception {
        int databaseSizeBeforeUpdate = paperItemRepository.findAll().size();

        // Create the PaperItem
        PaperItemDTO paperItemDTO = paperItemMapper.toDto(paperItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaperItemMockMvc.perform(put("/api/paper-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paperItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaperItem in the database
        List<PaperItem> paperItemList = paperItemRepository.findAll();
        assertThat(paperItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaperItem() throws Exception {
        // Initialize the database
        paperItemRepository.saveAndFlush(paperItem);

        int databaseSizeBeforeDelete = paperItemRepository.findAll().size();

        // Delete the paperItem
        restPaperItemMockMvc.perform(delete("/api/paper-items/{id}", paperItem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaperItem> paperItemList = paperItemRepository.findAll();
        assertThat(paperItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
