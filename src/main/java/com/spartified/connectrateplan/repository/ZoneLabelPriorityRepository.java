package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.ZoneLabelPriority;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ZoneLabelPriority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ZoneLabelPriorityRepository extends JpaRepository<ZoneLabelPriority, Long> {}
