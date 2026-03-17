package com.dawm.sonara.repositories;


import com.dawm.sonara.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    @Override
    Optional<Ranking> findById(Long id);
}
