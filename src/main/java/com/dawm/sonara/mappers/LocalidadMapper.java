package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.localidad.LocalidadCreateDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDetailDTO;
import com.dawm.sonara.dtos.localidad.LocalidadUpdateDTO;
import com.dawm.sonara.entities.Localidad;

import java.util.List;

public class LocalidadMapper {

    // ===============================
    // Entity -> DTO (listado)
    // ===============================
    public static LocalidadDTO toDTO(Localidad entity) {
        if (entity == null) return null;

        LocalidadDTO dto = new LocalidadDTO();
        dto.setId(entity.getId());
        dto.setPais(entity.getPais());
        dto.setNombreCiudad(entity.getNombreCiudad());
        dto.setCodigoPostal(entity.getCodigoPostal());

        return dto;
    }

    public static List<LocalidadDTO> toDTOList(List<Localidad> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(LocalidadMapper::toDTO).toList();
    }

    // ===============================
    // Entity -> DetailDTO
    // ===============================
    public static LocalidadDetailDTO toDetailDTO(Localidad entity) {
        if (entity == null) return null;

        LocalidadDetailDTO dto = new LocalidadDetailDTO();
        dto.setId(entity.getId());
        dto.setPais(entity.getPais());
        dto.setNombreCiudad(entity.getNombreCiudad());
        dto.setCodigoPostal(entity.getCodigoPostal());

        return dto;
    }

    // ===============================
    // Entity -> UpdateDTO
    // ===============================
    public static LocalidadUpdateDTO toUpdateDTO(Localidad entity) {
        if (entity == null) return null;

        LocalidadUpdateDTO dto = new LocalidadUpdateDTO();
        dto.setId(entity.getId());
        dto.setPais(entity.getPais());
        dto.setNombreCiudad(entity.getNombreCiudad());
        dto.setCodigoPostal(entity.getCodigoPostal());

        return dto;
    }

    // ===============================
    // DTO -> Entity (Update)
    // ===============================
    public static Localidad toEntity(LocalidadUpdateDTO dto) {
        if (dto == null) return null;

        Localidad e = new Localidad();
        e.setId(dto.getId());
        e.setPais(dto.getPais());
        e.setNombreCiudad(dto.getNombreCiudad());
        e.setCodigoPostal(dto.getCodigoPostal());

        return e;
    }

    // ===============================
    // DTO -> Entity (Create)
    // ===============================
    public static Localidad toEntity(LocalidadCreateDTO dto) {
        if (dto == null) return null;

        Localidad e = new Localidad();
        // ⚠️ normalmente NO se setea el id en create
        e.setPais(dto.getPais());
        e.setNombreCiudad(dto.getNombreCiudad());
        e.setCodigoPostal(dto.getCodigoPostal());

        return e;
    }

    // ===============================
    // Copy update
    // ===============================
    public static void copyToExistingEntity(LocalidadUpdateDTO dto, Localidad entity) {
        if (dto == null || entity == null) return;

        entity.setPais(dto.getPais());
        entity.setNombreCiudad(dto.getNombreCiudad());
        entity.setCodigoPostal(dto.getCodigoPostal());
    }
}