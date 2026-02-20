package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistaService {

    Page<ArtistasDTO> list(Pageable pageable);
    ArtistasUpdateDTO getArtistaById(Long id);
    ArtistasDTO create(ArtistasCreateDTO dto);
    ArtistasDTO update(ArtistasUpdateDTO dto);
    void delete(Long id);
    ArtistasUpdateDTO getForEdit(Long id);
    ArtistasDetailDTO getDetail(Long id);
}
