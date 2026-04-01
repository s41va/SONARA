package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Concierto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConciertoRepository extends JpaRepository<Concierto, Long> {
    @Override
    Optional<Concierto> findById(Long id);
}
