package app.repository;

import app.domain.StopWord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StopWord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StopWordRepository extends JpaRepository<StopWord, Long>, JpaSpecificationExecutor<StopWord> {

}
