package app.web.rest;

import app.service.dto.StopWordDTO;
import app.service.mapper.StopWordMapper;
import com.codahale.metrics.annotation.Timed;
import app.domain.StopWord;
import app.service.StopWordService;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import app.service.dto.StopWordCriteria;
import app.service.StopWordQueryService;
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
 * REST controller for managing StopWord.
 */
@RestController
@RequestMapping("/api")
public class StopWordResource {

    private final Logger log = LoggerFactory.getLogger(StopWordResource.class);

    private static final String ENTITY_NAME = "stopWord";

    private final StopWordService stopWordService;

    private final StopWordQueryService stopWordQueryService;

    private final StopWordMapper stopWordMapper;

    public StopWordResource(StopWordService stopWordService, StopWordQueryService stopWordQueryService, StopWordMapper stopWordMapper) {
        this.stopWordService = stopWordService;
        this.stopWordQueryService = stopWordQueryService;
        this.stopWordMapper = stopWordMapper;
    }

    /**
     * POST  /stop-words : Create a new stopWord.
     *
     * @param stopWordDTO the stopWord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stopWord, or with status 400 (Bad Request) if the stopWord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stop-words")
    @Timed
    public ResponseEntity<StopWordDTO> createStopWord(@RequestBody StopWordDTO stopWordDTO) throws URISyntaxException {
        log.debug("REST request to save StopWord : {}", stopWordDTO);
        if (stopWordDTO.getId() != null || stopWordDTO.getAccountId() == null) {
            throw new BadRequestAlertException("A new stopWord cannot already have an ID or does not have linked account", ENTITY_NAME, "idexists");
        }
        StopWord stopWord = stopWordMapper.toEntity(stopWordDTO);
        StopWordDTO result = stopWordMapper.toDto(stopWordService.save(stopWord));

        return ResponseEntity.created(new URI("/api/stop-words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stop-words : Updates an existing stopWord.
     *
     * @param stopWordDTO the stopWord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stopWord,
     * or with status 400 (Bad Request) if the stopWord is not valid,
     * or with status 500 (Internal Server Error) if the stopWord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stop-words")
    @Timed
    public ResponseEntity<StopWordDTO> updateStopWord(@RequestBody StopWordDTO stopWordDTO) throws URISyntaxException {
        log.debug("REST request to update StopWord : {}", stopWordDTO);
        if (stopWordDTO.getId() == null || stopWordDTO.getAccountId() == null) {
            throw new BadRequestAlertException("Invalid stopWord or account id", ENTITY_NAME, "idnull");
        }
        StopWord stopWord = stopWordMapper.toEntity(stopWordDTO);
        StopWordDTO result = stopWordMapper.toDto(stopWordService.save(stopWord));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stopWordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stop-words : get all the stopWords.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stopWords in body
     */
    @GetMapping("/stop-words")
    @Timed
    public ResponseEntity<List<StopWord>> getAllStopWords(StopWordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StopWords by criteria: {}", criteria);
        Page<StopWord> page = stopWordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stop-words");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stop-words/:id : get the "id" stopWord.
     *
     * @param id the id of the stopWord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stopWord, or with status 404 (Not Found)
     */
    @GetMapping("/stop-words/{id}")
    @Timed
    public ResponseEntity<StopWordDTO> getStopWord(@PathVariable Long id) {
        log.debug("REST request to get StopWord : {}", id);
        Optional<StopWordDTO> result = stopWordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * DELETE  /stop-words/:id : delete the "id" stopWord.
     *
     * @param id the id of the stopWord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stop-words/{id}")
    @Timed
    public ResponseEntity<Void> deleteStopWord(@PathVariable Long id) {
        log.debug("REST request to delete StopWord : {}", id);
        stopWordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
