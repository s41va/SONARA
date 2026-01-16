package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.ArtistasCreateDTO;
import com.dawm.sonara.dtos.ArtistasDTO;
import com.dawm.sonara.dtos.ArtistasDetailDTO;
import com.dawm.sonara.dtos.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Artista;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistasMapper {
    /**
     * Convierte una entidad {@link Artista} a {@link } (vista simple).
     * Incluye campos de estado de la cuenta relevantes para una vista de lista.
     */
    public static ArtistasDTO toDTO(Artista entity){
        if (entity == null) return null;
        ArtistasDTO dto = new ArtistasDTO();

        dto.setArtista_id(entity.getArtista_id());
        dto.setNombre_artistico(entity.getNombre_artistico());
        dto.setPais(entity.getPais());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    /**
     * Convierte una lista de entidades {@link Artista} a {@link ArtistasDTO}.
     */
    public static List<ArtistasDTO> toDTOList(List<Artista> entities){
        if (entities == null) return List.of();
        return entities.stream().map(ArtistasMapper::toDTO).collect(Collectors.toList());
    }

    //
    // Entity -> DTO (detalle con todos los campos de estado y seguridad)
    //
    /**
     * Convierte una {@link Artista} a {@link ArtistasDetailDTO}, mapeando todos sus campos de seguridad y estado (incluyendo roles).
     */
    public static ArtistasDetailDTO toDetailDTO(Artista entity) {
        if (entity == null) return null;

        ArtistasDetailDTO dto = new ArtistasDetailDTO();
        dto.setArtista_id(entity.getArtista_id());
        dto.setNombre_artistico(entity.getNombre_artistico());
        dto.setPais(entity.getPais());
        dto.setDescripcion(entity.getDescripcion());

        return dto;
    }

    //
    // DTO -> Entity (Creación)
    //
    /**
     * Convierte un DTO de creación {@link ArtistasCreateDTO} a la entidad {@link Artista}.
     * Solo mapea los campos que el usuario proporciona inicialmente (username y quizás la contraseña temporal).
     */
    public static Artista toEntity(ArtistasCreateDTO dto){
        if (dto == null) return null;
        Artista a = new Artista();
        a.setNombre_artistico(dto.getNombre_artistico());
        a.setPais(dto.getPais());
        a.setDescripcion(dto.getDescripcion());


        return a;
    }

    //
    // Entity -> UpdateDTO
    //
    public static Artista toEntity(ArtistasUpdateDTO dto){
        if (dto == null) return null;
        Artista a = new Artista();

        a.setNombre_artistico(dto.getNombre_artistico());
        a.setPais(dto.getPais());
        a.setDescripcion(dto.getDescripcion());

        return a;
    }


    /**
     * Convierte una entidad {@link Artista} a {@link ArtistasUpdateDTO}.
     * Este DTO es útil para recuperar el estado actual para una edición.
     */
    public static ArtistasUpdateDTO toUpdateDTO(Artista entity) {
        if (entity == null) return null;
        ArtistasUpdateDTO dto = new ArtistasUpdateDTO();
        dto.setNombre_artistico(dto.getNombre_artistico());
        dto.setPais(dto.getPais());
        dto.setDescripcion(dto.getDescripcion());

        return dto;
    }

    //
    // DTO -> Entity (Copia a entidad existente)
    //
    /**
     * Copia las propiedades de un DTO de actualización {@link ArtistasUpdateDTO} a una entidad {@link Artista} **existente**.
     */
    public static void copyToExistingEntity(ArtistasUpdateDTO dto,Artista entity){
        if (dto == null || entity == null) return;

        entity.setNombre_artistico(dto.getNombre_artistico());
        entity.setPais(dto.getPais());
        entity.setDescripcion(dto.getDescripcion());

    }

}
