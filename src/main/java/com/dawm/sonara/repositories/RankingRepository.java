package com.dawm.sonara.repositories;


import com.dawm.sonara.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    @Override
    Optional<Ranking> findById(Long id);
}
