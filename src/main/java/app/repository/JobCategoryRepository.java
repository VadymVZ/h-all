package app.repository;

import app.domain.JobCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the JobCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long>, JpaSpecificationExecutor<JobCategory> {

}
