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

import app.domain.Occupation;
import app.domain.*; // for static metamodels
import app.repository.OccupationRepository;
import app.service.dto.OccupationCriteria;

/**
 * Service for executing complex queries for Occupation entities in the database.
 * The main input is a {@link OccupationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Occupation} or a {@link Page} of {@link Occupation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OccupationQueryService extends QueryService<Occupation> {

    private final Logger log = LoggerFactory.getLogger(OccupationQueryService.class);

    private final OccupationRepository occupationRepository;

    public OccupationQueryService(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    /**
     * Return a {@link List} of {@link Occupation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Occupation> findByCriteria(OccupationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Occupation> specification = createSpecification(criteria);
        return occupationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Occupation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Occupation> findByCriteria(OccupationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Occupation> specification = createSpecification(criteria);
        return occupationRepository.findAll(specification, page);
    }

    /**
     * Function to convert OccupationCriteria to a {@link Specification}
     */
    private Specification<Occupation> createSpecification(OccupationCriteria criteria) {
        Specification<Occupation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Occupation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Occupation_.name));
            }
        }
        return specification;
    }
}
