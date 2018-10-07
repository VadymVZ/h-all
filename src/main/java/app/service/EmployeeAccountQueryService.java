package app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import app.domain.EmployeeAccount;
import app.domain.*; // for static metamodels
import app.repository.EmployeeAccountRepository;
import app.service.dto.EmployeeAccountCriteria;

/**
 * Service for executing complex queries for EmployeeAccount entities in the database.
 * The main input is a {@link EmployeeAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeAccount} or a {@link Page} of {@link EmployeeAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeAccountQueryService extends QueryService<EmployeeAccount> {

    private final Logger log = LoggerFactory.getLogger(EmployeeAccountQueryService.class);

    private final EmployeeAccountRepository employeeAccountRepository;

    public EmployeeAccountQueryService(EmployeeAccountRepository employeeAccountRepository) {
        this.employeeAccountRepository = employeeAccountRepository;
    }

    /**
     * Return a {@link List} of {@link EmployeeAccount} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeAccount> findByCriteria(EmployeeAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EmployeeAccount> specification = createSpecification(criteria);
        return employeeAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EmployeeAccount} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeAccount> findByCriteria(EmployeeAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeAccount> specification = createSpecification(criteria);
        return employeeAccountRepository.findAll(specification, page);
    }

    /**
     * Function to convert EmployeeAccountCriteria to a {@link Specification}
     */
    private Specification<EmployeeAccount> createSpecification(EmployeeAccountCriteria criteria) {
        Specification<EmployeeAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EmployeeAccount_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeAccount_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeAccount_.lastName));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), EmployeeAccount_.age));
            }
        }
        return specification;
    }
}
