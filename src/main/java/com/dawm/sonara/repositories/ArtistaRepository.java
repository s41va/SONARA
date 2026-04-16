package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Artista;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ArtistaRepository extends JpaRepository<Artista, Integer> {

    // Traer los 10 con más votos
    List<Artista> findTop10ByOrderByVotosRankingDesc();
    List<Artista> findAll(Sort sort);
}