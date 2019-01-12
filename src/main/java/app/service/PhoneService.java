package app.service;

import app.domain.Phone;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Phone.
 */
public interface PhoneService {

    /**
     * Save a phone.
     *
     * @param phone the entity to save
     * @return the persisted entity
     */
    Phone save(Phone phone);

    /**
     * Get all the phones.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Phone> findAll(Pageable pageable);


    /**
     * Get the "id" phone.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Phone> findOne(Long id);

    /**
     * Delete the "id" phone.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
