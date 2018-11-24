package app.repository;

import app.domain.PersistentToken;
import app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

    PersistentToken findOneBySeries(String series);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
