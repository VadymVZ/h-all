package app.service.impl;

import app.domain.*;
import app.service.UserAccountService;
import app.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing UserAccount.
 */
@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final Logger log = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    /**
     * Save a userAccount.
     *
     * @param userAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public UserAccount save(UserAccount userAccount) {
        log.debug("Request to save UserAccount : {}", userAccount);        return userAccountRepository.save(userAccount);
    }

    /**
     * Get all the userAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAccount> findAll(Pageable pageable) {
        log.debug("Request to get all UserAccounts");
        return userAccountRepository.findAll(pageable);
    }


    /**
     * Get one userAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserAccount> findOne(Long id) {
        log.debug("Request to get UserAccount : {}", id);
        return userAccountRepository.findById(id);
    }

    /**
     * Delete the userAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAccount : {}", id);
        userAccountRepository.deleteById(id);
    }

    @Override
    public UserAccount createUserAccount(User user) {
        UserAccount userAccount = new UserAccount();
        userAccount.setName(user.getLogin() + " account");
        userAccount.setActivated(false);
        userAccount.setRecruiter(false);
        userAccount.setReceiveMailing(true);

        return userAccountRepository.save(userAccount);
    }

    //TODO remove this test
    public void addUserAccountSkills(List<Long> ids) {

        UserAccount account = userAccountRepository.findById(1L).orElse(null);


        AccountSkill accountSkill1 = new AccountSkill();
        accountSkill1.setAccount(new UserAccount());
        accountSkill1.setSkill(new Skill());
        accountSkill1.setSkillLevel(new SkillLevel());
        account.getAccountSkills().add(accountSkill1);

    }
}
