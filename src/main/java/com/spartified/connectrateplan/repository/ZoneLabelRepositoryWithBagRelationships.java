package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.ZoneLabel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ZoneLabelRepositoryWithBagRelationships {
    Optional<ZoneLabel> fetchBagRelationships(Optional<ZoneLabel> zoneLabel);

    List<ZoneLabel> fetchBagRelationships(List<ZoneLabel> zoneLabels);

    Page<ZoneLabel> fetchBagRelationships(Page<ZoneLabel> zoneLabels);
}
