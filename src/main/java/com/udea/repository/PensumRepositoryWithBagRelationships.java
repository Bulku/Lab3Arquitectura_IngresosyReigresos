package com.udea.repository;

import com.udea.domain.Pensum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PensumRepositoryWithBagRelationships {
    Optional<Pensum> fetchBagRelationships(Optional<Pensum> pensum);

    List<Pensum> fetchBagRelationships(List<Pensum> pensums);

    Page<Pensum> fetchBagRelationships(Page<Pensum> pensums);
}
