package app.web.rest;

import com.codahale.metrics.annotation.Timed;
import app.domain.JobCategory;
import app.service.JobCategoryService;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import app.service.dto.JobCategoryCriteria;
import app.service.JobCategoryQueryService;
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
 * REST controller for managing JobCategory.
 */
@RestController
@RequestMapping("/api")
public class JobCategoryResource {

    private final Logger log = LoggerFactory.getLogger(JobCategoryResource.class);

    private static final String ENTITY_NAME = "jobCategory";

    private final JobCategoryService jobCategoryService;

    private final JobCategoryQueryService jobCategoryQueryService;

    public JobCategoryResource(JobCategoryService jobCategoryService, JobCategoryQueryService jobCategoryQueryService) {
        this.jobCategoryService = jobCategoryService;
        this.jobCategoryQueryService = jobCategoryQueryService;
    }

    /**
     * POST  /job-categories : Create a new jobCategory.
     *
     * @param jobCategory the jobCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobCategory, or with status 400 (Bad Request) if the jobCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-categories")
    @Timed
    public ResponseEntity<JobCategory> createJobCategory(@RequestBody JobCategory jobCategory) throws URISyntaxException {
        log.debug("REST request to save JobCategory : {}", jobCategory);
        if (jobCategory.getId() != null) {
            throw new BadRequestAlertException("A new jobCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobCategory result = jobCategoryService.save(jobCategory);
        return ResponseEntity.created(new URI("/api/job-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-categories : Updates an existing jobCategory.
     *
     * @param jobCategory the jobCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobCategory,
     * or with status 400 (Bad Request) if the jobCategory is not valid,
     * or with status 500 (Internal Server Error) if the jobCategory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-categories")
    @Timed
    public ResponseEntity<JobCategory> updateJobCategory(@RequestBody JobCategory jobCategory) throws URISyntaxException {
        log.debug("REST request to update JobCategory : {}", jobCategory);
        if (jobCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JobCategory result = jobCategoryService.save(jobCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-categories : get all the jobCategories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of jobCategories in body
     */
    @GetMapping("/job-categories")
    @Timed
    public ResponseEntity<List<JobCategory>> getAllJobCategories(JobCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobCategories by criteria: {}", criteria);
        Page<JobCategory> page = jobCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-categories/:id : get the "id" jobCategory.
     *
     * @param id the id of the jobCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobCategory, or with status 404 (Not Found)
     */
    @GetMapping("/job-categories/{id}")
    @Timed
    public ResponseEntity<JobCategory> getJobCategory(@PathVariable Long id) {
        log.debug("REST request to get JobCategory : {}", id);
        Optional<JobCategory> jobCategory = jobCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobCategory);
    }

    /**
     * DELETE  /job-categories/:id : delete the "id" jobCategory.
     *
     * @param id the id of the jobCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobCategory(@PathVariable Long id) {
        log.debug("REST request to delete JobCategory : {}", id);
        jobCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
