package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.artistasOLD.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasUpdateDTO;
import com.dawm.sonara.entities.ArtistaOLD;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistasMapperOLD {

        /**
         * Convierte una entidad {@link ArtistaOLD} a {@link } (vista simple).
         * Incluye campos de estado de la cuenta relevantes para una vista de lista.
         */
        public static ArtistasDTO toDTO(ArtistaOLD entity){
            if (entity == null) return null;
            ArtistasDTO dto = new ArtistasDTO();

            dto.setId(entity.getId());
            dto.setNombre(entity.getNombre());
            dto.setPais(entity.getPais());
            dto.setDescripcion(entity.getDescripcion());
            return dto;
        }

        /**
         * Convierte una lista de entidades {@link ArtistaOLD} a {@link ArtistasDTO}.
         */
        public static List<ArtistasDTO> toDTOList(List<ArtistaOLD> entities){
            if (entities == null) return List.of();
            return entities.stream().map(ArtistasMapperOLD::toDTO).collect(Collectors.toList());
        }

        //
        // Entity -> DTO (detalle con todos los campos de estado y seguridad)
        //
        /**
         * Convierte una {@link ArtistaOLD} a {@link ArtistasDetailDTO}, mapeando todos sus campos de seguridad y estado (incluyendo roles).
         */
        public static ArtistasDetailDTO toDetailDTO(ArtistaOLD entity) {
            if (entity == null) return null;

            ArtistasDetailDTO dto = new ArtistasDetailDTO();
            dto.setId(entity.getId());
            dto.setNombre(entity.getNombre());
            dto.setPais(entity.getPais());
            dto.setDescripcion(entity.getDescripcion());

            return dto;
        }

        //
        // DTO -> Entity (Creación)
        //
        /**
         * Convierte un DTO de creación {@link ArtistasCreateDTO} a la entidad {@link ArtistaOLD}.
         * Solo mapea los campos que el usuario proporciona inicialmente (username y quizás la contraseña temporal).
         */
        public static ArtistaOLD toEntity(ArtistasCreateDTO dto){
            if (dto == null) return null;
            ArtistaOLD a = new ArtistaOLD();
            a.setNombre(dto.getNombre_artistico());
            a.setPais(dto.getPais());
            a.setDescripcion(dto.getDescripcion());


            return a;
        }

        //
        // Entity -> UpdateDTO
        //
        public static ArtistaOLD toEntity(ArtistasUpdateDTO dto){
            if (dto == null) return null;
            ArtistaOLD a = new ArtistaOLD();

            a.setNombre(dto.getNombre());
            a.setPais(dto.getPais());
            a.setDescripcion(dto.getDescripcion());

            return a;
        }


        /**
         * Convierte una entidad {@link ArtistaOLD} a {@link ArtistasUpdateDTO}.
         * Este DTO es útil para recuperar el estado actual para una edición.
         */
        public static ArtistasUpdateDTO toUpdateDTO(ArtistaOLD entity) {
            if (entity == null) return null;
            ArtistasUpdateDTO dto = new ArtistasUpdateDTO();
            dto.setNombre(dto.getNombre());
            dto.setPais(dto.getPais());
            dto.setDescripcion(dto.getDescripcion());

            return dto;
        }

        //
        // DTO -> Entity (Copia a entidad existente)
        //
        /**
         * Copia las propiedades de un DTO de actualización {@link ArtistasUpdateDTO} a una entidad {@link ArtistaOLD} **existente**.
         */
        public static void copyToExistingEntity(ArtistasUpdateDTO dto, ArtistaOLD entity){
            if (dto == null || entity == null) return;

            entity.setNombre(dto.getNombre());
            entity.setPais(dto.getPais());
            entity.setDescripcion(dto.getDescripcion());

        }




}
