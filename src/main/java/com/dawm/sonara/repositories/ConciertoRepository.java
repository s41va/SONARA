package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Concierto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConciertoRepository extends JpaRepository<Concierto, Long> {
    @Override
    Optional<Concierto> findById(Long id);
    // Busca por nombre de artista, ciudad de la localidad o local del concierto
    Page<Concierto> findByArtistaNombreContainingIgnoreCaseOrLocalidadNombreCiudadContainingIgnoreCaseOrLocalContainingIgnoreCase(
            String artistaNombre,
            String ciudad,
            String local,
            Pageable pageable
    );

    Page<Concierto> findByArtistaNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Concierto> findByLocalidadNombreCiudadContainingIgnoreCase(String localidad, Pageable pageable);

    Page<Concierto> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
