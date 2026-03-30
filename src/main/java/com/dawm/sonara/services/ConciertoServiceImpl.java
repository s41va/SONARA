package com.dawm.sonara.services;

import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Concierto;
import com.dawm.sonara.entities.Localidad;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ConciertoMapper;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.repositories.ConciertoRepository;
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
public class ConciertoServiceImpl implements ConciertoService {

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    // ===============================
    // Listados
    // ===============================
    @Override
    public List<ConciertoDTO> listAll() {
        return conciertoRepository.findAll()
                .stream()
                .map(ConciertoMapper::toDTO)
                .toList();
    }

    @Override
    public Page<ConciertoDTO> listPage(Pageable pageable) {
        return conciertoRepository.findAll(pageable)
                .map(ConciertoMapper::toDTO);
    }

    @Override
    public List<ConciertoDTO> listAll(Sort sort) {
        return conciertoRepository.findAll(sort)
                .stream()
                .map(ConciertoMapper::toDTO)
                .toList();
    }

    // ===============================
    // Buscar por ID
    // ===============================
    @Override
    public Concierto findById(Long id) {
        return conciertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concierto", "id", id));
    }

    @Override
    public ConciertoDetailDTO getDetail(Long id) {
        Concierto concierto = findById(id);
        return ConciertoMapper.toDetailDTO(concierto);
    }

    // ===============================
    // Crear
    // ===============================
    @Override
    public ConciertoDTO create(ConciertoCreateDTO dto) {

        Artista artista = artistaRepository.findById(dto.getArtistaId())
                .orElseThrow(() -> new ResourceNotFoundException("Artista", "id", dto.getArtistaId()));

        Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", dto.getLocalidadId()));

        Concierto concierto = ConciertoMapper.toEntity(dto);

        // asignamos las relaciones
        concierto.setArtista(artista);
        concierto.setLocalidad(localidad);

        concierto = conciertoRepository.save(concierto);
        return ConciertoMapper.toDTO(concierto);
    }

    // ===============================
    // Actualizar
    // ===============================
    @Override
    public ConciertoDTO update(ConciertoUpdateDTO dto) {

        Concierto concierto = conciertoRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Concierto", "id", dto.getId()));

        Artista artista = artistaRepository.findById(dto.getArtistaId())
                .orElseThrow(() -> new ResourceNotFoundException("Artista", "id", dto.getArtistaId()));

        Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", dto.getLocalidadId()));

        // copiamos campos simples
        ConciertoMapper.copyToExistingEntity(dto, concierto);

        // actualizamos relaciones
        concierto.setArtista(artista);
        concierto.setLocalidad(localidad);

        concierto = conciertoRepository.save(concierto);
        return ConciertoMapper.toDTO(concierto);
    }

    // ===============================
    // Eliminar
    // ===============================
    @Override
    public void delete(Long id) {
        if (!conciertoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Concierto", "id", id);
        }
        conciertoRepository.deleteById(id);
    }
}