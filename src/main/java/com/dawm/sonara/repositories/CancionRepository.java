package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancionRepository extends JpaRepository<Cancion, Long> {

    @Override
    Optional<Cancion> findById(Long id);
}
