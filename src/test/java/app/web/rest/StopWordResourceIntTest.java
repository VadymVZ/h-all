package app.web.rest;

import app.AppApp;

import app.domain.StopWord;
import app.repository.StopWordRepository;
import app.service.StopWordService;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.StopWordCriteria;
import app.service.StopWordQueryService;

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
 * Test class for the StopWordResource REST controller.
 *
 * @see StopWordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class StopWordResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    @Autowired
    private StopWordRepository stopWordRepository;
    
    @Autowired
    private StopWordService stopWordService;

    @Autowired
    private StopWordQueryService stopWordQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStopWordMockMvc;

    private StopWord stopWord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StopWordResource stopWordResource = new StopWordResource(stopWordService, stopWordQueryService);
        this.restStopWordMockMvc = MockMvcBuilders.standaloneSetup(stopWordResource)
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
    public static StopWord createEntity(EntityManager em) {
        StopWord stopWord = new StopWord()
            .key(DEFAULT_KEY);
        return stopWord;
    }

    @Before
    public void initTest() {
        stopWord = createEntity(em);
    }

    @Test
    @Transactional
    public void createStopWord() throws Exception {
        int databaseSizeBeforeCreate = stopWordRepository.findAll().size();

        // Create the StopWord
        restStopWordMockMvc.perform(post("/api/stop-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopWord)))
            .andExpect(status().isCreated());

        // Validate the StopWord in the database
        List<StopWord> stopWordList = stopWordRepository.findAll();
        assertThat(stopWordList).hasSize(databaseSizeBeforeCreate + 1);
        StopWord testStopWord = stopWordList.get(stopWordList.size() - 1);
        assertThat(testStopWord.getKey()).isEqualTo(DEFAULT_KEY);
    }

    @Test
    @Transactional
    public void createStopWordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stopWordRepository.findAll().size();

        // Create the StopWord with an existing ID
        stopWord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStopWordMockMvc.perform(post("/api/stop-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopWord)))
            .andExpect(status().isBadRequest());

        // Validate the StopWord in the database
        List<StopWord> stopWordList = stopWordRepository.findAll();
        assertThat(stopWordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStopWords() throws Exception {
        // Initialize the database
        stopWordRepository.saveAndFlush(stopWord);

        // Get all the stopWordList
        restStopWordMockMvc.perform(get("/api/stop-words?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stopWord.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())));
    }
    
    @Test
    @Transactional
    public void getStopWord() throws Exception {
        // Initialize the database
        stopWordRepository.saveAndFlush(stopWord);

        // Get the stopWord
        restStopWordMockMvc.perform(get("/api/stop-words/{id}", stopWord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stopWord.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()));
    }

    @Test
    @Transactional
    public void getAllStopWordsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        stopWordRepository.saveAndFlush(stopWord);

        // Get all the stopWordList where key equals to DEFAULT_KEY
        defaultStopWordShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the stopWordList where key equals to UPDATED_KEY
        defaultStopWordShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllStopWordsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        stopWordRepository.saveAndFlush(stopWord);

        // Get all the stopWordList where key in DEFAULT_KEY or UPDATED_KEY
        defaultStopWordShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the stopWordList where key equals to UPDATED_KEY
        defaultStopWordShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllStopWordsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        stopWordRepository.saveAndFlush(stopWord);

        // Get all the stopWordList where key is not null
        defaultStopWordShouldBeFound("key.specified=true");

        // Get all the stopWordList where key is null
        defaultStopWordShouldNotBeFound("key.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStopWordShouldBeFound(String filter) throws Exception {
        restStopWordMockMvc.perform(get("/api/stop-words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stopWord.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStopWordShouldNotBeFound(String filter) throws Exception {
        restStopWordMockMvc.perform(get("/api/stop-words?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingStopWord() throws Exception {
        // Get the stopWord
        restStopWordMockMvc.perform(get("/api/stop-words/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStopWord() throws Exception {
        // Initialize the database
        stopWordService.save(stopWord);

        int databaseSizeBeforeUpdate = stopWordRepository.findAll().size();

        // Update the stopWord
        StopWord updatedStopWord = stopWordRepository.findById(stopWord.getId()).get();
        // Disconnect from session so that the updates on updatedStopWord are not directly saved in db
        em.detach(updatedStopWord);
        updatedStopWord
            .key(UPDATED_KEY);

        restStopWordMockMvc.perform(put("/api/stop-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStopWord)))
            .andExpect(status().isOk());

        // Validate the StopWord in the database
        List<StopWord> stopWordList = stopWordRepository.findAll();
        assertThat(stopWordList).hasSize(databaseSizeBeforeUpdate);
        StopWord testStopWord = stopWordList.get(stopWordList.size() - 1);
        assertThat(testStopWord.getKey()).isEqualTo(UPDATED_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingStopWord() throws Exception {
        int databaseSizeBeforeUpdate = stopWordRepository.findAll().size();

        // Create the StopWord

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStopWordMockMvc.perform(put("/api/stop-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stopWord)))
            .andExpect(status().isBadRequest());

        // Validate the StopWord in the database
        List<StopWord> stopWordList = stopWordRepository.findAll();
        assertThat(stopWordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStopWord() throws Exception {
        // Initialize the database
        stopWordService.save(stopWord);

        int databaseSizeBeforeDelete = stopWordRepository.findAll().size();

        // Get the stopWord
        restStopWordMockMvc.perform(delete("/api/stop-words/{id}", stopWord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StopWord> stopWordList = stopWordRepository.findAll();
        assertThat(stopWordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StopWord.class);
        StopWord stopWord1 = new StopWord();
        stopWord1.setId(1L);
        StopWord stopWord2 = new StopWord();
        stopWord2.setId(stopWord1.getId());
        assertThat(stopWord1).isEqualTo(stopWord2);
        stopWord2.setId(2L);
        assertThat(stopWord1).isNotEqualTo(stopWord2);
        stopWord1.setId(null);
        assertThat(stopWord1).isNotEqualTo(stopWord2);
    }
}
