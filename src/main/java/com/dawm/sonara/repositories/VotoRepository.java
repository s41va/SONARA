package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    // Ranking por Localidad
    @Query("SELECT v.artista.id, v.artista.nombre, v.artista.foto, COUNT(v) as total " +
            "FROM Voto v WHERE v.localidad = :localidad " +
            "GROUP BY v.artista.id, v.artista.nombre, v.artista.foto " +
            "ORDER BY total DESC")
    List<Object[]> findRankingByLocalidad(@Param("localidad") String localidad);

    // Ranking Global
    @Query("SELECT v.artista.id, v.artista.nombre, v.artista.foto, COUNT(v) as total " +
            "FROM Voto v " +
            "GROUP BY v.artista.id, v.artista.nombre, v.artista.foto " +
            "ORDER BY total DESC")
    List<Object[]> findRankingGlobal();

    boolean existsByUsuarioIdAndArtistaId(Long usuarioId, String artistaId);
}