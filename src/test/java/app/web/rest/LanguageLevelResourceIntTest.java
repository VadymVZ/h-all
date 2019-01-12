package app.web.rest;

import app.AppApp;

import app.domain.LanguageLevel;
import app.repository.LanguageLevelRepository;
import app.web.rest.errors.ExceptionTranslator;

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
 * Test class for the LanguageLevelResource REST controller.
 *
 * @see LanguageLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class LanguageLevelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private LanguageLevelRepository languageLevelRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLanguageLevelMockMvc;

    private LanguageLevel languageLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LanguageLevelResource languageLevelResource = new LanguageLevelResource(languageLevelRepository);
        this.restLanguageLevelMockMvc = MockMvcBuilders.standaloneSetup(languageLevelResource)
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
    public static LanguageLevel createEntity(EntityManager em) {
        LanguageLevel languageLevel = new LanguageLevel()
            .name(DEFAULT_NAME);
        return languageLevel;
    }

    @Before
    public void initTest() {
        languageLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguageLevel() throws Exception {
        int databaseSizeBeforeCreate = languageLevelRepository.findAll().size();

        // Create the LanguageLevel
        restLanguageLevelMockMvc.perform(post("/api/language-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLevel)))
            .andExpect(status().isCreated());

        // Validate the LanguageLevel in the database
        List<LanguageLevel> languageLevelList = languageLevelRepository.findAll();
        assertThat(languageLevelList).hasSize(databaseSizeBeforeCreate + 1);
        LanguageLevel testLanguageLevel = languageLevelList.get(languageLevelList.size() - 1);
        assertThat(testLanguageLevel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createLanguageLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = languageLevelRepository.findAll().size();

        // Create the LanguageLevel with an existing ID
        languageLevel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageLevelMockMvc.perform(post("/api/language-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLevel)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageLevel in the database
        List<LanguageLevel> languageLevelList = languageLevelRepository.findAll();
        assertThat(languageLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLanguageLevels() throws Exception {
        // Initialize the database
        languageLevelRepository.saveAndFlush(languageLevel);

        // Get all the languageLevelList
        restLanguageLevelMockMvc.perform(get("/api/language-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languageLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelRepository.saveAndFlush(languageLevel);

        // Get the languageLevel
        restLanguageLevelMockMvc.perform(get("/api/language-levels/{id}", languageLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(languageLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguageLevel() throws Exception {
        // Get the languageLevel
        restLanguageLevelMockMvc.perform(get("/api/language-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelRepository.saveAndFlush(languageLevel);

        int databaseSizeBeforeUpdate = languageLevelRepository.findAll().size();

        // Update the languageLevel
        LanguageLevel updatedLanguageLevel = languageLevelRepository.findById(languageLevel.getId()).get();
        // Disconnect from session so that the updates on updatedLanguageLevel are not directly saved in db
        em.detach(updatedLanguageLevel);
        updatedLanguageLevel
            .name(UPDATED_NAME);

        restLanguageLevelMockMvc.perform(put("/api/language-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLanguageLevel)))
            .andExpect(status().isOk());

        // Validate the LanguageLevel in the database
        List<LanguageLevel> languageLevelList = languageLevelRepository.findAll();
        assertThat(languageLevelList).hasSize(databaseSizeBeforeUpdate);
        LanguageLevel testLanguageLevel = languageLevelList.get(languageLevelList.size() - 1);
        assertThat(testLanguageLevel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingLanguageLevel() throws Exception {
        int databaseSizeBeforeUpdate = languageLevelRepository.findAll().size();

        // Create the LanguageLevel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageLevelMockMvc.perform(put("/api/language-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLevel)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageLevel in the database
        List<LanguageLevel> languageLevelList = languageLevelRepository.findAll();
        assertThat(languageLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLanguageLevel() throws Exception {
        // Initialize the database
        languageLevelRepository.saveAndFlush(languageLevel);

        int databaseSizeBeforeDelete = languageLevelRepository.findAll().size();

        // Get the languageLevel
        restLanguageLevelMockMvc.perform(delete("/api/language-levels/{id}", languageLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LanguageLevel> languageLevelList = languageLevelRepository.findAll();
        assertThat(languageLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageLevel.class);
        LanguageLevel languageLevel1 = new LanguageLevel();
        languageLevel1.setId(1L);
        LanguageLevel languageLevel2 = new LanguageLevel();
        languageLevel2.setId(languageLevel1.getId());
        assertThat(languageLevel1).isEqualTo(languageLevel2);
        languageLevel2.setId(2L);
        assertThat(languageLevel1).isNotEqualTo(languageLevel2);
        languageLevel1.setId(null);
        assertThat(languageLevel1).isNotEqualTo(languageLevel2);
    }
}
