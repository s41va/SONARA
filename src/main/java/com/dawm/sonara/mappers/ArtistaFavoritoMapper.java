package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.artista.ArtistaFavoritoDTO;
import com.dawm.sonara.entities.Artista;
import org.springframework.stereotype.Component;

@Component
public class ArtistaFavoritoMapper {

    public ArtistaFavoritoDTO toDTO(Artista artista) {
        if (artista == null) return null;

        return ArtistaFavoritoDTO.builder()
                .id(artista.getId())
                .nombre(artista.getNombre())
                .foto(artista.getFoto())
                .genero(artista.getGenero())
                .build();
    }
}