package app.web.rest;

import app.domain.Photo;
import app.service.PhotoService;
import app.service.dto.PhotoDTO;
import app.service.mapper.PhotoMapper;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Photo.
 */
@RestController
@RequestMapping("/api")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    private static final String ENTITY_NAME = "photo";

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @Inject
    public PhotoResource(PhotoService photoService, PhotoMapper photoMapper) {
        this.photoService = photoService;
        this.photoMapper = photoMapper;
    }

    /**
     * POST  /photos : Create a new photo.
     *
     * @param photoDTO the photo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new photo, or with status 400 (Bad Request) if the photo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/photos")
    @Timed
    public ResponseEntity<PhotoDTO> createPhoto(@RequestBody PhotoDTO photoDTO) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photoDTO);
        if (photoDTO.getId() != null || photoDTO.getContactId() == null) {
            throw new BadRequestAlertException("A new photo cannot already have an ID or Empty contact", ENTITY_NAME, "idexists");
        }
        Photo photo = photoMapper.toEntity(photoDTO);
        PhotoDTO result = photoMapper.toDto(photoService.save(photo));

        return ResponseEntity.created(new URI("/api/photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photos : Updates an existing photo.
     *
     * @param photoDTO the photo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated photo,
     * or with status 400 (Bad Request) if the photo is not valid,
     * or with status 500 (Internal Server Error) if the photo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/photos")
    @Timed
    public ResponseEntity<PhotoDTO> updatePhoto(@RequestBody PhotoDTO photoDTO) throws URISyntaxException {
        log.debug("REST request to update PhotoDTO : {}", photoDTO);
        if (photoDTO.getId() == null || photoDTO.getContactId() == null) {
            throw new BadRequestAlertException("Invalid photo or contact id", ENTITY_NAME, "idnull");
        }
        Photo photo = photoMapper.toEntity(photoDTO);
        PhotoDTO result = photoMapper.toDto(photoService.save(photo));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, photo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photos : get all the photos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of photos in body
     */
    @GetMapping("/photos")
    @Timed
    public ResponseEntity<List<Photo>> getAllPhotos(Pageable pageable) {
        log.debug("REST request to get a page of Photos");
        Page<Photo> page = photoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/photos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /photos/:id : get the "id" photo.
     *
     * @param id the id of the photo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the photo, or with status 404 (Not Found)
     */
    @GetMapping("/photos/{id}")
    @Timed
    public ResponseEntity<PhotoDTO> getPhoto(@PathVariable Long id) {
        log.debug("REST request to get Photo : {}", id);
        Optional<PhotoDTO> photoDTO = photoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(photoDTO);
    }

    /**
     * DELETE  /photos/:id : delete the "id" photo.
     *
     * @param id the id of the photo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/photos/{id}")
    @Timed
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        log.debug("REST request to delete Photo : {}", id);
        photoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
