package app.service;

import app.domain.CvFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CvFile.
 */
public interface CvFileService {

    /**
     * Save a cvFile.
     *
     * @param cvFile the entity to save
     * @return the persisted entity
     */
    CvFile save(CvFile cvFile);

    /**
     * Get all the cvFiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CvFile> findAll(Pageable pageable);


    /**
     * Get the "id" cvFile.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CvFile> findOne(Long id);

    /**
     * Delete the "id" cvFile.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
