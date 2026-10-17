package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.States;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the States entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatesRepository extends JpaRepository<States, Long> {}
