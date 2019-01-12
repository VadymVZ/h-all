package app.service;

import app.domain.AccountSkill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Account.
 */
public interface SkillAccountService {

    /**
     * Save an account.
     *
     * @return the persisted entity
     */
    AccountSkill save(AccountSkill accountSkill);

    /**
     * Get all the SkillAccount.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccountSkill> findAll(Pageable pageable);


    /**
     * Get the "id" account.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccountSkill> findOne(Long id);

    /**
     * Delete the "id" SkillAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
