package app.service;

import app.domain.StopWord;

import app.service.dto.StopWordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StopWord.
 */
public interface StopWordService {

    /**
     * Save a stopWord.
     *
     * @param stopWord the entity to save
     * @return the persisted entity
     */
    StopWord save(StopWord stopWord);

    /**
     * Get all the stopWords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StopWord> findAll(Pageable pageable);


    /**
     * Get the "id" stopWord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StopWordDTO> findOne(Long id);

    /**
     * Delete the "id" stopWord.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
