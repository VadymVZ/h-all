package app.repository;

import app.domain.AccountSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SkillAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillAccountRepository extends JpaRepository<AccountSkill, Long>, JpaSpecificationExecutor<AccountSkill> {

}
