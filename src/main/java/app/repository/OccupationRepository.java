package app.repository;

import app.domain.Occupation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Occupation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long>, JpaSpecificationExecutor<Occupation> {

}
