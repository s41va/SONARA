package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.entities.Genero;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre la entidad {@link Genero} y su DTO {@link GenerosDTO}.
 */
public class GeneroMapper {

    /**
     * Convierte una entidad de Género a su representación DTO.
     * @param entity Entidad de base de datos.
     * @return DTO con los datos del género.
     */
    public static GenerosDTO toDTO(Genero entity){
        if (entity == null) return null;
        GenerosDTO dto = new GenerosDTO();

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());

        return dto;
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs.
     * @param entities Lista de géneros de la base de datos.
     * @return Lista de DTOs mapeados.
     */
    public static List<GenerosDTO> toDTOList(List<Genero> entities){
        if (entities == null) return List.of();
        return entities.stream().map(GeneroMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Convierte un DTO en una nueva entidad de Género.
     * @param dto DTO proveniente de la vista.
     * @return Nueva entidad lista para ser persistida.
     */
    public static Genero toEntity(GenerosDTO dto){
        if (dto == null) return null;
        Genero g = new Genero();
        // El ID no se suele setear al crear para dejar que la DB use AUTO_INCREMENT
        g.setNombre(dto.getNombre());

        return g;
    }

    /**
     * Copia las propiedades de un DTO a una entidad existente para su actualización.
     * @param dto DTO con los nuevos datos.
     * @param entity Entidad recuperada de la base de datos.
     */
    public static void copyToExistingEntity(GenerosDTO dto, Genero entity){
        if (dto == null || entity == null) return;

        entity.setNombre(dto.getNombre());
    }
}