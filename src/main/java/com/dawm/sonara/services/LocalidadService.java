package com.dawm.sonara.services;

import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDetailDTO;
import com.dawm.sonara.dtos.localidad.LocalidadUpdateDTO;
import com.dawm.sonara.entities.Localidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocalidadService {

    List<LocalidadDTO> listAll();

    Page<LocalidadDTO> listPage(Pageable pageable);

    Localidad findById(Long id);

    void create(LocalidadUpdateDTO dto);

    void update(LocalidadUpdateDTO dto);

    void delete(Long id);

    LocalidadDetailDTO getDetail(Long id);



}
