package app.web.rest;

import app.AppApp;

import app.domain.CvFile;
import app.repository.CvFileRepository;
import app.service.CvFileService;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CvFileResource REST controller.
 *
 * @see CvFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class CvFileResourceIntTest {

    private static final byte[] DEFAULT_CV = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CV = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CV_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CV_CONTENT_TYPE = "image/png";

    @Autowired
    private CvFileRepository cvFileRepository;
    
    @Autowired
    private CvFileService cvFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCvFileMockMvc;

    private CvFile cvFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CvFileResource cvFileResource = new CvFileResource(cvFileService);
        this.restCvFileMockMvc = MockMvcBuilders.standaloneSetup(cvFileResource)
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
    public static CvFile createEntity(EntityManager em) {
        CvFile cvFile = new CvFile()
            .cv(DEFAULT_CV)
            .cvContentType(DEFAULT_CV_CONTENT_TYPE);
        return cvFile;
    }

    @Before
    public void initTest() {
        cvFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createCvFile() throws Exception {
        int databaseSizeBeforeCreate = cvFileRepository.findAll().size();

        // Create the CvFile
        restCvFileMockMvc.perform(post("/api/cv-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cvFile)))
            .andExpect(status().isCreated());

        // Validate the CvFile in the database
        List<CvFile> cvFileList = cvFileRepository.findAll();
        assertThat(cvFileList).hasSize(databaseSizeBeforeCreate + 1);
        CvFile testCvFile = cvFileList.get(cvFileList.size() - 1);
        assertThat(testCvFile.getCv()).isEqualTo(DEFAULT_CV);
        assertThat(testCvFile.getCvContentType()).isEqualTo(DEFAULT_CV_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createCvFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cvFileRepository.findAll().size();

        // Create the CvFile with an existing ID
        cvFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCvFileMockMvc.perform(post("/api/cv-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cvFile)))
            .andExpect(status().isBadRequest());

        // Validate the CvFile in the database
        List<CvFile> cvFileList = cvFileRepository.findAll();
        assertThat(cvFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCvFiles() throws Exception {
        // Initialize the database
        cvFileRepository.saveAndFlush(cvFile);

        // Get all the cvFileList
        restCvFileMockMvc.perform(get("/api/cv-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cvFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].cvContentType").value(hasItem(DEFAULT_CV_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cv").value(hasItem(Base64Utils.encodeToString(DEFAULT_CV))));
    }
    
    @Test
    @Transactional
    public void getCvFile() throws Exception {
        // Initialize the database
        cvFileRepository.saveAndFlush(cvFile);

        // Get the cvFile
        restCvFileMockMvc.perform(get("/api/cv-files/{id}", cvFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cvFile.getId().intValue()))
            .andExpect(jsonPath("$.cvContentType").value(DEFAULT_CV_CONTENT_TYPE))
            .andExpect(jsonPath("$.cv").value(Base64Utils.encodeToString(DEFAULT_CV)));
    }

    @Test
    @Transactional
    public void getNonExistingCvFile() throws Exception {
        // Get the cvFile
        restCvFileMockMvc.perform(get("/api/cv-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCvFile() throws Exception {
        // Initialize the database
        cvFileService.save(cvFile);

        int databaseSizeBeforeUpdate = cvFileRepository.findAll().size();

        // Update the cvFile
        CvFile updatedCvFile = cvFileRepository.findById(cvFile.getId()).get();
        // Disconnect from session so that the updates on updatedCvFile are not directly saved in db
        em.detach(updatedCvFile);
        updatedCvFile
            .cv(UPDATED_CV)
            .cvContentType(UPDATED_CV_CONTENT_TYPE);

        restCvFileMockMvc.perform(put("/api/cv-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCvFile)))
            .andExpect(status().isOk());

        // Validate the CvFile in the database
        List<CvFile> cvFileList = cvFileRepository.findAll();
        assertThat(cvFileList).hasSize(databaseSizeBeforeUpdate);
        CvFile testCvFile = cvFileList.get(cvFileList.size() - 1);
        assertThat(testCvFile.getCv()).isEqualTo(UPDATED_CV);
        assertThat(testCvFile.getCvContentType()).isEqualTo(UPDATED_CV_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCvFile() throws Exception {
        int databaseSizeBeforeUpdate = cvFileRepository.findAll().size();

        // Create the CvFile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCvFileMockMvc.perform(put("/api/cv-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cvFile)))
            .andExpect(status().isBadRequest());

        // Validate the CvFile in the database
        List<CvFile> cvFileList = cvFileRepository.findAll();
        assertThat(cvFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCvFile() throws Exception {
        // Initialize the database
        cvFileService.save(cvFile);

        int databaseSizeBeforeDelete = cvFileRepository.findAll().size();

        // Get the cvFile
        restCvFileMockMvc.perform(delete("/api/cv-files/{id}", cvFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CvFile> cvFileList = cvFileRepository.findAll();
        assertThat(cvFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CvFile.class);
        CvFile cvFile1 = new CvFile();
        cvFile1.setId(1L);
        CvFile cvFile2 = new CvFile();
        cvFile2.setId(cvFile1.getId());
        assertThat(cvFile1).isEqualTo(cvFile2);
        cvFile2.setId(2L);
        assertThat(cvFile1).isNotEqualTo(cvFile2);
        cvFile1.setId(null);
        assertThat(cvFile1).isNotEqualTo(cvFile2);
    }
}
