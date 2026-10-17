package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.RateplanLcStateModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RateplanLcStateModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateplanLcStateModelRepository extends JpaRepository<RateplanLcStateModel, Long> {}
