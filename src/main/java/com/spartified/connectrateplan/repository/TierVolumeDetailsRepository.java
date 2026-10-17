package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.TierVolumeDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TierVolumeDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TierVolumeDetailsRepository extends JpaRepository<TierVolumeDetails, Long> {}
