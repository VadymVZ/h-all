package app.service.impl;

import app.service.OccupationService;
import app.domain.Occupation;
import app.repository.OccupationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Occupation.
 */
@Service
@Transactional
public class OccupationServiceImpl implements OccupationService {

    private final Logger log = LoggerFactory.getLogger(OccupationServiceImpl.class);

    private final OccupationRepository occupationRepository;

    public OccupationServiceImpl(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    /**
     * Save a occupation.
     *
     * @param occupation the entity to save
     * @return the persisted entity
     */
    @Override
    public Occupation save(Occupation occupation) {
        log.debug("Request to save Occupation : {}", occupation);        return occupationRepository.save(occupation);
    }

    /**
     * Get all the occupations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Occupation> findAll(Pageable pageable) {
        log.debug("Request to get all Occupations");
        return occupationRepository.findAll(pageable);
    }


    /**
     * Get one occupation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Occupation> findOne(Long id) {
        log.debug("Request to get Occupation : {}", id);
        return occupationRepository.findById(id);
    }

    /**
     * Delete the occupation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Occupation : {}", id);
        occupationRepository.deleteById(id);
    }
}
