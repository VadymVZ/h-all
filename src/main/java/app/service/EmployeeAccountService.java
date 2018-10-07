package app.service;

import app.domain.EmployeeAccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing EmployeeAccount.
 */
public interface EmployeeAccountService {

    /**
     * Save a employeeAccount.
     *
     * @param employeeAccount the entity to save
     * @return the persisted entity
     */
    EmployeeAccount save(EmployeeAccount employeeAccount);

    /**
     * Get all the employeeAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EmployeeAccount> findAll(Pageable pageable);


    /**
     * Get the "id" employeeAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EmployeeAccount> findOne(Long id);

    /**
     * Delete the "id" employeeAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
