package com.dawm.sonara.services;

import com.dawm.sonara.dtos.localidad.LocalidadCreateDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDetailDTO;
import com.dawm.sonara.dtos.localidad.LocalidadUpdateDTO;
import com.dawm.sonara.entities.Localidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LocalidadService {

    List<LocalidadDTO> listAll();

    Page<LocalidadDTO> listPage(Pageable pageable);

    Localidad findById(Long id);

    LocalidadDTO create(LocalidadCreateDTO dto);

    LocalidadDTO update(LocalidadUpdateDTO dto);

    void delete(Long id);

    LocalidadDetailDTO getDetail(Long id);


    Page<LocalidadDTO> list(Pageable pageable);

    List<LocalidadDTO> listAll(Sort sort);
}
