package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.ArtistaOLD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistasRepositoryOLD extends JpaRepository<ArtistaOLD, Long> {
    Optional<ArtistaOLD> findByNombre(String nombre);
    boolean existsByNombre(String name) ;
    boolean existsByNombreAndIdNot(String name, Long id);
    //Page<Artista> findAll(Pageable pageable);
    @Override
    List<ArtistaOLD> findAll();


}
