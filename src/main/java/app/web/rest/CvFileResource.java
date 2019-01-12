package app.web.rest;

import com.codahale.metrics.annotation.Timed;
import app.domain.CvFile;
import app.service.CvFileService;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CvFile.
 */
@RestController
@RequestMapping("/api")
public class CvFileResource {

    private final Logger log = LoggerFactory.getLogger(CvFileResource.class);

    private static final String ENTITY_NAME = "cvFile";

    private final CvFileService cvFileService;

    public CvFileResource(CvFileService cvFileService) {
        this.cvFileService = cvFileService;
    }

    /**
     * POST  /cv-files : Create a new cvFile.
     *
     * @param cvFile the cvFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cvFile, or with status 400 (Bad Request) if the cvFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cv-files")
    @Timed
    public ResponseEntity<CvFile> createCvFile(@RequestBody CvFile cvFile) throws URISyntaxException {
        log.debug("REST request to save CvFile : {}", cvFile);
        if (cvFile.getId() != null) {
            throw new BadRequestAlertException("A new cvFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CvFile result = cvFileService.save(cvFile);
        return ResponseEntity.created(new URI("/api/cv-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cv-files : Updates an existing cvFile.
     *
     * @param cvFile the cvFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cvFile,
     * or with status 400 (Bad Request) if the cvFile is not valid,
     * or with status 500 (Internal Server Error) if the cvFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cv-files")
    @Timed
    public ResponseEntity<CvFile> updateCvFile(@RequestBody CvFile cvFile) throws URISyntaxException {
        log.debug("REST request to update CvFile : {}", cvFile);
        if (cvFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CvFile result = cvFileService.save(cvFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cvFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cv-files : get all the cvFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cvFiles in body
     */
    @GetMapping("/cv-files")
    @Timed
    public ResponseEntity<List<CvFile>> getAllCvFiles(Pageable pageable) {
        log.debug("REST request to get a page of CvFiles");
        Page<CvFile> page = cvFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cv-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cv-files/:id : get the "id" cvFile.
     *
     * @param id the id of the cvFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cvFile, or with status 404 (Not Found)
     */
    @GetMapping("/cv-files/{id}")
    @Timed
    public ResponseEntity<CvFile> getCvFile(@PathVariable Long id) {
        log.debug("REST request to get CvFile : {}", id);
        Optional<CvFile> cvFile = cvFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cvFile);
    }

    /**
     * DELETE  /cv-files/:id : delete the "id" cvFile.
     *
     * @param id the id of the cvFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cv-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteCvFile(@PathVariable Long id) {
        log.debug("REST request to delete CvFile : {}", id);
        cvFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
