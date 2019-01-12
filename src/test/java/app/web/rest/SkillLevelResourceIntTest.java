package app.web.rest;

import app.AppApp;

import app.domain.SkillLevel;
import app.repository.SkillLevelRepository;
import app.service.SkillLevelService;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.SkillLevelCriteria;
import app.service.SkillLevelQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SkillLevelResource REST controller.
 *
 * @see SkillLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class SkillLevelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SkillLevelRepository skillLevelRepository;
    
    @Autowired
    private SkillLevelService skillLevelService;

    @Autowired
    private SkillLevelQueryService skillLevelQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkillLevelMockMvc;

    private SkillLevel skillLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkillLevelResource skillLevelResource = new SkillLevelResource(skillLevelService, skillLevelQueryService);
        this.restSkillLevelMockMvc = MockMvcBuilders.standaloneSetup(skillLevelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillLevel createEntity(EntityManager em) {
        SkillLevel skillLevel = new SkillLevel()
            .name(DEFAULT_NAME);
        return skillLevel;
    }

    @Before
    public void initTest() {
        skillLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkillLevel() throws Exception {
        int databaseSizeBeforeCreate = skillLevelRepository.findAll().size();

        // Create the SkillLevel
        restSkillLevelMockMvc.perform(post("/api/skill-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillLevel)))
            .andExpect(status().isCreated());

        // Validate the SkillLevel in the database
        List<SkillLevel> skillLevelList = skillLevelRepository.findAll();
        assertThat(skillLevelList).hasSize(databaseSizeBeforeCreate + 1);
        SkillLevel testSkillLevel = skillLevelList.get(skillLevelList.size() - 1);
        assertThat(testSkillLevel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSkillLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skillLevelRepository.findAll().size();

        // Create the SkillLevel with an existing ID
        skillLevel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillLevelMockMvc.perform(post("/api/skill-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillLevel)))
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        List<SkillLevel> skillLevelList = skillLevelRepository.findAll();
        assertThat(skillLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSkillLevels() throws Exception {
        // Initialize the database
        skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList
        restSkillLevelMockMvc.perform(get("/api/skill-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getSkillLevel() throws Exception {
        // Initialize the database
        skillLevelRepository.saveAndFlush(skillLevel);

        // Get the skillLevel
        restSkillLevelMockMvc.perform(get("/api/skill-levels/{id}", skillLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skillLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllSkillLevelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where name equals to DEFAULT_NAME
        defaultSkillLevelShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the skillLevelList where name equals to UPDATED_NAME
        defaultSkillLevelShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSkillLevelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSkillLevelShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the skillLevelList where name equals to UPDATED_NAME
        defaultSkillLevelShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSkillLevelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        skillLevelRepository.saveAndFlush(skillLevel);

        // Get all the skillLevelList where name is not null
        defaultSkillLevelShouldBeFound("name.specified=true");

        // Get all the skillLevelList where name is null
        defaultSkillLevelShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSkillLevelShouldBeFound(String filter) throws Exception {
        restSkillLevelMockMvc.perform(get("/api/skill-levels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSkillLevelShouldNotBeFound(String filter) throws Exception {
        restSkillLevelMockMvc.perform(get("/api/skill-levels?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSkillLevel() throws Exception {
        // Get the skillLevel
        restSkillLevelMockMvc.perform(get("/api/skill-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillLevel() throws Exception {
        // Initialize the database
        skillLevelService.save(skillLevel);

        int databaseSizeBeforeUpdate = skillLevelRepository.findAll().size();

        // Update the skillLevel
        SkillLevel updatedSkillLevel = skillLevelRepository.findById(skillLevel.getId()).get();
        // Disconnect from session so that the updates on updatedSkillLevel are not directly saved in db
        em.detach(updatedSkillLevel);
        updatedSkillLevel
            .name(UPDATED_NAME);

        restSkillLevelMockMvc.perform(put("/api/skill-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkillLevel)))
            .andExpect(status().isOk());

        // Validate the SkillLevel in the database
        List<SkillLevel> skillLevelList = skillLevelRepository.findAll();
        assertThat(skillLevelList).hasSize(databaseSizeBeforeUpdate);
        SkillLevel testSkillLevel = skillLevelList.get(skillLevelList.size() - 1);
        assertThat(testSkillLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSkillLevel() throws Exception {
        int databaseSizeBeforeUpdate = skillLevelRepository.findAll().size();

        // Create the SkillLevel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillLevelMockMvc.perform(put("/api/skill-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillLevel)))
            .andExpect(status().isBadRequest());

        // Validate the SkillLevel in the database
        List<SkillLevel> skillLevelList = skillLevelRepository.findAll();
        assertThat(skillLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSkillLevel() throws Exception {
        // Initialize the database
        skillLevelService.save(skillLevel);

        int databaseSizeBeforeDelete = skillLevelRepository.findAll().size();

        // Get the skillLevel
        restSkillLevelMockMvc.perform(delete("/api/skill-levels/{id}", skillLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SkillLevel> skillLevelList = skillLevelRepository.findAll();
        assertThat(skillLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillLevel.class);
        SkillLevel skillLevel1 = new SkillLevel();
        skillLevel1.setId(1L);
        SkillLevel skillLevel2 = new SkillLevel();
        skillLevel2.setId(skillLevel1.getId());
        assertThat(skillLevel1).isEqualTo(skillLevel2);
        skillLevel2.setId(2L);
        assertThat(skillLevel1).isNotEqualTo(skillLevel2);
        skillLevel1.setId(null);
        assertThat(skillLevel1).isNotEqualTo(skillLevel2);
    }
}
