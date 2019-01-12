package app.service.impl;

import app.service.LanguageService;
import app.domain.Language;
import app.repository.LanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Language.
 */
@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageServiceImpl.class);

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * Save a language.
     *
     * @param language the entity to save
     * @return the persisted entity
     */
    @Override
    public Language save(Language language) {
        log.debug("Request to save Language : {}", language);        return languageRepository.save(language);
    }

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Language> findAll(Pageable pageable) {
        log.debug("Request to get all Languages");
        return languageRepository.findAll(pageable);
    }


    /**
     * Get one language by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Language> findOne(Long id) {
        log.debug("Request to get Language : {}", id);
        return languageRepository.findById(id);
    }

    /**
     * Delete the language by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Language : {}", id);
        languageRepository.deleteById(id);
    }
}
