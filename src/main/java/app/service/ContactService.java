package app.service;

import app.domain.Contact;
import app.service.dto.ContactDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Contact.
 */
public interface ContactService {

    /**
     * Save a contact.
     *
     * @param contact the entity to save
     * @return the persisted entity
     */
    Contact save(Contact contact);

    /**
     * Get all the contacts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ContactDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contact.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ContactDTO> findOne(Long id);

    /**
     * Delete the "id" contact.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
