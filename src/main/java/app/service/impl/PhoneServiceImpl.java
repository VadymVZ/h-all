package app.service.impl;

import app.service.PhoneService;
import app.domain.Phone;
import app.repository.PhoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Phone.
 */
@Service
@Transactional
public class PhoneServiceImpl implements PhoneService {

    private final Logger log = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final PhoneRepository phoneRepository;

    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * Save a phone.
     *
     * @param phone the entity to save
     * @return the persisted entity
     */
    @Override
    public Phone save(Phone phone) {
        log.debug("Request to save Phone : {}", phone);        return phoneRepository.save(phone);
    }

    /**
     * Get all the phones.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Phone> findAll(Pageable pageable) {
        log.debug("Request to get all Phones");
        return phoneRepository.findAll(pageable);
    }


    /**
     * Get one phone by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Phone> findOne(Long id) {
        log.debug("Request to get Phone : {}", id);
        return phoneRepository.findById(id);
    }

    /**
     * Delete the phone by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Phone : {}", id);
        phoneRepository.deleteById(id);
    }
}
