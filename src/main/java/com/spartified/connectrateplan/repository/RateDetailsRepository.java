package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.RateDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RateDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateDetailsRepository extends JpaRepository<RateDetails, Long> {}
