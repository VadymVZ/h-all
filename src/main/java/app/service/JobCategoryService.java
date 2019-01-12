package app.service;

import app.domain.JobCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing JobCategory.
 */
public interface JobCategoryService {

    /**
     * Save a jobCategory.
     *
     * @param jobCategory the entity to save
     * @return the persisted entity
     */
    JobCategory save(JobCategory jobCategory);

    /**
     * Get all the jobCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<JobCategory> findAll(Pageable pageable);


    /**
     * Get the "id" jobCategory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<JobCategory> findOne(Long id);

    /**
     * Delete the "id" jobCategory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
