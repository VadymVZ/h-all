package app.web.rest;

import app.AppApp;

import app.domain.Profile;
import app.repository.ProfileRepository;
import app.service.ProfileService;
import app.service.dto.ProfileDTO;
import app.service.mapper.ProfileMapper;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.ProfileCriteria;
import app.service.ProfileQueryService;

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
import java.math.BigDecimal;
import java.util.List;


import static app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class ProfileResourceIntTest {

    private static final BigDecimal DEFAULT_SALARY_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXPERIENCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPERIENCE = new BigDecimal(2);

    private static final String DEFAULT_JOB_EXPECTATIONS = "AAAAAAAAAA";
    private static final String UPDATED_JOB_EXPECTATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_ACHIEVEMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ACHIEVEMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_NAME = "BBBBBBBBBB";

    private static final byte[] UPDATED_Y = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_Y_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_Y_CONTENT_TYPE = "image/png";

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;
    
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
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
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .salaryAmount(DEFAULT_SALARY_AMOUNT)
            .city(DEFAULT_CITY)
            .description(DEFAULT_DESCRIPTION)
            .experience(DEFAULT_EXPERIENCE)
            .jobExpectations(DEFAULT_JOB_EXPECTATIONS)
            .achievements(DEFAULT_ACHIEVEMENTS)
            .positionName(DEFAULT_POSITION_NAME);
        return profile;
    }

    @Before
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getSalaryAmount()).isEqualTo(DEFAULT_SALARY_AMOUNT);
        assertThat(testProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProfile.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testProfile.getJobExpectations()).isEqualTo(DEFAULT_JOB_EXPECTATIONS);
        assertThat(testProfile.getAchievements()).isEqualTo(DEFAULT_ACHIEVEMENTS);
        assertThat(testProfile.getPositionName()).isEqualTo(DEFAULT_POSITION_NAME);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaryAmount").value(hasItem(DEFAULT_SALARY_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.intValue())))
            .andExpect(jsonPath("$.[*].jobExpectations").value(hasItem(DEFAULT_JOB_EXPECTATIONS.toString())))
            .andExpect(jsonPath("$.[*].achievements").value(hasItem(DEFAULT_ACHIEVEMENTS.toString())))
            .andExpect(jsonPath("$.[*].positionName").value(hasItem(DEFAULT_POSITION_NAME.toString())))
            .andExpect(jsonPath("$.[*].yContentType").value(hasItem(DEFAULT_Y_CONTENT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.salaryAmount").value(DEFAULT_SALARY_AMOUNT.intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE.intValue()))
            .andExpect(jsonPath("$.jobExpectations").value(DEFAULT_JOB_EXPECTATIONS.toString()))
            .andExpect(jsonPath("$.achievements").value(DEFAULT_ACHIEVEMENTS.toString()))
            .andExpect(jsonPath("$.positionName").value(DEFAULT_POSITION_NAME.toString()))
            .andExpect(jsonPath("$.yContentType").value(DEFAULT_Y_CONTENT_TYPE));
    }

    @Test
    @Transactional
    public void getAllProfilesBySalaryAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where salaryAmount equals to DEFAULT_SALARY_AMOUNT
        defaultProfileShouldBeFound("salaryAmount.equals=" + DEFAULT_SALARY_AMOUNT);

        // Get all the profileList where salaryAmount equals to UPDATED_SALARY_AMOUNT
        defaultProfileShouldNotBeFound("salaryAmount.equals=" + UPDATED_SALARY_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProfilesBySalaryAmountIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where salaryAmount in DEFAULT_SALARY_AMOUNT or UPDATED_SALARY_AMOUNT
        defaultProfileShouldBeFound("salaryAmount.in=" + DEFAULT_SALARY_AMOUNT + "," + UPDATED_SALARY_AMOUNT);

        // Get all the profileList where salaryAmount equals to UPDATED_SALARY_AMOUNT
        defaultProfileShouldNotBeFound("salaryAmount.in=" + UPDATED_SALARY_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllProfilesBySalaryAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where salaryAmount is not null
        defaultProfileShouldBeFound("salaryAmount.specified=true");

        // Get all the profileList where salaryAmount is null
        defaultProfileShouldNotBeFound("salaryAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city equals to DEFAULT_CITY
        defaultProfileShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the profileList where city equals to UPDATED_CITY
        defaultProfileShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city in DEFAULT_CITY or UPDATED_CITY
        defaultProfileShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the profileList where city equals to UPDATED_CITY
        defaultProfileShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city is not null
        defaultProfileShouldBeFound("city.specified=true");

        // Get all the profileList where city is null
        defaultProfileShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where description equals to DEFAULT_DESCRIPTION
        defaultProfileShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the profileList where description equals to UPDATED_DESCRIPTION
        defaultProfileShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProfilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProfileShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the profileList where description equals to UPDATED_DESCRIPTION
        defaultProfileShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProfilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where description is not null
        defaultProfileShouldBeFound("description.specified=true");

        // Get all the profileList where description is null
        defaultProfileShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experience equals to DEFAULT_EXPERIENCE
        defaultProfileShouldBeFound("experience.equals=" + DEFAULT_EXPERIENCE);

        // Get all the profileList where experience equals to UPDATED_EXPERIENCE
        defaultProfileShouldNotBeFound("experience.equals=" + UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experience in DEFAULT_EXPERIENCE or UPDATED_EXPERIENCE
        defaultProfileShouldBeFound("experience.in=" + DEFAULT_EXPERIENCE + "," + UPDATED_EXPERIENCE);

        // Get all the profileList where experience equals to UPDATED_EXPERIENCE
        defaultProfileShouldNotBeFound("experience.in=" + UPDATED_EXPERIENCE);
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experience is not null
        defaultProfileShouldBeFound("experience.specified=true");

        // Get all the profileList where experience is null
        defaultProfileShouldNotBeFound("experience.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByJobExpectationsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where jobExpectations equals to DEFAULT_JOB_EXPECTATIONS
        defaultProfileShouldBeFound("jobExpectations.equals=" + DEFAULT_JOB_EXPECTATIONS);

        // Get all the profileList where jobExpectations equals to UPDATED_JOB_EXPECTATIONS
        defaultProfileShouldNotBeFound("jobExpectations.equals=" + UPDATED_JOB_EXPECTATIONS);
    }

    @Test
    @Transactional
    public void getAllProfilesByJobExpectationsIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where jobExpectations in DEFAULT_JOB_EXPECTATIONS or UPDATED_JOB_EXPECTATIONS
        defaultProfileShouldBeFound("jobExpectations.in=" + DEFAULT_JOB_EXPECTATIONS + "," + UPDATED_JOB_EXPECTATIONS);

        // Get all the profileList where jobExpectations equals to UPDATED_JOB_EXPECTATIONS
        defaultProfileShouldNotBeFound("jobExpectations.in=" + UPDATED_JOB_EXPECTATIONS);
    }

    @Test
    @Transactional
    public void getAllProfilesByJobExpectationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where jobExpectations is not null
        defaultProfileShouldBeFound("jobExpectations.specified=true");

        // Get all the profileList where jobExpectations is null
        defaultProfileShouldNotBeFound("jobExpectations.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAchievementsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where achievements equals to DEFAULT_ACHIEVEMENTS
        defaultProfileShouldBeFound("achievements.equals=" + DEFAULT_ACHIEVEMENTS);

        // Get all the profileList where achievements equals to UPDATED_ACHIEVEMENTS
        defaultProfileShouldNotBeFound("achievements.equals=" + UPDATED_ACHIEVEMENTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAchievementsIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where achievements in DEFAULT_ACHIEVEMENTS or UPDATED_ACHIEVEMENTS
        defaultProfileShouldBeFound("achievements.in=" + DEFAULT_ACHIEVEMENTS + "," + UPDATED_ACHIEVEMENTS);

        // Get all the profileList where achievements equals to UPDATED_ACHIEVEMENTS
        defaultProfileShouldNotBeFound("achievements.in=" + UPDATED_ACHIEVEMENTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByAchievementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where achievements is not null
        defaultProfileShouldBeFound("achievements.specified=true");

        // Get all the profileList where achievements is null
        defaultProfileShouldNotBeFound("achievements.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByPositionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where positionName equals to DEFAULT_POSITION_NAME
        defaultProfileShouldBeFound("positionName.equals=" + DEFAULT_POSITION_NAME);

        // Get all the profileList where positionName equals to UPDATED_POSITION_NAME
        defaultProfileShouldNotBeFound("positionName.equals=" + UPDATED_POSITION_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByPositionNameIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where positionName in DEFAULT_POSITION_NAME or UPDATED_POSITION_NAME
        defaultProfileShouldBeFound("positionName.in=" + DEFAULT_POSITION_NAME + "," + UPDATED_POSITION_NAME);

        // Get all the profileList where positionName equals to UPDATED_POSITION_NAME
        defaultProfileShouldNotBeFound("positionName.in=" + UPDATED_POSITION_NAME);
    }

    @Test
    @Transactional
    public void getAllProfilesByPositionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where positionName is not null
        defaultProfileShouldBeFound("positionName.specified=true");

        // Get all the profileList where positionName is null
        defaultProfileShouldNotBeFound("positionName.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaryAmount").value(hasItem(DEFAULT_SALARY_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.intValue())))
            .andExpect(jsonPath("$.[*].jobExpectations").value(hasItem(DEFAULT_JOB_EXPECTATIONS.toString())))
            .andExpect(jsonPath("$.[*].achievements").value(hasItem(DEFAULT_ACHIEVEMENTS.toString())))
            .andExpect(jsonPath("$.[*].positionName").value(hasItem(DEFAULT_POSITION_NAME.toString())))
            .andExpect(jsonPath("$.[*].yContentType").value(hasItem(DEFAULT_Y_CONTENT_TYPE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .salaryAmount(UPDATED_SALARY_AMOUNT)
            .city(UPDATED_CITY)
            .description(UPDATED_DESCRIPTION)
            .experience(UPDATED_EXPERIENCE)
            .jobExpectations(UPDATED_JOB_EXPECTATIONS)
            .achievements(UPDATED_ACHIEVEMENTS)
            .positionName(UPDATED_POSITION_NAME);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getSalaryAmount()).isEqualTo(UPDATED_SALARY_AMOUNT);
        assertThat(testProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProfile.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testProfile.getJobExpectations()).isEqualTo(UPDATED_JOB_EXPECTATIONS);
        assertThat(testProfile.getAchievements()).isEqualTo(UPDATED_ACHIEVEMENTS);
        assertThat(testProfile.getPositionName()).isEqualTo(UPDATED_POSITION_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Get the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        Profile profile2 = new Profile();
        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);
        profile2.setId(2L);
        assertThat(profile1).isNotEqualTo(profile2);
        profile1.setId(null);
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDTO.class);
        ProfileDTO profileDTO1 = new ProfileDTO();
        profileDTO1.setId(1L);
        ProfileDTO profileDTO2 = new ProfileDTO();
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO2.setId(profileDTO1.getId());
        assertThat(profileDTO1).isEqualTo(profileDTO2);
        profileDTO2.setId(2L);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO1.setId(null);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileMapper.fromId(null)).isNull();
    }
}
