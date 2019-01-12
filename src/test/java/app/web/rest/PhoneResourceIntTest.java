package app.web.rest;

import app.AppApp;

import app.domain.Phone;
import app.repository.PhoneRepository;
import app.service.PhoneService;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.PhoneCriteria;
import app.service.PhoneQueryService;

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
 * Test class for the PhoneResource REST controller.
 *
 * @see PhoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class PhoneResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    @Autowired
    private PhoneRepository phoneRepository;
    
    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneQueryService phoneQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPhoneMockMvc;

    private Phone phone;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PhoneResource phoneResource = new PhoneResource(phoneService, phoneQueryService);
        this.restPhoneMockMvc = MockMvcBuilders.standaloneSetup(phoneResource)
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
    public static Phone createEntity(EntityManager em) {
        Phone phone = new Phone()
            .number(DEFAULT_NUMBER);
        return phone;
    }

    @Before
    public void initTest() {
        phone = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhone() throws Exception {
        int databaseSizeBeforeCreate = phoneRepository.findAll().size();

        // Create the Phone
        restPhoneMockMvc.perform(post("/api/phones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isCreated());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate + 1);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void createPhoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = phoneRepository.findAll().size();

        // Create the Phone with an existing ID
        phone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneMockMvc.perform(post("/api/phones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPhones() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList
        restPhoneMockMvc.perform(get("/api/phones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getPhone() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get the phone
        restPhoneMockMvc.perform(get("/api/phones/{id}", phone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(phone.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAllPhonesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number equals to DEFAULT_NUMBER
        defaultPhoneShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the phoneList where number equals to UPDATED_NUMBER
        defaultPhoneShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPhonesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultPhoneShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the phoneList where number equals to UPDATED_NUMBER
        defaultPhoneShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPhonesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        phoneRepository.saveAndFlush(phone);

        // Get all the phoneList where number is not null
        defaultPhoneShouldBeFound("number.specified=true");

        // Get all the phoneList where number is null
        defaultPhoneShouldNotBeFound("number.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPhoneShouldBeFound(String filter) throws Exception {
        restPhoneMockMvc.perform(get("/api/phones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phone.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPhoneShouldNotBeFound(String filter) throws Exception {
        restPhoneMockMvc.perform(get("/api/phones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPhone() throws Exception {
        // Get the phone
        restPhoneMockMvc.perform(get("/api/phones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhone() throws Exception {
        // Initialize the database
        phoneService.save(phone);

        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Update the phone
        Phone updatedPhone = phoneRepository.findById(phone.getId()).get();
        // Disconnect from session so that the updates on updatedPhone are not directly saved in db
        em.detach(updatedPhone);
        updatedPhone
            .number(UPDATED_NUMBER);

        restPhoneMockMvc.perform(put("/api/phones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPhone)))
            .andExpect(status().isOk());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
        Phone testPhone = phoneList.get(phoneList.size() - 1);
        assertThat(testPhone.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingPhone() throws Exception {
        int databaseSizeBeforeUpdate = phoneRepository.findAll().size();

        // Create the Phone

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneMockMvc.perform(put("/api/phones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phone)))
            .andExpect(status().isBadRequest());

        // Validate the Phone in the database
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePhone() throws Exception {
        // Initialize the database
        phoneService.save(phone);

        int databaseSizeBeforeDelete = phoneRepository.findAll().size();

        // Get the phone
        restPhoneMockMvc.perform(delete("/api/phones/{id}", phone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Phone> phoneList = phoneRepository.findAll();
        assertThat(phoneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phone.class);
        Phone phone1 = new Phone();
        phone1.setId(1L);
        Phone phone2 = new Phone();
        phone2.setId(phone1.getId());
        assertThat(phone1).isEqualTo(phone2);
        phone2.setId(2L);
        assertThat(phone1).isNotEqualTo(phone2);
        phone1.setId(null);
        assertThat(phone1).isNotEqualTo(phone2);
    }
}
