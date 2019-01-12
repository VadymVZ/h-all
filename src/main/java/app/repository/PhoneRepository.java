package app.repository;

import app.domain.Phone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Phone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>, JpaSpecificationExecutor<Phone> {

}
