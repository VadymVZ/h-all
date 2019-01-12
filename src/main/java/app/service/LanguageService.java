package app.service;

import app.domain.Language;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Language.
 */
public interface LanguageService {

    /**
     * Save a language.
     *
     * @param language the entity to save
     * @return the persisted entity
     */
    Language save(Language language);

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Language> findAll(Pageable pageable);


    /**
     * Get the "id" language.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Language> findOne(Long id);

    /**
     * Delete the "id" language.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
