package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.GenerosCreateDTO;
import com.dawm.sonara.dtos.GenerosDTO;
import com.dawm.sonara.dtos.GenerosDetailDTO;
import com.dawm.sonara.dtos.GenerosUpdateDTO;
import com.dawm.sonara.entities.Genero;

import java.util.List;
import java.util.stream.Collectors;

public class GeneroMapper {
    public static GenerosDTO toDTO(Genero entity){
        if (entity == null) return null;
        GenerosDTO dto = new GenerosDTO();

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());

        return dto;
    }


    public static List<GenerosDTO> toDTOList(List<Genero> entities){
        if (entities == null) return List.of();
        return entities.stream().map(GeneroMapper::toDTO).collect(Collectors.toList());
    }


    //
    // Entity -> DTO (detalle con todos los campos de estado y seguridad)
    //
    /**
     * Convierte una {@link Genero} a {@link GenerosDetailDTO}, mapeando todos sus campos de seguridad y estado (incluyendo roles).
     */
    public static GenerosDetailDTO toDetailDTO(Genero entity) {
        if (entity == null) return null;

        GenerosDetailDTO dto = new GenerosDetailDTO();
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    //
    // DTO -> Entity (Creación)
    //
    /**
     * Convierte un DTO de creación {@link GenerosCreateDTO} a la entidad {@link Genero}.
     * Solo mapea los campos que el usuario proporciona inicialmente (username y quizás la contraseña temporal).
     */
    public static Genero toEntity(GenerosCreateDTO dto){
        if (dto == null) return null;
        Genero g  = new Genero();
        g.setNombre(dto.getNombre());
        g.setDescripcion(dto.getDescripcion());


        return g;
    }

    //
    // Entity -> UpdateDTO
    //
    public static Genero toEntity(GenerosUpdateDTO dto){
        if (dto == null) return null;
        Genero g = new Genero();

        g.setNombre(dto.getNombre());
        g.setDescripcion(dto.getDescripcion());

        return g;
    }


    /**
     * Convierte una entidad {@link Genero} a {@link GenerosUpdateDTO}.
     * Este DTO es útil para recuperar el estado actual para una edición.
     */
    public static GenerosUpdateDTO toUpdateDTO(Genero entity) {
        if (entity == null) return null;

        GenerosUpdateDTO dto = new GenerosUpdateDTO();
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());

        return dto;
    }

    //
    // DTO -> Entity (Copia a entidad existente)
    //
    /**
     * Copia las propiedades de un DTO de actualización {@link GenerosUpdateDTO} a una entidad {@link Genero} **existente**.
     */
    public static void copyToExistingEntity(GenerosUpdateDTO dto,Genero entity){
        if (dto == null || entity == null) return;

        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());

    }
}
