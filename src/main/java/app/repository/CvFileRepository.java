package app.repository;

import app.domain.CvFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CvFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CvFileRepository extends JpaRepository<CvFile, Long> {

}
