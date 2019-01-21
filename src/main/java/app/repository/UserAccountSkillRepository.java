package app.repository;

import app.domain.AccountSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the AccountSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAccountSkillRepository extends JpaRepository<AccountSkill, Long> {

    Optional<AccountSkill> deleteByPkAccountIdAndPkSkillId(Long pkAccountId, Long pkSkillId);
}
