package com.dawm.sonara.services;

import com.dawm.sonara.dtos.cancion.CancionDTO;

public interface CancionService {
    CancionDTO buscarCancion(String artista, String titulo);
}