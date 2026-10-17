package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.SpecialNumber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecialNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialNumberRepository extends JpaRepository<SpecialNumber, Long> {}
