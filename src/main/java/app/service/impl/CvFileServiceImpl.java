package app.service.impl;

import app.service.CvFileService;
import app.domain.CvFile;
import app.repository.CvFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CvFile.
 */
@Service
@Transactional
public class CvFileServiceImpl implements CvFileService {

    private final Logger log = LoggerFactory.getLogger(CvFileServiceImpl.class);

    private final CvFileRepository cvFileRepository;

    public CvFileServiceImpl(CvFileRepository cvFileRepository) {
        this.cvFileRepository = cvFileRepository;
    }

    /**
     * Save a cvFile.
     *
     * @param cvFile the entity to save
     * @return the persisted entity
     */
    @Override
    public CvFile save(CvFile cvFile) {
        log.debug("Request to save CvFile : {}", cvFile);        return cvFileRepository.save(cvFile);
    }

    /**
     * Get all the cvFiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CvFile> findAll(Pageable pageable) {
        log.debug("Request to get all CvFiles");
        return cvFileRepository.findAll(pageable);
    }


    /**
     * Get one cvFile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CvFile> findOne(Long id) {
        log.debug("Request to get CvFile : {}", id);
        return cvFileRepository.findById(id);
    }

    /**
     * Delete the cvFile by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CvFile : {}", id);
        cvFileRepository.deleteById(id);
    }
}
