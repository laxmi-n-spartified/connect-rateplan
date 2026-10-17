package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.AccountBillingProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountBillingProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountBillingProfileRepository extends JpaRepository<AccountBillingProfile, Long> {}
