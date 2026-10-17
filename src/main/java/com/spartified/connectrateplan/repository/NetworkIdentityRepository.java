package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.NetworkIdentity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NetworkIdentity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NetworkIdentityRepository extends JpaRepository<NetworkIdentity, Long> {}
