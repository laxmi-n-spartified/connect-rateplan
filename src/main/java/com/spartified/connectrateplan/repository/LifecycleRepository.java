package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.Lifecycle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Lifecycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LifecycleRepository extends JpaRepository<Lifecycle, Long> {}
