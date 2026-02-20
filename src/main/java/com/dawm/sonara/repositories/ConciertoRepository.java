package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Concierto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConciertoRepository extends JpaRepository<Concierto, Long> {
    @Override
    Optional<Concierto> findById(Long id);
}
