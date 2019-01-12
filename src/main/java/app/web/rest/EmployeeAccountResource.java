package app.web.rest;

import app.domain.*;
import app.repository.SkillRepository;
import app.service.*;
import com.codahale.metrics.annotation.Timed;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import app.service.dto.EmployeeAccountCriteria;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

/**
 * REST controller for managing EmployeeAccount.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAccountResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountResource.class);

    private static final String ENTITY_NAME = "employeeAccount";

    private final EmployeeAccountService employeeAccountService;

    private final UserAccountService accountService;
    private final SkillService skillService;
    private final SkillLevelService skillLevelService;
    private final UserService userService;

    private final EmployeeAccountQueryService employeeAccountQueryService;

    @Inject
    private SkillRepository skillRepository;


    public EmployeeAccountResource(EmployeeAccountService employeeAccountService, EmployeeAccountQueryService employeeAccountQueryService,
                                   UserAccountService accountService, SkillService skillService, SkillLevelService skillLevelService,
                                   UserService userService) {
        this.employeeAccountService = employeeAccountService;
        this.accountService = accountService;
        this.skillService = skillService;
        this.skillLevelService = skillLevelService;
        this.employeeAccountQueryService = employeeAccountQueryService;
        this.userService = userService;
    }

    /**
     * POST  /employee-accounts : Create a new employeeAccount.
     *
     * @param employeeAccount the employeeAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeAccount, or with status 400 (Bad Request) if the employeeAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employee-accounts")
    @Timed
    public ResponseEntity<EmployeeAccount> createEmployeeAccount(@Valid @RequestBody EmployeeAccount employeeAccount) throws URISyntaxException {
        log.debug("REST request to save EmployeeAccount : {}", employeeAccount);
        if (employeeAccount.getId() != null) {
            throw new BadRequestAlertException("A new employeeAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeAccount result = employeeAccountService.save(employeeAccount);
        return ResponseEntity.created(new URI("/api/employee-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employee-accounts : Updates an existing employeeAccount.
     *
     * @param employeeAccount the employeeAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeAccount,
     * or with status 400 (Bad Request) if the employeeAccount is not valid,
     * or with status 500 (Internal Server Error) if the employeeAccount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/employee-accounts")
    @Timed
    public ResponseEntity<EmployeeAccount> updateEmployeeAccount(@Valid @RequestBody EmployeeAccount employeeAccount) throws URISyntaxException {
        log.debug("REST request to update EmployeeAccount : {}", employeeAccount);
        if (employeeAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeAccount result = employeeAccountService.save(employeeAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, employeeAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employee-accounts : get all the employeeAccounts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of employeeAccounts in body
     */
    @GetMapping("/employee-accounts")
    @Timed
    public ResponseEntity<List<EmployeeAccount>> getAllEmployeeAccounts(EmployeeAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmployeeAccounts by criteria: {}", criteria);
        Page<EmployeeAccount> page = employeeAccountQueryService.findByCriteria(criteria, pageable);

        /*Set<Authority> authorities = new HashSet<>(1);
        authorities.add(authorityRepository.findOneByName(AuthoritiesConstants.USER));*/
        //Session session = HibernateUtil.getSessionFactory().openSession();
        //session.beginTransaction();


        /*Skill skill = new Skill();
        skill.setName("java");

        SkillLevel skillLevel = new SkillLevel();
        skillLevel.setName("medium");
        skillLevelService.save(skillLevel);

        Account account = new Account();
        account.setReceiveMailing(true);
        account.setRecruiter(true);
        account.setActivated(true);

        accountService.save(account);

        AccountSkill accountSkill = new AccountSkill();
        accountSkill.setAccount(account);
        accountSkill.setSkill(skill);
        accountSkill.setSkillLevel(skillLevel);

        //account.getSkillAccounts().add(skillAccount);
        skill.getAccountSkills().add(accountSkill);
        skillService.save(skill);*/

        /*Account account = new Account();
        account.setActivated(true);
        account.setReceiveMailing(true);
        account.setRecruiter(false);

        User user = userService.getUserWithAuthorities(5L).orElse(null);

        Set<User> users = new HashSet<>();
        users.add(user);

        account.setUsers(users);
        accountService.save(account);
*/
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employee-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employee-accounts/:id : get the "id" employeeAccount.
     *
     * @param id the id of the employeeAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeAccount, or with status 404 (Not Found)
     */
    @GetMapping("/employee-accounts/{id}")
    @Timed
    public ResponseEntity<EmployeeAccount> getEmployeeAccount(@PathVariable Long id) {
        log.debug("REST request to get EmployeeAccount : {}", id);
        Optional<EmployeeAccount> employeeAccount = employeeAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeAccount);
    }

    /**
     * DELETE  /employee-accounts/:id : delete the "id" employeeAccount.
     *
     * @param id the id of the employeeAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employee-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmployeeAccount(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeAccount : {}", id);
        employeeAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
