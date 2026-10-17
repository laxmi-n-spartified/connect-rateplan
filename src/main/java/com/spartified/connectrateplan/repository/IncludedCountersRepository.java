package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.IncludedCounters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IncludedCounters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncludedCountersRepository extends JpaRepository<IncludedCounters, Long> {}
