package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.BillingCycle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BillingCycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillingCycleRepository extends JpaRepository<BillingCycle, Long> {}
