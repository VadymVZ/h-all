package app.web.rest;

import com.codahale.metrics.annotation.Timed;
import app.domain.SkillLevel;
import app.service.SkillLevelService;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import app.service.dto.SkillLevelCriteria;
import app.service.SkillLevelQueryService;
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
 * REST controller for managing SkillLevel.
 */
@RestController
@RequestMapping("/api")
public class SkillLevelResource {

    private final Logger log = LoggerFactory.getLogger(SkillLevelResource.class);

    private static final String ENTITY_NAME = "skillLevel";

    private final SkillLevelService skillLevelService;

    private final SkillLevelQueryService skillLevelQueryService;

    public SkillLevelResource(SkillLevelService skillLevelService, SkillLevelQueryService skillLevelQueryService) {
        this.skillLevelService = skillLevelService;
        this.skillLevelQueryService = skillLevelQueryService;
    }

    /**
     * POST  /skill-levels : Create a new skillLevel.
     *
     * @param skillLevel the skillLevel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skillLevel, or with status 400 (Bad Request) if the skillLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skill-levels")
    @Timed
    public ResponseEntity<SkillLevel> createSkillLevel(@RequestBody SkillLevel skillLevel) throws URISyntaxException {
        log.debug("REST request to save SkillLevel : {}", skillLevel);
        if (skillLevel.getId() != null) {
            throw new BadRequestAlertException("A new skillLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SkillLevel result = skillLevelService.save(skillLevel);
        return ResponseEntity.created(new URI("/api/skill-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skill-levels : Updates an existing skillLevel.
     *
     * @param skillLevel the skillLevel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skillLevel,
     * or with status 400 (Bad Request) if the skillLevel is not valid,
     * or with status 500 (Internal Server Error) if the skillLevel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skill-levels")
    @Timed
    public ResponseEntity<SkillLevel> updateSkillLevel(@RequestBody SkillLevel skillLevel) throws URISyntaxException {
        log.debug("REST request to update SkillLevel : {}", skillLevel);
        if (skillLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SkillLevel result = skillLevelService.save(skillLevel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skillLevel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skill-levels : get all the skillLevels.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of skillLevels in body
     */
    @GetMapping("/skill-levels")
    @Timed
    public ResponseEntity<List<SkillLevel>> getAllSkillLevels(SkillLevelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SkillLevels by criteria: {}", criteria);
        Page<SkillLevel> page = skillLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/skill-levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /skill-levels/:id : get the "id" skillLevel.
     *
     * @param id the id of the skillLevel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skillLevel, or with status 404 (Not Found)
     */
    @GetMapping("/skill-levels/{id}")
    @Timed
    public ResponseEntity<SkillLevel> getSkillLevel(@PathVariable Long id) {
        log.debug("REST request to get SkillLevel : {}", id);
        Optional<SkillLevel> skillLevel = skillLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillLevel);
    }

    /**
     * DELETE  /skill-levels/:id : delete the "id" skillLevel.
     *
     * @param id the id of the skillLevel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skill-levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkillLevel(@PathVariable Long id) {
        log.debug("REST request to delete SkillLevel : {}", id);
        skillLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
