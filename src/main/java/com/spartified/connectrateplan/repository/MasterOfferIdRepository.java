package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.MasterOfferId;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MasterOfferId entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterOfferIdRepository extends JpaRepository<MasterOfferId, Long> {}
