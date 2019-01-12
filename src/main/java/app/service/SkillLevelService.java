package app.service;

import app.domain.SkillLevel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SkillLevel.
 */
public interface SkillLevelService {

    /**
     * Save a skillLevel.
     *
     * @param skillLevel the entity to save
     * @return the persisted entity
     */
    SkillLevel save(SkillLevel skillLevel);

    /**
     * Get all the skillLevels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SkillLevel> findAll(Pageable pageable);


    /**
     * Get the "id" skillLevel.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SkillLevel> findOne(Long id);

    /**
     * Delete the "id" skillLevel.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
