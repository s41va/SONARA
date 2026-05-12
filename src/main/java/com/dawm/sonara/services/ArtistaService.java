package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import java.util.List;

public interface ArtistaService {
    ArtistaDTO buscarPorNombre(String nombre);

    // Nuevo método necesario para importar por ID desde la API externa
    ArtistaDTO buscarPorIdExterno(String id);

    List<ArtistaDTO> obtenerTodosOrdenados(String campo, String direccion);
    void eliminar(String id);
    ArtistaDTO obtenerPorIdCompleto(String id);
    ArtistaDTO guardarArtistaLocal(ArtistaDTO dto);
}