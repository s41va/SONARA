package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artistasOLD.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ArtistaServiceOLD {

    Page<ArtistasDTO> list(Pageable pageable);

    ArtistasUpdateDTO getForEdit(Long id);

    ArtistasDTO create(ArtistasCreateDTO dto);

    ArtistasDTO update(ArtistasUpdateDTO dto);

    void delete(Long id);

    ArtistasDetailDTO getDetail(Long id);

    List<Genero> findAllGeneros();

    List<ArtistasDTO> listAll(Sort name);
}
