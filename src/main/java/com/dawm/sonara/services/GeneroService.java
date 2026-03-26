package com.dawm.sonara.services;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.entities.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GeneroService {

    Page<GenerosDTO> list(Pageable pageable);
    GenerosUpdateDTO getForEdit(Long id);
    GenerosDTO create(GenerosCreateDTO dto);
    GenerosDTO update(GenerosUpdateDTO dto);
    void delete(Long id);
    GenerosDetailDTO getDetail(Long id);
    List<Genero> findAllGeneros();
}
