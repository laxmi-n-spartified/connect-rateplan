package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.TadigCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TadigCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TadigCodeRepository extends JpaRepository<TadigCode, Long> {}
