package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;

import java.util.List;

public interface ArtistaService {
    ArtistaDTO buscarPorNombre(String nombre);
    List<ArtistaDTO> obtenerRanking(); // Nuevo
    void votarArtista(Integer id, String nombre); // Nuevo
    List<ArtistaDTO> obtenerTodosOrdenados(String campo, String direccion);
    void eliminar(Integer id);
    ArtistaDTO obtenerPorIdCompleto(Integer id);
}