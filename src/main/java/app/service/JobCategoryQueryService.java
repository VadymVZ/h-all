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

import app.domain.JobCategory;
import app.domain.*; // for static metamodels
import app.repository.JobCategoryRepository;
import app.service.dto.JobCategoryCriteria;

/**
 * Service for executing complex queries for JobCategory entities in the database.
 * The main input is a {@link JobCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobCategory} or a {@link Page} of {@link JobCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobCategoryQueryService extends QueryService<JobCategory> {

    private final Logger log = LoggerFactory.getLogger(JobCategoryQueryService.class);

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryQueryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    /**
     * Return a {@link List} of {@link JobCategory} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobCategory> findByCriteria(JobCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobCategory> specification = createSpecification(criteria);
        return jobCategoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobCategory} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobCategory> findByCriteria(JobCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobCategory> specification = createSpecification(criteria);
        return jobCategoryRepository.findAll(specification, page);
    }

    /**
     * Function to convert JobCategoryCriteria to a {@link Specification}
     */
    private Specification<JobCategory> createSpecification(JobCategoryCriteria criteria) {
        Specification<JobCategory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JobCategory_.id));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParentId(), JobCategory_.parentId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), JobCategory_.name));
            }
        }
        return specification;
    }
}
