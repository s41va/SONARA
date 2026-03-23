package com.dawm.sonara.services;


import com.dawm.sonara.dtos.localidad.LocalidadCreateDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDetailDTO;
import com.dawm.sonara.dtos.localidad.LocalidadUpdateDTO;
import com.dawm.sonara.entities.Localidad;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.LocalidadMapper;
import com.dawm.sonara.repositories.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocalidadServiceImpl implements LocalidadService{

    @Autowired
    LocalidadRepository localidadRepository;

    @Override
    public List<LocalidadDTO> listAll() {
        return localidadRepository.findAll()
                .stream()
                .map(LocalidadMapper::toDTO)
                .toList();
    }

    @Override
    public Page<LocalidadDTO> listPage(Pageable pageable) {
        return localidadRepository.findAll(pageable).map(LocalidadMapper::toDTO);
    }

    @Override
    public Localidad findById(Long id) {
        return localidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("localidad", "id", id));
    }

    @Override
    public LocalidadDTO create(LocalidadCreateDTO dto) {
        if (localidadRepository.existsByCodigoPostal(dto.getCodigoPostal())) {
            throw new DuplicateResourceException("localidad", "codigoPostal", dto.getCodigoPostal());
        }
        Localidad localidad = LocalidadMapper.toEntity(dto);
        localidad = localidadRepository.save(localidad);
        return LocalidadMapper.toDTO(localidad);
    }

    @Override
    public LocalidadDTO update(LocalidadUpdateDTO dto) {
        if (localidadRepository.existsByCodigoPostalAndIdNot(dto.getCodigoPostal(), dto.getId())){
            throw new DuplicateResourceException("localidad", "codigoPostal", dto.getCodigoPostal());
        }
        Localidad localidad = localidadRepository.findById(dto.getId()).
                orElseThrow(() -> new ResourceNotFoundException("localidad", "id", dto.getId()));
        LocalidadMapper.copyToExistingEntity(dto, localidad);
        localidad = localidadRepository.save(localidad);
        return LocalidadMapper.toDTO(localidad);
    }

    @Override
    public void delete(Long id) {
        if (!localidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("localidad", "id", id);
        }
        localidadRepository.deleteById(id);
    }

    @Override
    public LocalidadDetailDTO getDetail(Long id) {
        Localidad localidad = localidadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("localidad", "id", id));
        return LocalidadMapper.toDetailDTO(localidad);
    }

    @Override
    public Page<LocalidadDTO> list(Pageable pageable) {
        return localidadRepository.findAll(pageable).map(LocalidadMapper::toDTO);
    }

    @Override
    public List<LocalidadDTO> listAll(Sort sort) {
        return localidadRepository.findAll()
                .stream()
                .map(LocalidadMapper::toDTO)
                .toList();
    }

}
