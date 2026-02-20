package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistasRepository extends JpaRepository<Artista, Long> {
    @Override
    Optional<Artista> findById(Long id);
}
