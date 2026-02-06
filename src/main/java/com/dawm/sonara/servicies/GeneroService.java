package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;

public interface GeneroService {

    void create(GenerosCreateDTO dto);
    void update(GenerosUpdateDTO dto);
    void delete(Long id);
    GenerosUpdateDTO getForEdit(Long id);
}
