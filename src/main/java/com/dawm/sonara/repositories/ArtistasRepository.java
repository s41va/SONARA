package com.dawm.sonara.repositories;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Artista;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistasRepository extends JpaRepository<Artista, Long> {
    @Override
    Optional<Artista> findById(Long id);

    ArtistasDTO getForEdit(Long id);
    ArtistasDTO create(ArtistasCreateDTO dto);
    ArtistasDTO update(ArtistasUpdateDTO dto);
    void delete(Long id);
    ArtistasDetailDTO getDetail(Long id);


}
