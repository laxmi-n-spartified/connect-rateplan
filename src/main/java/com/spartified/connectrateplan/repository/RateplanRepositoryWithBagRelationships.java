package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.Rateplan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RateplanRepositoryWithBagRelationships {
    Optional<Rateplan> fetchBagRelationships(Optional<Rateplan> rateplan);

    List<Rateplan> fetchBagRelationships(List<Rateplan> rateplans);

    Page<Rateplan> fetchBagRelationships(Page<Rateplan> rateplans);
}
