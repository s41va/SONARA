package com.dawm.sonara.repositories;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.entities.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    @Override
    Optional<Genero> findById(Long id);

    GenerosUpdateDTO getForEdit(Long id);
    GenerosDTO create(GenerosCreateDTO dto);
    GenerosDTO update(GenerosUpdateDTO dto);
    void delete(Long id);
    GenerosDetailDTO getDetail(Long id);
}
