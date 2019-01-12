package app.web.rest;

import app.AppApp;

import app.domain.Occupation;
import app.repository.OccupationRepository;
import app.service.OccupationService;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.OccupationCriteria;
import app.service.OccupationQueryService;

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
 * Test class for the OccupationResource REST controller.
 *
 * @see OccupationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class OccupationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private OccupationRepository occupationRepository;
    
    @Autowired
    private OccupationService occupationService;

    @Autowired
    private OccupationQueryService occupationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOccupationMockMvc;

    private Occupation occupation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OccupationResource occupationResource = new OccupationResource(occupationService, occupationQueryService);
        this.restOccupationMockMvc = MockMvcBuilders.standaloneSetup(occupationResource)
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
    public static Occupation createEntity(EntityManager em) {
        Occupation occupation = new Occupation()
            .name(DEFAULT_NAME);
        return occupation;
    }

    @Before
    public void initTest() {
        occupation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOccupation() throws Exception {
        int databaseSizeBeforeCreate = occupationRepository.findAll().size();

        // Create the Occupation
        restOccupationMockMvc.perform(post("/api/occupations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(occupation)))
            .andExpect(status().isCreated());

        // Validate the Occupation in the database
        List<Occupation> occupationList = occupationRepository.findAll();
        assertThat(occupationList).hasSize(databaseSizeBeforeCreate + 1);
        Occupation testOccupation = occupationList.get(occupationList.size() - 1);
        assertThat(testOccupation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createOccupationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = occupationRepository.findAll().size();

        // Create the Occupation with an existing ID
        occupation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOccupationMockMvc.perform(post("/api/occupations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(occupation)))
            .andExpect(status().isBadRequest());

        // Validate the Occupation in the database
        List<Occupation> occupationList = occupationRepository.findAll();
        assertThat(occupationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOccupations() throws Exception {
        // Initialize the database
        occupationRepository.saveAndFlush(occupation);

        // Get all the occupationList
        restOccupationMockMvc.perform(get("/api/occupations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(occupation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getOccupation() throws Exception {
        // Initialize the database
        occupationRepository.saveAndFlush(occupation);

        // Get the occupation
        restOccupationMockMvc.perform(get("/api/occupations/{id}", occupation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(occupation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllOccupationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        occupationRepository.saveAndFlush(occupation);

        // Get all the occupationList where name equals to DEFAULT_NAME
        defaultOccupationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the occupationList where name equals to UPDATED_NAME
        defaultOccupationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOccupationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        occupationRepository.saveAndFlush(occupation);

        // Get all the occupationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOccupationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the occupationList where name equals to UPDATED_NAME
        defaultOccupationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOccupationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        occupationRepository.saveAndFlush(occupation);

        // Get all the occupationList where name is not null
        defaultOccupationShouldBeFound("name.specified=true");

        // Get all the occupationList where name is null
        defaultOccupationShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOccupationShouldBeFound(String filter) throws Exception {
        restOccupationMockMvc.perform(get("/api/occupations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(occupation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOccupationShouldNotBeFound(String filter) throws Exception {
        restOccupationMockMvc.perform(get("/api/occupations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingOccupation() throws Exception {
        // Get the occupation
        restOccupationMockMvc.perform(get("/api/occupations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOccupation() throws Exception {
        // Initialize the database
        occupationService.save(occupation);

        int databaseSizeBeforeUpdate = occupationRepository.findAll().size();

        // Update the occupation
        Occupation updatedOccupation = occupationRepository.findById(occupation.getId()).get();
        // Disconnect from session so that the updates on updatedOccupation are not directly saved in db
        em.detach(updatedOccupation);
        updatedOccupation
            .name(UPDATED_NAME);

        restOccupationMockMvc.perform(put("/api/occupations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOccupation)))
            .andExpect(status().isOk());

        // Validate the Occupation in the database
        List<Occupation> occupationList = occupationRepository.findAll();
        assertThat(occupationList).hasSize(databaseSizeBeforeUpdate);
        Occupation testOccupation = occupationList.get(occupationList.size() - 1);
        assertThat(testOccupation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingOccupation() throws Exception {
        int databaseSizeBeforeUpdate = occupationRepository.findAll().size();

        // Create the Occupation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOccupationMockMvc.perform(put("/api/occupations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(occupation)))
            .andExpect(status().isBadRequest());

        // Validate the Occupation in the database
        List<Occupation> occupationList = occupationRepository.findAll();
        assertThat(occupationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOccupation() throws Exception {
        // Initialize the database
        occupationService.save(occupation);

        int databaseSizeBeforeDelete = occupationRepository.findAll().size();

        // Get the occupation
        restOccupationMockMvc.perform(delete("/api/occupations/{id}", occupation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Occupation> occupationList = occupationRepository.findAll();
        assertThat(occupationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Occupation.class);
        Occupation occupation1 = new Occupation();
        occupation1.setId(1L);
        Occupation occupation2 = new Occupation();
        occupation2.setId(occupation1.getId());
        assertThat(occupation1).isEqualTo(occupation2);
        occupation2.setId(2L);
        assertThat(occupation1).isNotEqualTo(occupation2);
        occupation1.setId(null);
        assertThat(occupation1).isNotEqualTo(occupation2);
    }
}
