package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.StateTransitions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StateTransitions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateTransitionsRepository extends JpaRepository<StateTransitions, Long> {}
