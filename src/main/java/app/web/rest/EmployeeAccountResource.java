package app.web.rest;

import com.codahale.metrics.annotation.Timed;
import app.domain.EmployeeAccount;
import app.service.EmployeeAccountService;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import app.service.dto.EmployeeAccountCriteria;
import app.service.EmployeeAccountQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EmployeeAccount.
 */
@RestController
@RequestMapping("/api")
public class EmployeeAccountResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountResource.class);

    private static final String ENTITY_NAME = "employeeAccount";

    private final EmployeeAccountService employeeAccountService;

    private final EmployeeAccountQueryService employeeAccountQueryService;

    public EmployeeAccountResource(EmployeeAccountService employeeAccountService, EmployeeAccountQueryService employeeAccountQueryService) {
        this.employeeAccountService = employeeAccountService;
        this.employeeAccountQueryService = employeeAccountQueryService;
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
