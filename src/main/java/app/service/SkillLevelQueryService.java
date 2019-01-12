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

import app.domain.SkillLevel;
import app.domain.*; // for static metamodels
import app.repository.SkillLevelRepository;
import app.service.dto.SkillLevelCriteria;

/**
 * Service for executing complex queries for SkillLevel entities in the database.
 * The main input is a {@link SkillLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SkillLevel} or a {@link Page} of {@link SkillLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SkillLevelQueryService extends QueryService<SkillLevel> {

    private final Logger log = LoggerFactory.getLogger(SkillLevelQueryService.class);

    private final SkillLevelRepository skillLevelRepository;

    public SkillLevelQueryService(SkillLevelRepository skillLevelRepository) {
        this.skillLevelRepository = skillLevelRepository;
    }

    /**
     * Return a {@link List} of {@link SkillLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SkillLevel> findByCriteria(SkillLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SkillLevel> specification = createSpecification(criteria);
        return skillLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SkillLevel} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SkillLevel> findByCriteria(SkillLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SkillLevel> specification = createSpecification(criteria);
        return skillLevelRepository.findAll(specification, page);
    }

    /**
     * Function to convert SkillLevelCriteria to a {@link Specification}
     */
    private Specification<SkillLevel> createSpecification(SkillLevelCriteria criteria) {
        Specification<SkillLevel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SkillLevel_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SkillLevel_.name));
            }
        }
        return specification;
    }
}
