package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface FavoritoService {
    void agregarArtistaAFavoritos(Long usuarioId, ArtistaDTO artistaDTO);
    void eliminarArtistaDeFavoritos(Long usuarioId, String artistaId);
    Set<ArtistaDTO> obtenerMisArtistasFavoritos(Long usuarioId);
}
