package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaFavoritoDTO;
import java.util.Set;

public interface FavoritoService {

    // Ahora solo pedimos el ID del artista, no el objeto completo
    void agregarArtistaAFavoritos(Long usuarioId, String artistaId);

    void eliminarArtistaDeFavoritos(Long usuarioId, String artistaId);

    // Devuelve el DTO simplificado (id, nombre, foto, genero)
    Set<ArtistaFavoritoDTO> obtenerMisArtistasFavoritos(Long usuarioId);
}