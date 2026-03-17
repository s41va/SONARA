package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    @Override
    Optional<Cancion> findById(Long id);
}
