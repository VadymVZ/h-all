package app.repository;

import app.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the UserAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query("SELECT a FROM UserAccount a JOIN FETCH a.accountSkills WHERE a.id = (:id)")
    Optional<UserAccount> getWithSkills(@Param("id") Long id);

}
