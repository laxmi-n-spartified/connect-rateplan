package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.SgsnIpList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SgsnIpList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SgsnIpListRepository extends JpaRepository<SgsnIpList, Long> {}
