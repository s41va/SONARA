package com.dawm.sonara.services;

import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.entities.Genero;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GeneroService {

    Page<GenerosDTO> list(Pageable pageable);
    GenerosDTO getDetail(Long id);
    GenerosDTO create(GenerosDTO dto);
    GenerosDTO update(GenerosDTO dto);
    void delete(Long id);
    List<Genero> findAllGeneros();
    @Nullable List<GenerosDTO> listAllPlain();
    void ensureExists(String genero);
}
