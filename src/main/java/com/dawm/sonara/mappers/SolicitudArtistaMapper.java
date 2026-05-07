package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaDTO;
import com.dawm.sonara.entities.SolicitudArtista;

public class SolicitudArtistaMapper {

    public static SolicitudArtistaDTO toDTO(SolicitudArtista entity) {
        if (entity == null) return null;

        return SolicitudArtistaDTO.builder()
                .id(entity.getId())
                .nombreArtista(entity.getNombreArtista())
                .generoSugerido(entity.getGeneroSugerido())
                .descripcion(entity.getDescripcion())
                .fotoUrl(entity.getFotoUrl())
                .estado(entity.getEstado().name())
                .fechaSolicitud(entity.getFechaSolicitud())
                // Solo enviamos el nombre del usuario que lo solicitó
                .nombreSolicitante(entity.getUsuarioSolicitante() != null ?
                        entity.getUsuarioSolicitante().getNombre() : "Anónimo")
                .build();
    }
}