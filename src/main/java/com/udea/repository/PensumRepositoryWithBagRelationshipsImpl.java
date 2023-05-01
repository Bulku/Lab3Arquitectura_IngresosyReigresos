package com.udea.repository;

import com.udea.domain.Pensum;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PensumRepositoryWithBagRelationshipsImpl implements PensumRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Pensum> fetchBagRelationships(Optional<Pensum> pensum) {
        return pensum.map(this::fetchMaterias);
    }

    @Override
    public Page<Pensum> fetchBagRelationships(Page<Pensum> pensums) {
        return new PageImpl<>(fetchBagRelationships(pensums.getContent()), pensums.getPageable(), pensums.getTotalElements());
    }

    @Override
    public List<Pensum> fetchBagRelationships(List<Pensum> pensums) {
        return Optional.of(pensums).map(this::fetchMaterias).orElse(Collections.emptyList());
    }

    Pensum fetchMaterias(Pensum result) {
        return entityManager
            .createQuery("select pensum from Pensum pensum left join fetch pensum.materias where pensum is :pensum", Pensum.class)
            .setParameter("pensum", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Pensum> fetchMaterias(List<Pensum> pensums) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pensums.size()).forEach(index -> order.put(pensums.get(index).getId(), index));
        List<Pensum> result = entityManager
            .createQuery("select distinct pensum from Pensum pensum left join fetch pensum.materias where pensum in :pensums", Pensum.class)
            .setParameter("pensums", pensums)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
