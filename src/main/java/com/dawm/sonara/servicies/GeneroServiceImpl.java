package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GeneroServiceImpl implements GeneroService{

    @Override
    public void create(GenerosCreateDTO dto) {

    }

    @Override
    public void update(GenerosUpdateDTO dto) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public GenerosUpdateDTO getForEdit(Long id) {
        return null;
    }
}
