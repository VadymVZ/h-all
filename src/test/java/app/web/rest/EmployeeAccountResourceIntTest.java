package app.web.rest;

import app.AppApp;

import app.domain.EmployeeAccount;
import app.repository.EmployeeAccountRepository;
import app.service.EmployeeAccountService;
import app.web.rest.errors.ExceptionTranslator;
import app.service.dto.EmployeeAccountCriteria;
import app.service.EmployeeAccountQueryService;

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
 * Test class for the EmployeeAccountResource REST controller.
 *
 * @see EmployeeAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class EmployeeAccountResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 2;
    private static final Integer UPDATED_AGE = 3;

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;
    
    @Autowired
    private EmployeeAccountService employeeAccountService;

    @Autowired
    private EmployeeAccountQueryService employeeAccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeeAccountMockMvc;

    private EmployeeAccount employeeAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeAccountResource employeeAccountResource = new EmployeeAccountResource(employeeAccountService, employeeAccountQueryService);
        this.restEmployeeAccountMockMvc = MockMvcBuilders.standaloneSetup(employeeAccountResource)
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
    public static EmployeeAccount createEntity(EntityManager em) {
        EmployeeAccount employeeAccount = new EmployeeAccount()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .age(DEFAULT_AGE);
        return employeeAccount;
    }

    @Before
    public void initTest() {
        employeeAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeAccount() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountRepository.findAll().size();

        // Create the EmployeeAccount
        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isCreated());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployeeAccount.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployeeAccount.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createEmployeeAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeAccountRepository.findAll().size();

        // Create the EmployeeAccount with an existing ID
        employeeAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setFirstName(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setLastName(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeAccountRepository.findAll().size();
        // set the field null
        employeeAccount.setAge(null);

        // Create the EmployeeAccount, which fails.

        restEmployeeAccountMockMvc.perform(post("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccounts() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }
    
    @Test
    @Transactional
    public void getEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get the employeeAccount
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts/{id}", employeeAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeAccount.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeAccountShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeAccountList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeAccountShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeAccountShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeAccountList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeAccountShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where firstName is not null
        defaultEmployeeAccountShouldBeFound("firstName.specified=true");

        // Get all the employeeAccountList where firstName is null
        defaultEmployeeAccountShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeAccountShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeAccountList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeAccountShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeAccountShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeAccountList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeAccountShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where lastName is not null
        defaultEmployeeAccountShouldBeFound("lastName.specified=true");

        // Get all the employeeAccountList where lastName is null
        defaultEmployeeAccountShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where age equals to DEFAULT_AGE
        defaultEmployeeAccountShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the employeeAccountList where age equals to UPDATED_AGE
        defaultEmployeeAccountShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where age in DEFAULT_AGE or UPDATED_AGE
        defaultEmployeeAccountShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the employeeAccountList where age equals to UPDATED_AGE
        defaultEmployeeAccountShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where age is not null
        defaultEmployeeAccountShouldBeFound("age.specified=true");

        // Get all the employeeAccountList where age is null
        defaultEmployeeAccountShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where age greater than or equals to DEFAULT_AGE
        defaultEmployeeAccountShouldBeFound("age.greaterOrEqualThan=" + DEFAULT_AGE);

        // Get all the employeeAccountList where age greater than or equals to UPDATED_AGE
        defaultEmployeeAccountShouldNotBeFound("age.greaterOrEqualThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllEmployeeAccountsByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeAccountRepository.saveAndFlush(employeeAccount);

        // Get all the employeeAccountList where age less than or equals to DEFAULT_AGE
        defaultEmployeeAccountShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the employeeAccountList where age less than or equals to UPDATED_AGE
        defaultEmployeeAccountShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEmployeeAccountShouldBeFound(String filter) throws Exception {
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEmployeeAccountShouldNotBeFound(String filter) throws Exception {
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingEmployeeAccount() throws Exception {
        // Get the employeeAccount
        restEmployeeAccountMockMvc.perform(get("/api/employee-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountService.save(employeeAccount);

        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Update the employeeAccount
        EmployeeAccount updatedEmployeeAccount = employeeAccountRepository.findById(employeeAccount.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeAccount are not directly saved in db
        em.detach(updatedEmployeeAccount);
        updatedEmployeeAccount
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE);

        restEmployeeAccountMockMvc.perform(put("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeAccount)))
            .andExpect(status().isOk());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
        EmployeeAccount testEmployeeAccount = employeeAccountList.get(employeeAccountList.size() - 1);
        assertThat(testEmployeeAccount.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployeeAccount.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployeeAccount.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeAccount() throws Exception {
        int databaseSizeBeforeUpdate = employeeAccountRepository.findAll().size();

        // Create the EmployeeAccount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeAccountMockMvc.perform(put("/api/employee-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeAccount)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeAccount in the database
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmployeeAccount() throws Exception {
        // Initialize the database
        employeeAccountService.save(employeeAccount);

        int databaseSizeBeforeDelete = employeeAccountRepository.findAll().size();

        // Get the employeeAccount
        restEmployeeAccountMockMvc.perform(delete("/api/employee-accounts/{id}", employeeAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeAccount> employeeAccountList = employeeAccountRepository.findAll();
        assertThat(employeeAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAccount.class);
        EmployeeAccount employeeAccount1 = new EmployeeAccount();
        employeeAccount1.setId(1L);
        EmployeeAccount employeeAccount2 = new EmployeeAccount();
        employeeAccount2.setId(employeeAccount1.getId());
        assertThat(employeeAccount1).isEqualTo(employeeAccount2);
        employeeAccount2.setId(2L);
        assertThat(employeeAccount1).isNotEqualTo(employeeAccount2);
        employeeAccount1.setId(null);
        assertThat(employeeAccount1).isNotEqualTo(employeeAccount2);
    }
}
