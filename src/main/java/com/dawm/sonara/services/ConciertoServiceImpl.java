package com.dawm.sonara.services;

import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.entities.Concierto;
import com.dawm.sonara.entities.Localidad;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ConciertoMapper;
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
    private LocalidadRepository localidadRepository;

    @Autowired
    private ArtistaService artistaApiService; // El que conecta con TheAudioDB

    // ===============================
    // Listados (Rápidos - Solo DB)
    // ===============================
    @Override
    public List<ConciertoDTO> listAll() {
        return conciertoRepository.findAll()
                .stream()
                .map(ConciertoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ConciertoDTO> listAll(Sort sort) {
        return conciertoRepository.findAll(sort)
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
    public Page<ConciertoDTO> list(Pageable pageable) {
        return listPage(pageable);
    }

    // ===============================
    // Búsqueda y Detalle
    // ===============================
    @Override
    public Concierto findById(Long id) {
        return conciertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concierto", "id", id));
    }

    @Override
    public ConciertoDetailDTO getDetail(Long id) {
        // 1. Buscamos el concierto en nuestra DB local
        Concierto concierto = findById(id);

        // 2. Mapeamos a DetailDTO (lo básico)
        ConciertoDetailDTO detail = ConciertoMapper.toDetailDTO(concierto);

        // 3. ENRIQUECER: Llamamos a la API externa para traer biografía, foto, etc.
        // Usamos el artistaNombre que guardamos en la DB
        detail.setArtista(artistaApiService.buscarPorNombre(concierto.getArtistaNombre()));

        return detail;
    }

    // ===============================
    // Crear (Híbrido - DB + API)
    // ===============================
    @Override
    public ConciertoDTO create(ConciertoCreateDTO dto) {
        // 1. Verificar localidad en nuestra DB
        Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", dto.getLocalidadId()));

        // 2. Obtener datos oficiales del artista de la API
        // CAMBIO: Usamos obtenerPorIdCompleto porque dto.getArtistaId() es el ID ("112045"), no el nombre.
        var artistaExterno = artistaApiService.obtenerPorIdCompleto(dto.getArtistaId());

        if (artistaExterno == null) {
            // CAMBIO: El field debe ser "id" para coincidir con tu nueva entidad Artista
            throw new ResourceNotFoundException("Artista", "id", dto.getArtistaId());
        }

        // 3. Crear entidad y setear datos
        Concierto concierto = ConciertoMapper.toEntity(dto);
        concierto.setLocalidad(localidad);
        concierto.setArtistaNombre(artistaExterno.getNombre()); // Nombre oficial (Taylor Swift)
        concierto.setArtistaId(artistaExterno.getId());       // ID oficial (112045)

        return ConciertoMapper.toDTO(conciertoRepository.save(concierto));
    }

    // ===============================
    // Actualizar
    // ===============================
    @Override
    public ConciertoDTO update(ConciertoUpdateDTO dto) {
        Concierto concierto = findById(dto.getId());

        Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", dto.getLocalidadId()));

        // Si el artistaId ha cambiado, actualizamos el nombre consultando la API
        if (!concierto.getArtistaId().equals(dto.getArtistaId())) {
            // CAMBIO: Usar el método de ID, no el de nombre
            var artistaExterno = artistaApiService.obtenerPorIdCompleto(dto.getArtistaId());
            if (artistaExterno != null) {
                concierto.setArtistaNombre(artistaExterno.getNombre());
                concierto.setArtistaId(artistaExterno.getId());
            } else {
                throw new ResourceNotFoundException("Artista", "id", dto.getArtistaId());
            }
        }

        ConciertoMapper.copyToExistingEntity(dto, concierto);
        concierto.setLocalidad(localidad);

        return ConciertoMapper.toDTO(conciertoRepository.save(concierto));
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