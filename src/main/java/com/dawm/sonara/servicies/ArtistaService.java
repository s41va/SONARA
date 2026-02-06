package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;

public interface ArtistaService {

    void create(ArtistasCreateDTO dto);
    void update(ArtistasUpdateDTO dto);
    void delete(Long id);
    ArtistasUpdateDTO getForEdit(Long id);
    ArtistasDetailDTO getDetail(Long id);
}
