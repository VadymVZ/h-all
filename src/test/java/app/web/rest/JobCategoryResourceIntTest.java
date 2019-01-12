package app.web.rest;

import app.AppApp;

import app.domain.JobCategory;
import app.repository.JobCategoryRepository;
import app.service.JobCategoryService;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.JobCategoryCriteria;
import app.service.JobCategoryQueryService;

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
 * Test class for the JobCategoryResource REST controller.
 *
 * @see JobCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class JobCategoryResourceIntTest {

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private JobCategoryRepository jobCategoryRepository;
    
    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private JobCategoryQueryService jobCategoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobCategoryMockMvc;

    private JobCategory jobCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobCategoryResource jobCategoryResource = new JobCategoryResource(jobCategoryService, jobCategoryQueryService);
        this.restJobCategoryMockMvc = MockMvcBuilders.standaloneSetup(jobCategoryResource)
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
    public static JobCategory createEntity(EntityManager em) {
        JobCategory jobCategory = new JobCategory()
            .parentId(DEFAULT_PARENT_ID)
            .name(DEFAULT_NAME);
        return jobCategory;
    }

    @Before
    public void initTest() {
        jobCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobCategory() throws Exception {
        int databaseSizeBeforeCreate = jobCategoryRepository.findAll().size();

        // Create the JobCategory
        restJobCategoryMockMvc.perform(post("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCategory)))
            .andExpect(status().isCreated());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        JobCategory testJobCategory = jobCategoryList.get(jobCategoryList.size() - 1);
        assertThat(testJobCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testJobCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createJobCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobCategoryRepository.findAll().size();

        // Create the JobCategory with an existing ID
        jobCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobCategoryMockMvc.perform(post("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCategory)))
            .andExpect(status().isBadRequest());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobCategories() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList
        restJobCategoryMockMvc.perform(get("/api/job-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getJobCategory() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get the jobCategory
        restJobCategoryMockMvc.perform(get("/api/job-categories/{id}", jobCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobCategory.getId().intValue()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByParentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where parentId equals to DEFAULT_PARENT_ID
        defaultJobCategoryShouldBeFound("parentId.equals=" + DEFAULT_PARENT_ID);

        // Get all the jobCategoryList where parentId equals to UPDATED_PARENT_ID
        defaultJobCategoryShouldNotBeFound("parentId.equals=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByParentIdIsInShouldWork() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where parentId in DEFAULT_PARENT_ID or UPDATED_PARENT_ID
        defaultJobCategoryShouldBeFound("parentId.in=" + DEFAULT_PARENT_ID + "," + UPDATED_PARENT_ID);

        // Get all the jobCategoryList where parentId equals to UPDATED_PARENT_ID
        defaultJobCategoryShouldNotBeFound("parentId.in=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByParentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where parentId is not null
        defaultJobCategoryShouldBeFound("parentId.specified=true");

        // Get all the jobCategoryList where parentId is null
        defaultJobCategoryShouldNotBeFound("parentId.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByParentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where parentId greater than or equals to DEFAULT_PARENT_ID
        defaultJobCategoryShouldBeFound("parentId.greaterOrEqualThan=" + DEFAULT_PARENT_ID);

        // Get all the jobCategoryList where parentId greater than or equals to UPDATED_PARENT_ID
        defaultJobCategoryShouldNotBeFound("parentId.greaterOrEqualThan=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByParentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where parentId less than or equals to DEFAULT_PARENT_ID
        defaultJobCategoryShouldNotBeFound("parentId.lessThan=" + DEFAULT_PARENT_ID);

        // Get all the jobCategoryList where parentId less than or equals to UPDATED_PARENT_ID
        defaultJobCategoryShouldBeFound("parentId.lessThan=" + UPDATED_PARENT_ID);
    }


    @Test
    @Transactional
    public void getAllJobCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where name equals to DEFAULT_NAME
        defaultJobCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the jobCategoryList where name equals to UPDATED_NAME
        defaultJobCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultJobCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the jobCategoryList where name equals to UPDATED_NAME
        defaultJobCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJobCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobCategoryRepository.saveAndFlush(jobCategory);

        // Get all the jobCategoryList where name is not null
        defaultJobCategoryShouldBeFound("name.specified=true");

        // Get all the jobCategoryList where name is null
        defaultJobCategoryShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultJobCategoryShouldBeFound(String filter) throws Exception {
        restJobCategoryMockMvc.perform(get("/api/job-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultJobCategoryShouldNotBeFound(String filter) throws Exception {
        restJobCategoryMockMvc.perform(get("/api/job-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingJobCategory() throws Exception {
        // Get the jobCategory
        restJobCategoryMockMvc.perform(get("/api/job-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobCategory() throws Exception {
        // Initialize the database
        jobCategoryService.save(jobCategory);

        int databaseSizeBeforeUpdate = jobCategoryRepository.findAll().size();

        // Update the jobCategory
        JobCategory updatedJobCategory = jobCategoryRepository.findById(jobCategory.getId()).get();
        // Disconnect from session so that the updates on updatedJobCategory are not directly saved in db
        em.detach(updatedJobCategory);
        updatedJobCategory
            .parentId(UPDATED_PARENT_ID)
            .name(UPDATED_NAME);

        restJobCategoryMockMvc.perform(put("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobCategory)))
            .andExpect(status().isOk());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeUpdate);
        JobCategory testJobCategory = jobCategoryList.get(jobCategoryList.size() - 1);
        assertThat(testJobCategory.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testJobCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingJobCategory() throws Exception {
        int databaseSizeBeforeUpdate = jobCategoryRepository.findAll().size();

        // Create the JobCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobCategoryMockMvc.perform(put("/api/job-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCategory)))
            .andExpect(status().isBadRequest());

        // Validate the JobCategory in the database
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobCategory() throws Exception {
        // Initialize the database
        jobCategoryService.save(jobCategory);

        int databaseSizeBeforeDelete = jobCategoryRepository.findAll().size();

        // Get the jobCategory
        restJobCategoryMockMvc.perform(delete("/api/job-categories/{id}", jobCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        assertThat(jobCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobCategory.class);
        JobCategory jobCategory1 = new JobCategory();
        jobCategory1.setId(1L);
        JobCategory jobCategory2 = new JobCategory();
        jobCategory2.setId(jobCategory1.getId());
        assertThat(jobCategory1).isEqualTo(jobCategory2);
        jobCategory2.setId(2L);
        assertThat(jobCategory1).isNotEqualTo(jobCategory2);
        jobCategory1.setId(null);
        assertThat(jobCategory1).isNotEqualTo(jobCategory2);
    }
}
