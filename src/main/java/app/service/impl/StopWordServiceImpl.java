package app.service.impl;

import app.service.StopWordService;
import app.domain.StopWord;
import app.repository.StopWordRepository;
import app.service.dto.StopWordDTO;
import app.service.mapper.StopWordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing StopWord.
 */
@Service
@Transactional
public class StopWordServiceImpl implements StopWordService {

    private final Logger log = LoggerFactory.getLogger(StopWordServiceImpl.class);

    private final StopWordRepository stopWordRepository;

    private final StopWordMapper stopWordMapper;

    public StopWordServiceImpl(StopWordRepository stopWordRepository, StopWordMapper stopWordMapper) {
        this.stopWordRepository = stopWordRepository;
        this.stopWordMapper = stopWordMapper;
    }

    /**
     * Save a stopWord.
     *
     * @param stopWord the entity to save
     * @return the persisted entity
     */
    @Override
    public StopWord save(StopWord stopWord) {
        log.debug("Request to save StopWord : {}", stopWord);
        return stopWordRepository.save(stopWord);
    }

    /**
     * Get all the stopWords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StopWord> findAll(Pageable pageable) {
        log.debug("Request to get all StopWords");
        return stopWordRepository.findAll(pageable);
    }


    /**
     * Get one stopWord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StopWordDTO> findOne(Long id) {
        log.debug("Request to get StopWord : {}", id);
        return stopWordRepository.findById(id).map(stopWordMapper::toDto);
    }

    /**
     * Delete the stopWord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StopWord : {}", id);
        stopWordRepository.deleteById(id);
    }
}
