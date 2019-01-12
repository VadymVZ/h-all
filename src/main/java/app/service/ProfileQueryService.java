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

import app.domain.Profile;
import app.domain.*; // for static metamodels
import app.repository.ProfileRepository;
import app.service.dto.ProfileCriteria;
import app.service.dto.ProfileDTO;
import app.service.mapper.ProfileMapper;

/**
 * Service for executing complex queries for Profile entities in the database.
 * The main input is a {@link ProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfileDTO} or a {@link Page} of {@link ProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfileQueryService extends QueryService<Profile> {

    private final Logger log = LoggerFactory.getLogger(ProfileQueryService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileQueryService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Return a {@link List} of {@link ProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findByCriteria(ProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileMapper.toDto(profileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfileDTO> findByCriteria(ProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.findAll(specification, page)
            .map(profileMapper::toDto);
    }

    /**
     * Function to convert ProfileCriteria to a {@link Specification}
     */
    private Specification<Profile> createSpecification(ProfileCriteria criteria) {
        Specification<Profile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Profile_.id));
            }
            if (criteria.getSalaryAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalaryAmount(), Profile_.salaryAmount));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Profile_.city));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Profile_.description));
            }
            if (criteria.getExperience() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExperience(), Profile_.experience));
            }
            if (criteria.getJobExpectations() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobExpectations(), Profile_.jobExpectations));
            }
            if (criteria.getAchievements() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAchievements(), Profile_.achievements));
            }
            if (criteria.getPositionName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPositionName(), Profile_.positionName));
            }
        }
        return specification;
    }
}
