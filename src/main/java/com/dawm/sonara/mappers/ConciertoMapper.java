package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.entities.Concierto;

public class ConciertoMapper {

    public static ConciertoDTO toDTO(Concierto entity) {
        if (entity == null) return null;
        ConciertoDTO dto = new ConciertoDTO();
        dto.setId(entity.getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setLocal(entity.getLocal());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecio(entity.getPrecio());

        // Creamos un DTO de artista básico con lo que tenemos en la DB
        ArtistaDTO artista = new ArtistaDTO();
        artista.setId(entity.getArtistaId());
        artista.setNombre(entity.getArtistaNombre());
        dto.setArtista(artista);

        dto.setLocalidad(LocalidadMapper.toDTO(entity.getLocalidad()));
        return dto;
    }

    public static Concierto toEntity(ConciertoCreateDTO dto) {
        if (dto == null) return null;
        Concierto e = new Concierto();
        e.setArtistaId(dto.getArtistaId());
        e.setFechaHora(dto.getFechaHora());
        e.setLocal(dto.getLocal());
        e.setDescripcion(dto.getDescripcion());
        e.setPrecio(dto.getPrecio());
        return e;
    }

    public static void copyToExistingEntity(ConciertoUpdateDTO dto, Concierto entity) {
        entity.setFechaHora(dto.getFechaHora());
        entity.setLocal(dto.getLocal());
        entity.setDescripcion(dto.getDescripcion());
        entity.setArtistaId(dto.getArtistaId());
        entity.setPrecio(dto.getPrecio());
    }

    public static ConciertoDetailDTO toDetailDTO(Concierto entity) {
        if (entity == null) return null;

        ConciertoDetailDTO dto = new ConciertoDetailDTO();
        dto.setId(entity.getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setLocal(entity.getLocal());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecio(entity.getPrecio());

        // Mapeamos la localidad usando su propio mapper
        dto.setLocalidad(LocalidadMapper.toDTO(entity.getLocalidad()));

        return dto;
    }
}