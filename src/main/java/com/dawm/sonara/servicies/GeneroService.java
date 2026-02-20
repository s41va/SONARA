package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeneroService {

    Page<GenerosDTO> list(Pageable pageable);
    GenerosDTO create(GenerosCreateDTO dto);
    GenerosDTO update(GenerosUpdateDTO dto);
    void delete(Long id);
    GenerosDetailDTO getDetail(Long id);
    GenerosUpdateDTO getForEdit(Long id);
    GenerosUpdateDTO getGeneroById(Long id);
}
