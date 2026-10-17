package com.spartified.connectrateplan.repository;

import com.spartified.connectrateplan.domain.Rateplan;
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
public class RateplanRepositoryWithBagRelationshipsImpl implements RateplanRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String RATEPLANS_PARAMETER = "rateplans";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Rateplan> fetchBagRelationships(Optional<Rateplan> rateplan) {
        return rateplan.map(this::fetchRatingGroups);
    }

    @Override
    public Page<Rateplan> fetchBagRelationships(Page<Rateplan> rateplans) {
        return new PageImpl<>(fetchBagRelationships(rateplans.getContent()), rateplans.getPageable(), rateplans.getTotalElements());
    }

    @Override
    public List<Rateplan> fetchBagRelationships(List<Rateplan> rateplans) {
        return Optional.of(rateplans).map(this::fetchRatingGroups).orElse(List.of());
    }

    Rateplan fetchRatingGroups(Rateplan result) {
        return entityManager
            .createQuery(
                "select rateplan from Rateplan rateplan left join fetch rateplan.ratingGroups where rateplan.id = :id",
                Rateplan.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Rateplan> fetchRatingGroups(List<Rateplan> rateplans) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, rateplans.size()).forEach(index -> order.put(rateplans.get(index).getId(), index));
        List<Rateplan> result = entityManager
            .createQuery(
                "select rateplan from Rateplan rateplan left join fetch rateplan.ratingGroups where rateplan in :rateplans",
                Rateplan.class
            )
            .setParameter(RATEPLANS_PARAMETER, rateplans)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
