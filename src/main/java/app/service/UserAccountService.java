package app.service;

import app.domain.User;
import app.domain.UserAccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserAccount.
 */
public interface UserAccountService {

    /**
     * Save a userAccount.
     *
     * @param userAccount the entity to save
     * @return the persisted entity
     */
    UserAccount save(UserAccount userAccount);

    /**
     * Get all the userAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserAccount> findAll(Pageable pageable);


    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserAccount> findOne(Long id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    UserAccount createUserAccount(User user);


    //TODO remove this test
    void addUserAccountSkills(List<Long> ids);
}
