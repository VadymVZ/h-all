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

import app.domain.Phone;
import app.domain.*; // for static metamodels
import app.repository.PhoneRepository;
import app.service.dto.PhoneCriteria;

/**
 * Service for executing complex queries for Phone entities in the database.
 * The main input is a {@link PhoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Phone} or a {@link Page} of {@link Phone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhoneQueryService extends QueryService<Phone> {

    private final Logger log = LoggerFactory.getLogger(PhoneQueryService.class);

    private final PhoneRepository phoneRepository;

    public PhoneQueryService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * Return a {@link List} of {@link Phone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Phone> findByCriteria(PhoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Phone} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Phone> findByCriteria(PhoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Phone> specification = createSpecification(criteria);
        return phoneRepository.findAll(specification, page);
    }

    /**
     * Function to convert PhoneCriteria to a {@link Specification}
     */
    private Specification<Phone> createSpecification(PhoneCriteria criteria) {
        Specification<Phone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Phone_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Phone_.number));
            }
        }
        return specification;
    }
}
