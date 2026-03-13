package com.dawm.sonara.repositories;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Artista;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistasRepository extends JpaRepository<Artista, Long> {
    Optional<Artista> findByNombre(String nombre);
    boolean existsByNombre(String name) ;
    boolean existsByNombreAndIdNot(String name, Long id);
    //Page<Artista> findAll(Pageable pageable);
    @Override
    List<Artista> findAll();


}
