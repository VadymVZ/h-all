package app.repository;

import app.domain.LanguageLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LanguageLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageLevelRepository extends JpaRepository<LanguageLevel, Long> {

}
