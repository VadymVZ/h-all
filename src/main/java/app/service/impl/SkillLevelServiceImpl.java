package app.service.impl;

import app.service.SkillLevelService;
import app.domain.SkillLevel;
import app.repository.SkillLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SkillLevel.
 */
@Service
@Transactional
public class SkillLevelServiceImpl implements SkillLevelService {

    private final Logger log = LoggerFactory.getLogger(SkillLevelServiceImpl.class);

    private final SkillLevelRepository skillLevelRepository;

    public SkillLevelServiceImpl(SkillLevelRepository skillLevelRepository) {
        this.skillLevelRepository = skillLevelRepository;
    }

    /**
     * Save a skillLevel.
     *
     * @param skillLevel the entity to save
     * @return the persisted entity
     */
    @Override
    public SkillLevel save(SkillLevel skillLevel) {
        log.debug("Request to save SkillLevel : {}", skillLevel);        return skillLevelRepository.save(skillLevel);
    }

    /**
     * Get all the skillLevels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SkillLevel> findAll(Pageable pageable) {
        log.debug("Request to get all SkillLevels");
        return skillLevelRepository.findAll(pageable);
    }


    /**
     * Get one skillLevel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SkillLevel> findOne(Long id) {
        log.debug("Request to get SkillLevel : {}", id);
        return skillLevelRepository.findById(id);
    }

    /**
     * Delete the skillLevel by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SkillLevel : {}", id);
        skillLevelRepository.deleteById(id);
    }
}
