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

import app.domain.StopWord;
import app.domain.*; // for static metamodels
import app.repository.StopWordRepository;
import app.service.dto.StopWordCriteria;

/**
 * Service for executing complex queries for StopWord entities in the database.
 * The main input is a {@link StopWordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StopWord} or a {@link Page} of {@link StopWord} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StopWordQueryService extends QueryService<StopWord> {

    private final Logger log = LoggerFactory.getLogger(StopWordQueryService.class);

    private final StopWordRepository stopWordRepository;

    public StopWordQueryService(StopWordRepository stopWordRepository) {
        this.stopWordRepository = stopWordRepository;
    }

    /**
     * Return a {@link List} of {@link StopWord} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StopWord> findByCriteria(StopWordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StopWord> specification = createSpecification(criteria);
        return stopWordRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StopWord} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StopWord> findByCriteria(StopWordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StopWord> specification = createSpecification(criteria);
        return stopWordRepository.findAll(specification, page);
    }

    /**
     * Function to convert StopWordCriteria to a {@link Specification}
     */
    private Specification<StopWord> createSpecification(StopWordCriteria criteria) {
        Specification<StopWord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StopWord_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), StopWord_.key));
            }
        }
        return specification;
    }
}
