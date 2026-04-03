package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;

public interface ArtistaService {
    ArtistaDTO buscarPorNombre(String nombre);
}
