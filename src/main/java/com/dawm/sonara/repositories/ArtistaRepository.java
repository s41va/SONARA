package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Artista;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, String> {

    // Traer los 10 con más votos
    List<Artista> findTop10ByOrderByVotosRankingDesc();
    List<Artista> findAll(Sort sort);

    Optional<Artista> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}