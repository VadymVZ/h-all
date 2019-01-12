package app.service.impl;

import app.domain.AccountSkill;
import app.repository.SkillAccountRepository;
import app.service.SkillAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Account.
 */
@Service
@Transactional
public class SkillAccountServiceImpl implements SkillAccountService {

    private final Logger log = LoggerFactory.getLogger(SkillAccountServiceImpl.class);

    private final SkillAccountRepository skillAccountRepository;

    public SkillAccountServiceImpl(SkillAccountRepository skillAccountRepository) {
        this.skillAccountRepository = skillAccountRepository;
    }

    /**
     * Save an account.
     *
     * @param accountSkill the entity to save
     * @return the persisted entity
     */
    @Override
    public AccountSkill save(AccountSkill accountSkill) {
        log.debug("Request to save Account : {}", accountSkill);
        return skillAccountRepository.save(accountSkill);
    }

    /**
     * Get all the accounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountSkill> findAll(Pageable pageable) {
        log.debug("Request to get all Accounts");
        return skillAccountRepository.findAll(pageable);
    }


    /**
     * Get one account by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountSkill> findOne(Long id) {
        log.debug("Request to get account : {}", id);
        return skillAccountRepository.findById(id);
    }

    /**
     * Delete the SkillAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Account : {}", id);
        skillAccountRepository.deleteById(id);
    }
}
