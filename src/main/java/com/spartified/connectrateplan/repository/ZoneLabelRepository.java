package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.ZoneLabel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ZoneLabel entity.
 *
 * When extending this class, extend ZoneLabelRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ZoneLabelRepository extends ZoneLabelRepositoryWithBagRelationships, JpaRepository<ZoneLabel, Long> {
    default Optional<ZoneLabel> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<ZoneLabel> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<ZoneLabel> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
