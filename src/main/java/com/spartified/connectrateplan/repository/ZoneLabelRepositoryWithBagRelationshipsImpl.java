package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.ZoneLabel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ZoneLabelRepositoryWithBagRelationshipsImpl implements ZoneLabelRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ZONELABELS_PARAMETER = "zoneLabels";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ZoneLabel> fetchBagRelationships(Optional<ZoneLabel> zoneLabel) {
        return zoneLabel.map(this::fetchNetworkIdentities);
    }

    @Override
    public Page<ZoneLabel> fetchBagRelationships(Page<ZoneLabel> zoneLabels) {
        return new PageImpl<>(fetchBagRelationships(zoneLabels.getContent()), zoneLabels.getPageable(), zoneLabels.getTotalElements());
    }

    @Override
    public List<ZoneLabel> fetchBagRelationships(List<ZoneLabel> zoneLabels) {
        return Optional.of(zoneLabels).map(this::fetchNetworkIdentities).orElse(List.of());
    }

    ZoneLabel fetchNetworkIdentities(ZoneLabel result) {
        return entityManager
            .createQuery(
                "select zoneLabel from ZoneLabel zoneLabel left join fetch zoneLabel.networkIdentities where zoneLabel.id = :id",
                ZoneLabel.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ZoneLabel> fetchNetworkIdentities(List<ZoneLabel> zoneLabels) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, zoneLabels.size()).forEach(index -> order.put(zoneLabels.get(index).getId(), index));
        List<ZoneLabel> result = entityManager
            .createQuery(
                "select zoneLabel from ZoneLabel zoneLabel left join fetch zoneLabel.networkIdentities where zoneLabel in :zoneLabels",
                ZoneLabel.class
            )
            .setParameter(ZONELABELS_PARAMETER, zoneLabels)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
