package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.RatingGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RatingGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingGroupRepository extends JpaRepository<RatingGroup, Long> {}
