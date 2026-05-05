package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;

import java.util.List;

public interface ArtistaService {
    ArtistaDTO buscarPorNombre(String nombre);
    List<ArtistaDTO> obtenerTodosOrdenados(String campo, String direccion);
    void eliminar(String id);
    ArtistaDTO obtenerPorIdCompleto(String id);
    ArtistaDTO guardarArtistaLocal(ArtistaDTO dto);
}