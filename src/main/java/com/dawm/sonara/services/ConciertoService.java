package com.dawm.sonara.services;

import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.entities.Concierto;
import com.dawm.sonara.entities.Localidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

public interface ConciertoService {

    List<ConciertoDTO> listAll();
    List<ConciertoDTO> listAll(Sort sort);
    Page<ConciertoDTO> listPage(Pageable pageable);
    Page<ConciertoDTO> list(Pageable pageable);

    Concierto findById(Long id);
    ConciertoDetailDTO getDetail(Long id);

    ConciertoDTO create(ConciertoCreateDTO dto);
    ConciertoDTO update(ConciertoUpdateDTO dto);

    Page<ConciertoDTO> findByFilters(String name, Date date, String localidad, Pageable pageable);

    void delete(Long id);
}