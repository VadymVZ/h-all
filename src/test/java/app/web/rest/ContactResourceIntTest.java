package app.web.rest;

import app.AppApp;

import app.domain.Contact;
import app.repository.ContactRepository;
import app.service.ContactService;
import app.service.dto.ContactDTO;
import app.service.mapper.ContactMapper;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.ContactCriteria;
import app.service.ContactQueryService;

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
 * Test class for the ContactResource REST controller.
 *
 * @see ContactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class ContactResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SKYPE = "AAAAAAAAAA";
    private static final String UPDATED_SKYPE = "BBBBBBBBBB";

    private static final String DEFAULT_GITHUB = "AAAAAAAAAA";
    private static final String UPDATED_GITHUB = "BBBBBBBBBB";

    private static final String DEFAULT_TELEGRAM = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;
    
    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactQueryService contactQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactMockMvc;

    private Contact contact;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactResource contactResource = new ContactResource(contactService, contactQueryService, contactMapper);
        this.restContactMockMvc = MockMvcBuilders.standaloneSetup(contactResource)
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
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .skype(DEFAULT_SKYPE)
            .github(DEFAULT_GITHUB)
            .telegram(DEFAULT_TELEGRAM)
            .linkedin(DEFAULT_LINKEDIN)
            .email(DEFAULT_EMAIL);
        return contact;
    }

    @Before
    public void initTest() {
        contact = createEntity(em);
    }

    @Test
    @Transactional
    public void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        restContactMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContact.getSkype()).isEqualTo(DEFAULT_SKYPE);
        assertThat(testContact.getGithub()).isEqualTo(DEFAULT_GITHUB);
        assertThat(testContact.getTelegram()).isEqualTo(DEFAULT_TELEGRAM);
        assertThat(testContact.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createContactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].github").value(hasItem(DEFAULT_GITHUB.toString())))
            .andExpect(jsonPath("$.[*].telegram").value(hasItem(DEFAULT_TELEGRAM.toString())))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.skype").value(DEFAULT_SKYPE.toString()))
            .andExpect(jsonPath("$.github").value(DEFAULT_GITHUB.toString()))
            .andExpect(jsonPath("$.telegram").value(DEFAULT_TELEGRAM.toString()))
            .andExpect(jsonPath("$.linkedin").value(DEFAULT_LINKEDIN.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllContactsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name equals to DEFAULT_NAME
        defaultContactShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactList where name equals to UPDATED_NAME
        defaultContactShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactList where name equals to UPDATED_NAME
        defaultContactShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name is not null
        defaultContactShouldBeFound("name.specified=true");

        // Get all the contactList where name is null
        defaultContactShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where lastName equals to DEFAULT_LAST_NAME
        defaultContactShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the contactList where lastName equals to UPDATED_LAST_NAME
        defaultContactShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultContactShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the contactList where lastName equals to UPDATED_LAST_NAME
        defaultContactShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where lastName is not null
        defaultContactShouldBeFound("lastName.specified=true");

        // Get all the contactList where lastName is null
        defaultContactShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsBySkypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where skype equals to DEFAULT_SKYPE
        defaultContactShouldBeFound("skype.equals=" + DEFAULT_SKYPE);

        // Get all the contactList where skype equals to UPDATED_SKYPE
        defaultContactShouldNotBeFound("skype.equals=" + UPDATED_SKYPE);
    }

    @Test
    @Transactional
    public void getAllContactsBySkypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where skype in DEFAULT_SKYPE or UPDATED_SKYPE
        defaultContactShouldBeFound("skype.in=" + DEFAULT_SKYPE + "," + UPDATED_SKYPE);

        // Get all the contactList where skype equals to UPDATED_SKYPE
        defaultContactShouldNotBeFound("skype.in=" + UPDATED_SKYPE);
    }

    @Test
    @Transactional
    public void getAllContactsBySkypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where skype is not null
        defaultContactShouldBeFound("skype.specified=true");

        // Get all the contactList where skype is null
        defaultContactShouldNotBeFound("skype.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByGithubIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where github equals to DEFAULT_GITHUB
        defaultContactShouldBeFound("github.equals=" + DEFAULT_GITHUB);

        // Get all the contactList where github equals to UPDATED_GITHUB
        defaultContactShouldNotBeFound("github.equals=" + UPDATED_GITHUB);
    }

    @Test
    @Transactional
    public void getAllContactsByGithubIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where github in DEFAULT_GITHUB or UPDATED_GITHUB
        defaultContactShouldBeFound("github.in=" + DEFAULT_GITHUB + "," + UPDATED_GITHUB);

        // Get all the contactList where github equals to UPDATED_GITHUB
        defaultContactShouldNotBeFound("github.in=" + UPDATED_GITHUB);
    }

    @Test
    @Transactional
    public void getAllContactsByGithubIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where github is not null
        defaultContactShouldBeFound("github.specified=true");

        // Get all the contactList where github is null
        defaultContactShouldNotBeFound("github.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByTelegramIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telegram equals to DEFAULT_TELEGRAM
        defaultContactShouldBeFound("telegram.equals=" + DEFAULT_TELEGRAM);

        // Get all the contactList where telegram equals to UPDATED_TELEGRAM
        defaultContactShouldNotBeFound("telegram.equals=" + UPDATED_TELEGRAM);
    }

    @Test
    @Transactional
    public void getAllContactsByTelegramIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telegram in DEFAULT_TELEGRAM or UPDATED_TELEGRAM
        defaultContactShouldBeFound("telegram.in=" + DEFAULT_TELEGRAM + "," + UPDATED_TELEGRAM);

        // Get all the contactList where telegram equals to UPDATED_TELEGRAM
        defaultContactShouldNotBeFound("telegram.in=" + UPDATED_TELEGRAM);
    }

    @Test
    @Transactional
    public void getAllContactsByTelegramIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where telegram is not null
        defaultContactShouldBeFound("telegram.specified=true");

        // Get all the contactList where telegram is null
        defaultContactShouldNotBeFound("telegram.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByLinkedinIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where linkedin equals to DEFAULT_LINKEDIN
        defaultContactShouldBeFound("linkedin.equals=" + DEFAULT_LINKEDIN);

        // Get all the contactList where linkedin equals to UPDATED_LINKEDIN
        defaultContactShouldNotBeFound("linkedin.equals=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllContactsByLinkedinIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where linkedin in DEFAULT_LINKEDIN or UPDATED_LINKEDIN
        defaultContactShouldBeFound("linkedin.in=" + DEFAULT_LINKEDIN + "," + UPDATED_LINKEDIN);

        // Get all the contactList where linkedin equals to UPDATED_LINKEDIN
        defaultContactShouldNotBeFound("linkedin.in=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllContactsByLinkedinIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where linkedin is not null
        defaultContactShouldBeFound("linkedin.specified=true");

        // Get all the contactList where linkedin is null
        defaultContactShouldNotBeFound("linkedin.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email equals to DEFAULT_EMAIL
        defaultContactShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the contactList where email equals to UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContactsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultContactShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the contactList where email equals to UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContactsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email is not null
        defaultContactShouldBeFound("email.specified=true");

        // Get all the contactList where email is null
        defaultContactShouldNotBeFound("email.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContactShouldBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].skype").value(hasItem(DEFAULT_SKYPE.toString())))
            .andExpect(jsonPath("$.[*].github").value(hasItem(DEFAULT_GITHUB.toString())))
            .andExpect(jsonPath("$.[*].telegram").value(hasItem(DEFAULT_TELEGRAM.toString())))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContactShouldNotBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).get();
        // Disconnect from session so that the updates on updatedContact are not directly saved in db
        em.detach(updatedContact);
        updatedContact
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .skype(UPDATED_SKYPE)
            .github(UPDATED_GITHUB)
            .telegram(UPDATED_TELEGRAM)
            .linkedin(UPDATED_LINKEDIN)
            .email(UPDATED_EMAIL);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        restContactMockMvc.perform(put("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContact.getSkype()).isEqualTo(UPDATED_SKYPE);
        assertThat(testContact.getGithub()).isEqualTo(UPDATED_GITHUB);
        assertThat(testContact.getTelegram()).isEqualTo(UPDATED_TELEGRAM);
        assertThat(testContact.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testContact.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc.perform(put("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Get the contact
        restContactMockMvc.perform(delete("/api/contacts/{id}", contact.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);
        contact2.setId(2L);
        assertThat(contact1).isNotEqualTo(contact2);
        contact1.setId(null);
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactDTO.class);
        ContactDTO contactDTO1 = new ContactDTO();
        contactDTO1.setId(1L);
        ContactDTO contactDTO2 = new ContactDTO();
        assertThat(contactDTO1).isNotEqualTo(contactDTO2);
        contactDTO2.setId(contactDTO1.getId());
        assertThat(contactDTO1).isEqualTo(contactDTO2);
        contactDTO2.setId(2L);
        assertThat(contactDTO1).isNotEqualTo(contactDTO2);
        contactDTO1.setId(null);
        assertThat(contactDTO1).isNotEqualTo(contactDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contactMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contactMapper.fromId(null)).isNull();
    }
}
