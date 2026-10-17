package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.TimeInterval;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimeInterval entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeIntervalRepository extends JpaRepository<TimeInterval, Long> {}
