package app.service.impl;

import app.service.JobCategoryService;
import app.domain.JobCategory;
import app.repository.JobCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing JobCategory.
 */
@Service
@Transactional
public class JobCategoryServiceImpl implements JobCategoryService {

    private final Logger log = LoggerFactory.getLogger(JobCategoryServiceImpl.class);

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryServiceImpl(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    /**
     * Save a jobCategory.
     *
     * @param jobCategory the entity to save
     * @return the persisted entity
     */
    @Override
    public JobCategory save(JobCategory jobCategory) {
        log.debug("Request to save JobCategory : {}", jobCategory);        return jobCategoryRepository.save(jobCategory);
    }

    /**
     * Get all the jobCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<JobCategory> findAll(Pageable pageable) {
        log.debug("Request to get all JobCategories");
        return jobCategoryRepository.findAll(pageable);
    }


    /**
     * Get one jobCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<JobCategory> findOne(Long id) {
        log.debug("Request to get JobCategory : {}", id);
        return jobCategoryRepository.findById(id);
    }

    /**
     * Delete the jobCategory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobCategory : {}", id);
        jobCategoryRepository.deleteById(id);
    }
}
