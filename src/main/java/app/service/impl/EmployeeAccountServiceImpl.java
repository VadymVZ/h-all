package app.service.impl;

import app.service.EmployeeAccountService;
import app.domain.EmployeeAccount;
import app.repository.EmployeeAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing EmployeeAccount.
 */
@Service
@Transactional
public class EmployeeAccountServiceImpl implements EmployeeAccountService {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountServiceImpl.class);

    private final EmployeeAccountRepository employeeAccountRepository;

    public EmployeeAccountServiceImpl(EmployeeAccountRepository employeeAccountRepository) {
        this.employeeAccountRepository = employeeAccountRepository;
    }

    /**
     * Save a employeeAccount.
     *
     * @param employeeAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public EmployeeAccount save(EmployeeAccount employeeAccount) {
        log.debug("Request to save EmployeeAccount : {}", employeeAccount);        return employeeAccountRepository.save(employeeAccount);
    }

    /**
     * Get all the employeeAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeAccount> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeAccounts");
        return employeeAccountRepository.findAll(pageable);
    }


    /**
     * Get one employeeAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeAccount> findOne(Long id) {
        log.debug("Request to get EmployeeAccount : {}", id);
        return employeeAccountRepository.findById(id);
    }

    /**
     * Delete the employeeAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeAccount : {}", id);
        employeeAccountRepository.deleteById(id);
    }
}
