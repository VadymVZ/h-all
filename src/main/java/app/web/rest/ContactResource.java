package app.web.rest;

import app.domain.Contact;
import app.service.mapper.ContactMapper;
import com.codahale.metrics.annotation.Timed;
import app.service.ContactService;
import app.web.rest.errors.BadRequestAlertException;
import app.web.rest.util.HeaderUtil;
import app.web.rest.util.PaginationUtil;
import app.service.dto.ContactDTO;
import app.service.dto.ContactCriteria;
import app.service.ContactQueryService;
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
 * REST controller for managing Contact.
 */
@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private static final String ENTITY_NAME = "contact";

    private final ContactService contactService;

    private final ContactQueryService contactQueryService;

    private final ContactMapper contactMapper;

    public ContactResource(ContactService contactService, ContactQueryService contactQueryService, ContactMapper contactMapper) {
        this.contactService = contactService;
        this.contactQueryService = contactQueryService;
        this.contactMapper = contactMapper;
    }

    /**
     * POST  /contacts : Create a new contact.
     *
     * @param contactDTO the contactDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactDTO, or with status 400 (Bad Request) if the contact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contacts")
    @Timed
    public ResponseEntity<Contact> createContact(@RequestBody ContactDTO contactDTO) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contactDTO);
        if (contactDTO.getId() != null || contactDTO.getAccountId() == null) {
            throw new BadRequestAlertException("A new contact cannot already have an ID and should have an account id", ENTITY_NAME, "idexists");
        }

        Contact contact = contactMapper.toEntity(contactDTO);
        Contact result = contactService.save(contact);

        return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contacts : Updates an existing contact.
     *
     * @param contactDTO the contactDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactDTO,
     * or with status 400 (Bad Request) if the contactDTO is not valid,
     * or with status 500 (Internal Server Error) if the contactDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contacts")
    @Timed
    public ResponseEntity<Contact> updateContact(@RequestBody ContactDTO contactDTO) throws URISyntaxException {
        log.debug("REST request to update Contact : {}", contactDTO);
        if (contactDTO.getId() == null || contactDTO.getAccountId() == null) {
            throw new BadRequestAlertException("Invalid contact or account id", ENTITY_NAME, "idnull");
        }
        Contact contact = contactMapper.toEntity(contactDTO);
        Contact result = contactService.save(contact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contacts : get all the contacts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @GetMapping("/contacts")
    @Timed
    public ResponseEntity<List<ContactDTO>> getAllContacts(ContactCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Contacts by criteria: {}", criteria);
        Page<ContactDTO> page = contactQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contacts/:id : get the "id" contact.
     *
     * @param id the id of the contactDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contacts/{id}")
    @Timed
    public ResponseEntity<ContactDTO> getContact(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        Optional<ContactDTO> contactDTO = contactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactDTO);
    }

    /**
     * DELETE  /contacts/:id : delete the "id" contact.
     *
     * @param id the id of the contactDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contacts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        contactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
