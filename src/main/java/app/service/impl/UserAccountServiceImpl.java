package app.service.impl;

import app.domain.AccountSkill;
import app.domain.User;
import app.domain.UserAccount;
import app.repository.UserAccountRepository;
import app.repository.UserAccountSkillRepository;
import app.service.UserAccountService;
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

    private final UserAccountSkillRepository userAccountSkillRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserAccountSkillRepository userAccountSkillRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userAccountSkillRepository = userAccountSkillRepository;
    }


    /**
     * Save a userAccount.
     *
     * @param userAccount the entity to save
     * @return the persisted entity
     */
    @Override
    public UserAccount save(UserAccount userAccount) {
        log.debug("Request to save UserAccount : {}", userAccount);
        return userAccountRepository.save(userAccount);
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

    @Override
    public Optional<UserAccount> addUserAccountSkill(AccountSkill accountSkill) {
        Optional<UserAccount> account = userAccountRepository.findById(accountSkill.getAccount().getId());
        account.map(userAccount ->
            userAccount.getAccountSkills().add(accountSkill));

        return account;
    }

    @Override
    public void deleteUserAccountSkill(AccountSkill accountSkill) {
        log.debug("Request to delete AccountSkill : {}", accountSkill);
        userAccountSkillRepository.deleteByPkAccountIdAndPkSkillId(
            accountSkill.getAccount().getId(), accountSkill.getSkill().getId());
    }

    @Override
    public Optional<UserAccount> getAccountWithAllSkills(Long accountId) {
        log.debug("Request to get AccountSkills by account id : {}", accountId);
        return userAccountRepository.getWithSkills(accountId);
    }


}
