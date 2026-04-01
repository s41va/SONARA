package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.concierto.ConciertoCreateDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDTO;
import com.dawm.sonara.dtos.concierto.ConciertoDetailDTO;
import com.dawm.sonara.dtos.concierto.ConciertoUpdateDTO;
import com.dawm.sonara.entities.Concierto;

import java.util.List;

public class ConciertoMapper {

    // ===============================
    // Entity -> DTO (listado)
    // ===============================
    public static ConciertoDTO toDTO(Concierto entity) {
        if (entity == null) return null;

        ConciertoDTO dto = new ConciertoDTO();
        dto.setId(entity.getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setLocal(entity.getLocal());
        dto.setDescripcion(entity.getDescripcion());

        // Relaciones (usar DTOs)
        dto.setArtista(ArtistasMapperOLD.toDTO(entity.getArtistaOLD()));
        dto.setLocalidad(LocalidadMapper.toDTO(entity.getLocalidad()));

        return dto;
    }

    public static List<ConciertoDTO> toDTOList(List<Concierto> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(ConciertoMapper::toDTO).toList();
    }

    // ===============================
    // Entity -> DetailDTO
    // ===============================
    public static ConciertoDetailDTO toDetailDTO(Concierto entity) {
        if (entity == null) return null;

        ConciertoDetailDTO dto = new ConciertoDetailDTO();
        dto.setId(entity.getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setLocal(entity.getLocal());
        dto.setDescripcion(entity.getDescripcion());

        dto.setArtista(ArtistasMapperOLD.toDTO(entity.getArtistaOLD()));
        dto.setLocalidad(LocalidadMapper.toDTO(entity.getLocalidad()));

        return dto;
    }

    // ===============================
    // Entity -> UpdateDTO
    // ===============================
    public static ConciertoUpdateDTO toUpdateDTO(Concierto entity) {
        if (entity == null) return null;

        ConciertoUpdateDTO dto = new ConciertoUpdateDTO();
        dto.setId(entity.getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setLocal(entity.getLocal());
        dto.setDescripcion(entity.getDescripcion());

        // IMPORTANTE: aquí normalmente se envían IDs
        dto.setArtistaId(entity.getArtistaOLD() != null ? entity.getArtistaOLD().getId() : null);
        dto.setLocalidadId(entity.getLocalidad() != null ? entity.getLocalidad().getId() : null);

        return dto;
    }

    // ===============================
    // DTO -> Entity (Update)
    // ===============================
    public static Concierto toEntity(ConciertoUpdateDTO dto) {
        if (dto == null) return null;

        Concierto e = new Concierto();
        e.setId(dto.getId());
        e.setFechaHora(dto.getFechaHora());
        e.setLocal(dto.getLocal());
        e.setDescripcion(dto.getDescripcion());

        // ⚠️ relaciones se setean en el SERVICE (no aquí)
        return e;
    }

    // ===============================
    // DTO -> Entity (Create)
    // ===============================
    public static Concierto toEntity(ConciertoCreateDTO dto) {
        if (dto == null) return null;

        Concierto e = new Concierto();
        e.setFechaHora(dto.getFechaHora());
        e.setLocal(dto.getLocal());
        e.setDescripcion(dto.getDescripcion());

        // ⚠️ relaciones se setean en el SERVICE
        return e;
    }

    // ===============================
    // Copy update
    // ===============================
    public static void copyToExistingEntity(ConciertoUpdateDTO dto, Concierto entity) {
        if (dto == null || entity == null) return;

        entity.setFechaHora(dto.getFechaHora());
        entity.setLocal(dto.getLocal());
        entity.setDescripcion(dto.getDescripcion());

        // ⚠️ relaciones también en el SERVICE
    }
}